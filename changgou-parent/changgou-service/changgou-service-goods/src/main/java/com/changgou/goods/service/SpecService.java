package com.changgou.goods.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 商品规格service
 * @author Joan
 * @date 2019-11-02 14:17
 */
@Service
public interface SpecService {
    /**
     * 根据商品分类名称查询规格
     * @param categoryName
     * @return
     */

    public List<Map> findSpecListByCategoryName(String categoryName);
}
