package com.github.jianqibot.wxshop.service;

import com.github.jianqibot.wxshop.dao.GoodsDao;
import com.github.jianqibot.wxshop.dao.ShopDao;
import com.github.jianqibot.wxshop.entity.HttpException;
import com.github.jianqibot.wxshop.entity.ItemStatus;
import com.github.jianqibot.wxshop.entity.PageResponse;
import com.github.jianqibot.wxshop.generate.Goods;
import com.github.jianqibot.wxshop.generate.GoodsExample;
import com.github.jianqibot.wxshop.generate.Shop;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class GoodsService {
    private final GoodsDao goodsDao;
    private final ShopDao shopDao;

    @Inject
    public GoodsService(GoodsDao goodsDao, ShopDao shopDao) {
        this.goodsDao = goodsDao;
        this.shopDao = shopDao;
    }

    public PageResponse<Goods> getGoods(Integer pageNum, Integer pageSize, Long shopId) {
        // find total goods number
        GoodsExample countAllCriteria = new GoodsExample();
        if (shopId == null) {
            countAllCriteria.createCriteria().andStatusEqualTo(ItemStatus.OK.getName());
        } else {
            countAllCriteria.createCriteria().andStatusEqualTo(ItemStatus.OK.getName())
                    .andShopIdEqualTo(shopId);
        }
        int totalNumber = goodsDao.countAll(countAllCriteria);
        // find page number
        int totalPage = totalNumber % pageSize == 0 ? totalNumber / pageSize : totalNumber / pageSize + 1;
        // fetch goods info
        if (pageNum <= totalPage) { // potential bug?
            GoodsExample pageCriteria = new GoodsExample();
            pageCriteria.setLimit(pageSize);
            pageCriteria.setOffset((pageNum - 1) * pageSize);
            pageCriteria.createCriteria().andShopIdEqualTo(shopId);
            List<Goods> goodsByPage = goodsDao.selectByPage(pageCriteria);
            return PageResponse.of(pageNum, pageSize, totalPage, goodsByPage, null);
        } else {
            return PageResponse.of(pageNum, pageSize, totalPage, null, "page number exceeds maximum");
        }
    }


    public Goods createGoods(Goods goods, Long userId) {
        Shop shop = shopDao.findShopByID(goods.getShopId());
        if (Objects.equals(shop.getOwnerUserId(), userId)) {
            goods.setStatus(ItemStatus.OK.getName());
            goods.setCreatedAt(LocalDateTime.now());
            goods.setUpdatedAt(LocalDateTime.now());
            return goodsDao.insertGoods(goods);
        } else {
            throw HttpException.notAuthorized("Can't create goods in other's shop");
        }
    }


    public Goods updateGoods(Long goodsId, Goods goods, Long userId) {
        // if goods does not exist in DB
        Goods goodsInDB = goodsDao.findGoodsById(goodsId);
        if (goodsInDB == null) {
            throw HttpException.notFound("item not found");
        }
        // check if owner is authorized
        Shop shop = shopDao.findShopByID(goodsInDB.getShopId());
        if (Objects.equals(shop.getOwnerUserId(), userId)) {
            Goods goodsIntoDB = prepareGoodsData(goodsId, goods, goodsInDB);
            goodsDao.updateGoods(goodsIntoDB);
            return goodsIntoDB;
        } else {
            throw HttpException.notAuthorized("Can't update goods does not belong to you");
        }
    }

    public Goods deleteGoodsById(Long goodsId, Long userId) {
        Goods goodsInDB = goodsDao.findGoodsById(goodsId);
        if (goodsInDB == null) {
            throw HttpException.notFound("item not found");
        }

        Shop shop = shopDao.findShopByID(goodsInDB.getShopId());
        if (Objects.equals(shop.getOwnerUserId(), userId)) {
            goodsInDB.setStatus((ItemStatus.DELETED.getName()));
            return goodsDao.deleteGoodsById(goodsInDB);
        } else {
            throw HttpException.notAuthorized("Can't delete goods does not belong to you");
        }
    }


    private Goods prepareGoodsData(Long goodsId, Goods goods, Goods goodsInDB) {
        goodsInDB.setId(goodsId);
        goodsInDB.setName(goods.getName());
        goodsInDB.setDescription(goods.getDescription());
        goodsInDB.setDetails(goods.getDetails());
        goodsInDB.setImgUrl(goods.getImgUrl());
        goodsInDB.setPrice(goods.getPrice());
        goodsInDB.setStock(goods.getStock());
        goodsInDB.setUpdatedAt(LocalDateTime.now());
        return goodsInDB;
    }


    public static class NotAuthorizedException extends RuntimeException {
        public NotAuthorizedException(String message) {
            super(message);
        }
    }
}
