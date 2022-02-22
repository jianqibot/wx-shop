package com.github.jianqibot.wxshop.service;

import com.github.jianqibot.wxshop.dao.ShopDao;
import com.github.jianqibot.wxshop.entity.HttpException;
import com.github.jianqibot.wxshop.entity.ItemStatus;
import com.github.jianqibot.wxshop.entity.PageResponse;
import com.github.jianqibot.wxshop.generate.Shop;
import com.github.jianqibot.wxshop.generate.ShopExample;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ShopService {
    private final ShopDao shopDao;

    @Inject
    public ShopService(ShopDao shopDap) {
        this.shopDao = shopDap;
    }

    public PageResponse<Shop> getShop(Integer pageNum, Integer pageSize, Long userId) {
        // get shop total number
        ShopExample countAllCriteria = new ShopExample();
        countAllCriteria.createCriteria().andOwnerUserIdEqualTo(userId);
        int totalShopNumber = shopDao.countShops(countAllCriteria);
        // calc total page
        int totalPageNumber = totalShopNumber % pageSize == 0 ? totalShopNumber / pageSize : totalShopNumber / pageSize + 1;
        // get shop list
        ShopExample pageCriteria = new ShopExample();
        pageCriteria.setLimit(pageSize);
        pageCriteria.setOffset((pageNum - 1) * pageSize);
        pageCriteria.createCriteria().andOwnerUserIdEqualTo(userId);
        List<Shop> shops = shopDao.findShops(pageCriteria);
        return PageResponse.of(pageNum, pageSize, totalPageNumber, shops, null);
    }

    public Shop getShopById(Long shopId) {
        Shop shop = shopDao.findShopByID(shopId);
        if (shop == null) {
            throw HttpException.notFound("shop not found");
        }
        return shop;
    }

    public Shop createShop(Shop shop, Long userId) {
        shop.setOwnerUserId(userId);
        shop.setStatus(ItemStatus.OK.getName());
        shop.setCreatedAt(LocalDateTime.now());
        shop.setUpdatedAt(LocalDateTime.now());
        shopDao.createShop(shop);
        return shopDao.findShopByID(shop.getId());
    }

    public Shop modifyShop(Long shopId, Shop shopForUpdate, Long userId) {
        Shop shopInDB = shopDao.findShopByID(shopId);
        if (shopInDB == null) {
            throw HttpException.notFound("shop not found");
        }
        if (Objects.equals(shopInDB.getOwnerUserId(), userId)) {
            return shopDao.modifyShop(shopForUpdate);
        } else {
            throw HttpException.notAuthorized("not allowed to modify other's shop");
        }
    }

    public Shop deleteShop(Long shopId, Long userId) {
        Shop shopInDB = shopDao.findShopByID(shopId);
        if (shopInDB == null) {
            throw HttpException.notFound("shop not found");
        }
        if (Objects.equals(shopInDB.getOwnerUserId(), userId)) {
            return shopDao.deleteShop(shopId);
        } else {
            throw HttpException.notAuthorized("not allowed to delete other's shop");
        }
    }
}
