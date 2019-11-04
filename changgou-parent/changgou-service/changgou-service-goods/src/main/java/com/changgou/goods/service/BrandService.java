package com.changgou.goods.service;

import com.changgou.entity.Page;
import com.changgou.goods.pojo.Brand;

import java.util.List;
import java.util.Map;

/**
 * @author Joan
 * @date 2019-11-01 21:51
 */
public interface BrandService {

    /**
     * 查询所有品牌
     *
     * @return
     */
    public List<Brand> findAll();

    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    public Brand findById(Integer id);

    /**
     * 新增品牌
     * @param brand
     */
    public void addBrand(Brand brand);

    /**
     * 修改品牌
     * @param brand
     */
    public void updateBrand(Brand brand);

    /**
     * 通过id删除品牌
     * @param id
     */
    public void deleteBrandById(Integer id);

    /**
     * 多条件查询品牌
     * @param searchMap
     * @return
     */
    public List<Brand> searchBrandListByMap(Map<String,Object> searchMap);

    /**
     * 分页
     * @param page
     * @param size
     * @return
     */
    public Page<Brand> findPage(Integer page,Integer size);

    /**
     * 多条件查询品牌并分页
     * @param page
     * @param size
     * @return
     */
    public Page<Brand> findPageByMap(Map<String,Object> searchMap,Integer page,Integer size);

    /**
     * 根据商品分类名称查询品牌
     * @param categoryName
     * @return
     */
    public List<Map> findBrandListByCategoryName(String categoryName);



}
