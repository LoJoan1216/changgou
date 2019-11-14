package com.changgou.goods.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.dao.SpecMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Joan
 * @date 2019-11-02 14:19
 */
@RestController
@RequestMapping("/spec")
public class SpecController {
    @Autowired
    private SpecMapper specMapper;

    /**
     * 根绝商品分类名称查询规格
     *
     * @param categoryName
     * @return
     */
    @GetMapping(value = "/category/{categoryName}")
    public Result findSpecListByCategoryName(@PathVariable("categoryName") String categoryName) {
        List<Map> specListByCategoryName = specMapper.findSpecListByCategoryName(categoryName);
        return new Result(true, StatusCode.OK, "查询成功", specListByCategoryName);
    }


}
