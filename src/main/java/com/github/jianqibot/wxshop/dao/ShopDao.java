package com.github.jianqibot.wxshop.dao;

import com.github.jianqibot.wxshop.entity.ItemStatus;
import com.github.jianqibot.wxshop.generate.Shop;
import com.github.jianqibot.wxshop.generate.ShopExample;
import com.github.jianqibot.wxshop.generate.ShopMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.util.List;

@Service
public class ShopDao {
    private final ShopMapper shopMapper;

    @Inject
    public ShopDao(ShopMapper shopMapper) {
        this.shopMapper = shopMapper;
    }

    public Shop findShopByID(Long shopId) {
        return shopMapper.selectByPrimaryKey(shopId);
    }

    public int countShops(ShopExample countAllCriteria) {
        return (int) shopMapper.countByExample(countAllCriteria);
    }

    public List<Shop> findShops(ShopExample pageCriteria) {
        return shopMapper.selectByExample(pageCriteria);
    }

    public int createShop(Shop shop) {
        return shopMapper.insert(shop);
    }

    public Shop modifyShop(Shop shopForUpdate) {
        shopMapper.updateByPrimaryKey(shopForUpdate);
        return shopMapper.selectByPrimaryKey(shopForUpdate.getId());
    }

    public Shop deleteShop(Long shopId) {
        Shop shopToBeDeleted = shopMapper.selectByPrimaryKey(shopId);
        shopToBeDeleted.setStatus(ItemStatus.DELETED.getName());
        if (shopMapper.updateByPrimaryKey(shopToBeDeleted) == 0){
            return null;
        }
        return shopToBeDeleted;
    }
}
