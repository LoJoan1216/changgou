package com.changgou.goods.service.impl;

import com.changgou.goods.dao.SpecMapper;
import com.changgou.goods.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author Joan
 * @date 2019-11-02 14:18
 */
public class SpecServiceImpl implements SpecService {

    @Autowired
    private SpecMapper specMapper;
    /**
     * 根据商品分类信息查询规格
     *
     * @param categoryName
     * @return
     */
    @Override
    public List<Map> findSpecListByCategoryName(String categoryName) {
        System.out.println(categoryName);
        List<Map> specList = specMapper.findSpecListByCategoryName(categoryName);
        for (Map spec : specList) {
            String[] options = ((String) spec.get("options")).split(",");
            spec.put("options", options);
        }
        return specList;
    }
}
