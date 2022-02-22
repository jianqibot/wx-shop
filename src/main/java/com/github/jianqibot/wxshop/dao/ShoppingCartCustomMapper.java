package com.github.jianqibot.wxshop.dao;

import com.github.jianqibot.wxshop.entity.GoodsInShoppingCart;
import com.github.jianqibot.wxshop.entity.ShoppingCartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface ShoppingCartCustomMapper {
    int countShopsInShoppingCart(long userId);

    List<ShoppingCartItem> selectShoppingCartDataByUserId(@Param("userId") long userId,
                                                          @Param("offset") int offset,
                                                          @Param("limit") int limit);

    List<GoodsInShoppingCart> selectShoppingCartDataByUserIdByShopID(@Param("userId") long usrId,
                                                                     @Param("shopId") long shopId);
}
