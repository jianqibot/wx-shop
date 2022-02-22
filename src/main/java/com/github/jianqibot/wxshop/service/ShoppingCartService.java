package com.github.jianqibot.wxshop.service;

import com.github.jianqibot.wxshop.dao.GoodsDao;
import com.github.jianqibot.wxshop.dao.ShopDao;
import com.github.jianqibot.wxshop.dao.ShoppingCartCustomMapper;
import com.github.jianqibot.wxshop.entity.*;
import com.github.jianqibot.wxshop.generate.*;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartService {
    private final ShoppingCartCustomMapper shoppingCartCustomMapper;
    private final ShoppingCartMapper shoppingCartMapper;
    private final GoodsDao goodsDao;
    private final ShopDao shopDao;

    @Inject
    public ShoppingCartService(ShoppingCartCustomMapper shoppingCartCustomMapper, ShoppingCartMapper shoppingCartMapper, GoodsDao goodsDao, ShopDao shopDao) {
        this.shoppingCartCustomMapper = shoppingCartCustomMapper;
        this.shoppingCartMapper = shoppingCartMapper;
        this.goodsDao = goodsDao;
        this.shopDao = shopDao;
    }

    public PageResponse<ShoppingCartResponse> getShopCart(Integer pageNum, Integer pageSize, Long userId) {
        // count how many shops in the shopping cart
        int totalNumber = shoppingCartCustomMapper.countShopsInShoppingCart(userId);
        int pageTotal = totalNumber % pageSize == 0 ? totalNumber / pageSize : totalNumber / pageSize + 1;
        int offset = (pageNum - 1) * pageSize;
        List<ShoppingCartItem> pagedData = shoppingCartCustomMapper.selectShoppingCartDataByUserId(userId, offset, pageSize);
        //Map<Shop, List<ShoppingCartItem>> result =
        List<ShoppingCartResponse> pagedDataResponse = pagedData.stream()
                .collect(Collectors.groupingBy(shoppingCartItem -> shoppingCartItem.getShop().getId()))
                .values().stream().map(this::mergeGoodsOfSameShop).collect(Collectors.toList());

        return PageResponse.of(pageNum, pageSize, pageTotal, pagedDataResponse, null);
    }

    private ShoppingCartResponse mergeGoodsOfSameShop(List<ShoppingCartItem> items) {
        ShoppingCartResponse response = new ShoppingCartResponse();
        response.setShop(items.get(0).getShop());
        response.setGoods(items.stream().map(ShoppingCartItem::getGoods).collect(Collectors.toList()));
        return response;
    }

    public Response<ShoppingCartResponse> updateShoppingCart(GoodsInShoppingCart goodsToBeAdded, Long userId) {
        // try to find item in Goods table
        Goods goodsInDB = goodsDao.findGoodsById(goodsToBeAdded.getId());
        if (goodsInDB == null) {
            throw  HttpException.notFound("item not found");
        }
        // get entry in shopping cart
        List<ShoppingCart> result = getEntryInShoppingCartTable(userId, goodsToBeAdded.getId());
        if ( result == null) {
            // goods does not exist in shopping cart
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setShopId(goodsInDB.getShopId());
            shoppingCart.setGoodsId(goodsToBeAdded.getId());
            shoppingCart.setUserId(userId);
            shoppingCart.setNumber(goodsToBeAdded.getNumber());
            shoppingCart.setStatus(ItemStatus.OK.getName());
            shoppingCart.setCreatedAt(LocalDateTime.now());
            shoppingCart.setUpdatedAt(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        } else {
            // goods already exist in shopping cart
            result.get(0).setNumber(result.stream().mapToInt(ShoppingCart::getNumber).sum() + goodsToBeAdded.getNumber());
            ShoppingCart entry = result.get(0);
            shoppingCartMapper.updateByPrimaryKey(entry);
        }

        return  Response.of(prepareShoppingCartResponse(userId, goodsInDB.getShopId()));
    }

    public Response<ShoppingCartResponse> deleteGoodsInShoppingCart(Long goodsId, Long userId) {
        // find entry in shopping cart
        List<ShoppingCart> result = getEntryInShoppingCartTable(userId, goodsId);
        ShoppingCart entry = result.get(0);
        entry.setStatus(ItemStatus.DELETED.getName());
        shoppingCartMapper.updateByPrimaryKey(entry);
        Goods goodsInTable = goodsDao.findGoodsById(goodsId);

        return Response.of(prepareShoppingCartResponse(userId, goodsInTable.getShopId()));
    }


    private List<ShoppingCart> getEntryInShoppingCartTable(Long userId, Long goodsId) {
        ShoppingCartExample exampleByGoodsId = new ShoppingCartExample();
        exampleByGoodsId.createCriteria().andGoodsIdEqualTo(goodsId)
                .andUserIdEqualTo(userId)
                .andStatusEqualTo(ItemStatus.OK.getName());
        return shoppingCartMapper.selectByExample(exampleByGoodsId);
    }

    private ShoppingCartResponse prepareShoppingCartResponse (Long userId, Long shopId) {
        // get shop info
        Shop shop = shopDao.findShopByID(shopId);
        // get goods(GoodsInShoppingCart) list
        List<GoodsInShoppingCart> data = shoppingCartCustomMapper.selectShoppingCartDataByUserIdByShopID(userId, shopId);
        // set fields value
        ShoppingCartResponse shoppingCartResponse = new ShoppingCartResponse();
        shoppingCartResponse.setShop(shop);
        shoppingCartResponse.setGoods(data);
        return shoppingCartResponse;
    }
}
