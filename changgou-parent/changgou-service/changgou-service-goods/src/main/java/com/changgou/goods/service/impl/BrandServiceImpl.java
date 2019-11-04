package com.changgou.goods.service.impl;

import com.changgou.entity.Page;
import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * 品牌实现类
 *
 * @author Joan
 * @date 2019-11-01 21:52
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }

    /**
     * 根据id查询商品
     *
     * @param id
     * @return
     */
    @Override
    public Brand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增品牌
     *
     * @param brand
     */
    @Override
    public void addBrand(Brand brand) {
        brandMapper.insertSelective(brand);
    }

    /**
     * 修改品牌
     *
     * @param brand
     */
    @Override
    public void updateBrand(Brand brand) {
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    /**
     * 通过id删除品牌
     *
     * @param id
     */
    @Override
    public void deleteBrandById(Integer id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    /**
     * 多条件查询品牌
     *
     * @param searchMap
     * @return
     */
    @Override
    public List<Brand> searchBrandListByMap(Map<String, Object> searchMap) {
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchMap == null) {
            //品牌名称
            if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                criteria.andLike("name", "%" + searchMap.get("name") + "%");
            }
            //品牌首字母
            if (searchMap.get("letter") != null && !"".equals(searchMap.get("letter"))) {
                criteria.andEqualTo("letter", searchMap.get("letter"));
            }
        }
        return brandMapper.selectByExample(example);

    }

    /**
     * 分页
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Brand> findPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        return (Page<Brand>) brandMapper.selectAll();
    }

    /**
     * 多条件查询品牌并且分页
     *
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Brand> findPageByMap(Map<String, Object> searchMap, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchMap == null) {
            //品牌名称
            if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                criteria.andLike("name", "%" + searchMap.get("name") + "%");
            }
            //品牌首字母
            if (searchMap.get("letter") != null && !"".equals(searchMap.get("letter"))) {
                criteria.andEqualTo("letter", searchMap.get("letter"));
            }
        }
        return (Page<Brand>) brandMapper.selectByExample(example);
    }



    /**
     * 分局商品分类信息查询商品名称
     *
     * @param categoryName
     * @return
     */
    @Override
    public List<Map> findBrandListByCategoryName(String categoryName) {
        List<Map> listByCategoryName = brandMapper.findBrandListByCategoryName(categoryName);
        return listByCategoryName;
    }


}
