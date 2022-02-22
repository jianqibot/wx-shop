package com.github.jianqibot.wxshop.dao;

import com.github.jianqibot.wxshop.generate.Goods;
import com.github.jianqibot.wxshop.generate.GoodsExample;
import com.github.jianqibot.wxshop.generate.GoodsMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class GoodsDao {
    private final GoodsMapper goodsMapper;

    @Inject
    public GoodsDao(GoodsMapper goodsMapper) {
        this.goodsMapper = goodsMapper;
    }

    public Goods insertGoods(Goods goods) {
        goodsMapper.insert(goods);
        GoodsExample example = new GoodsExample();
        example.createCriteria().andShopIdEqualTo(goods.getShopId())
                .andNameEqualTo(goods.getName())
                .andDescriptionEqualTo(goods.getDescription());

        return goodsMapper.selectByExample(example).get(0);
    }

    public Goods deleteGoodsById(Goods goodsToBeDeleted) {
        goodsMapper.updateByPrimaryKey(goodsToBeDeleted);
        return goodsToBeDeleted;
    }

    public int countAll(GoodsExample goodsExample) {
        return (int) goodsMapper.countByExample(goodsExample);
    }

    public List<Goods> selectByPage(GoodsExample goodsExample) {
        return goodsMapper.selectByExample(goodsExample);
    }

    public Goods findGoodsById(Long goodsId) {
        return goodsMapper.selectByPrimaryKey(goodsId);
    }

    public void updateGoods(Goods goodsInDB) {
        goodsMapper.updateByPrimaryKey(goodsInDB);
    }

}
