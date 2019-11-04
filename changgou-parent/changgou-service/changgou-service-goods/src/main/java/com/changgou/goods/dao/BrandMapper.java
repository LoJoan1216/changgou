package com.changgou.goods.dao;

import com.changgou.goods.pojo.Brand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 商品接口mapper
 * @author Joan
 * @date 2019-11-01 21:50
 */

public interface BrandMapper extends Mapper<Brand> {

    /**
     * 根据分类名称查询品牌
     * @param categoryName
     * @return
     */
    @Select("SELECT " +
            "image,name " +
            "FROM " +
            "tb_brand " +
            "WHERE " +
            "id " +
            "IN " +
            "(SELECT " +
            "brand_id " +
            "FROM " +
            "tb_category_brand " +
            "WHERE " +
            "category_id " +
            "IN " +
            "(SELECT " +
            "id " +
            "FROM " +
            "tb_category " +
            "WHERE " +
            "name=#{categoryName})) " +
            "ORDER BY " +
            "seq")
    public List<Map> findBrandListByCategoryName(@Param("categoryName") String categoryName);


}
