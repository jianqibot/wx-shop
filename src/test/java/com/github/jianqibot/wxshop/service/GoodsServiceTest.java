package com.github.jianqibot.wxshop.service;

import com.github.jianqibot.wxshop.dao.GoodsDao;
import com.github.jianqibot.wxshop.dao.ShopDao;
import com.github.jianqibot.wxshop.entity.HttpException;
import com.github.jianqibot.wxshop.generate.Goods;
import com.github.jianqibot.wxshop.generate.Shop;
import com.github.jianqibot.wxshop.generate.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoodsServiceTest {
    @Mock
    private GoodsDao goodsDao;
    @Mock
    private ShopDao shopDao;
    @InjectMocks
    private GoodsService goodsService;
    @Mock
    private Shop shop;
    @Mock
    private Goods goods;
    @Mock
    private List<Goods> goodsList;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setId(1L);
        UserContext.setCurrentUser(user);
    }

    @AfterEach
    public void cleanUserContext() {
        UserContext.setCurrentUser(null);
    }

    @Test
    public void insertGoodsSucceedIfUserIsShopOwner() {
        when(shopDao.findShopByID(anyLong())).thenReturn(shop);
        when(shop.getOwnerUserId()).thenReturn(1L);
        when(goodsDao.insertGoods(goods)).thenReturn(goods);
        assertEquals(goods, goodsService.createGoods(goods, 1L));
    }

    @Test
    public void insertGoodsFailIfUserIsNotShopOwner() {
        when(shopDao.findShopByID(anyLong())).thenReturn(shop);
        when(shop.getOwnerUserId()).thenReturn(2L);
        HttpException thrownException = assertThrows(HttpException.class, () -> goodsService.createGoods(goods, 1L));
        assertEquals(thrownException.getStatusCode(), HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void deleteGoodsSucceedIfUserIsShopOwner() {
        Goods goodsInDB = new Goods();
        goodsInDB.setShopId(12345L);
        when(goodsDao.findGoodsById(anyLong())).thenReturn(goodsInDB);
        when(shopDao.findShopByID(anyLong())).thenReturn(shop);
        when(shop.getOwnerUserId()).thenReturn(1L);
        when(goodsDao.deleteGoodsById(any())).thenReturn(goods);
        assertEquals(goods, goodsService.deleteGoodsById(anyLong(), 1L));
    }

    @Test
    public void deleteGoodsFailIfUserIsNotShopOwner() {
        Goods goodsInDB = new Goods();
        goodsInDB.setShopId(12345L);
        when(goodsDao.findGoodsById(anyLong())).thenReturn(goodsInDB);
        when(shopDao.findShopByID(anyLong())).thenReturn(shop);
        when(shop.getOwnerUserId()).thenReturn(2L);
        HttpException thrownException = assertThrows(HttpException.class, () -> goodsService.deleteGoodsById(anyLong(), 1L));
        assertEquals(thrownException.getStatusCode(), HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void getGoodsSucceedIfPageNumLessThanTotalPage() {
        int pageNum = 2;
        int pageSize = 10;
        when(goodsDao.countAll(any())).thenReturn(40);
        when(goodsDao.selectByPage(any())).thenReturn(goodsList);
        assertEquals(pageNum, goodsService.getGoods(pageNum, pageSize, anyLong()).getPageNum());
        assertEquals(pageSize, goodsService.getGoods(pageNum, pageSize, anyLong()).getPageSize());
        assertEquals(goodsList, goodsService.getGoods(pageNum, pageSize, anyLong()).getData());
    }

    @Test
    public void getGoodsFailIfPageNumGreaterThanTotalPage() {
        int pageNum = 6;
        int pageSize = 10;
        when(goodsDao.countAll(any())).thenReturn(40);
        assertNull(goodsService.getGoods(pageNum, pageSize, anyLong()).getData());
    }

    @Test
    public void updateGoodsThrowNotFoundExceptionWhenItemNotFound() {
        when(goodsDao.findGoodsById(anyLong())).thenReturn(null);
        HttpException thrownException = assertThrows(HttpException.class, () -> goodsService.updateGoods(anyLong(), goods, 1L));
        assertEquals(thrownException.getStatusCode(), HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void updateGoodsThrowNotAuthorizedExceptionWhenInvalidOwnership() {
        when(goodsDao.findGoodsById(anyLong())).thenReturn(goods);
        when(shopDao.findShopByID(anyLong())).thenReturn(shop);
        when(shop.getOwnerUserId()).thenReturn(2L);
        HttpException thrownException = assertThrows(HttpException.class, () -> goodsService.updateGoods(anyLong(), goods, 1L));
        assertEquals(thrownException.getStatusCode(), HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void updateGoodsSucceed() {
        long goodsId = 123L;
        Goods goods = new Goods();
        Goods goodsInDB = new Goods();
        goods.setName("rocket");
        goods.setDescription("a hugh rocket");
        goods.setDetails("a red rocket");
        goodsInDB.setShopId(12345L);
        when(goodsDao.findGoodsById(anyLong())).thenReturn(goodsInDB);
        when(shopDao.findShopByID(anyLong())).thenReturn(shop);
        when(shop.getOwnerUserId()).thenReturn(1L);
        assertEquals(goods.getName(), goodsService.updateGoods(goodsId, goods, 1L).getName());
        assertEquals(goods.getDescription(), goodsService.updateGoods(goodsId, goods, 1L).getDescription());
        assertEquals(goods.getDetails(), goodsService.updateGoods(goodsId, goods, 1L).getDetails());
    }
}
