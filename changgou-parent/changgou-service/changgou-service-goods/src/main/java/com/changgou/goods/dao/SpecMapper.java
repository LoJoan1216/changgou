package com.changgou.goods.dao;

import com.changgou.goods.pojo.Brand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 规格接口mapper
 * @author Joan
 * @date 2019-11-02 14:14
 */
public interface SpecMapper extends Mapper<Brand> {
    /**
     * 根据商品分类名称查询规格
     * @param categoryName
     * @return
     */
    @Select("SELECT " +
            "name,options " +
            "FROM " +
            "tb_spec " +
            "WHERE " +
            "template_id " +
            "IN" +
            "(SELECT" +
            " template_id " +
            "FROM " +
            "tb_category " +
            "WHERE " +
            "name=#{categoryName}) " +
            "ORDER BY " +
            "seq")
    public List<Map> findSpecListByCategoryName(@Param("categoryName") String categoryName);

}
