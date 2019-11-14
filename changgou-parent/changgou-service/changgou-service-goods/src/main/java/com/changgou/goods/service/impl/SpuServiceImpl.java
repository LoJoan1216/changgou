package com.changgou.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.dao.*;
import com.changgou.goods.pojo.*;
import com.changgou.goods.service.SpuService;
import com.changgou.util.IdWorker;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private CategoryBrandMapper categoryBrandMapper;


    /**
     * 查询全部列表
     *
     * @return
     */
    @Override
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    @Override
    public Spu findById(String id) {
        return spuMapper.selectByPrimaryKey(id);
    }


    /**
     * 增加商品
     *
     * @param goods
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(Goods goods) {
        //1.添加spu
        Spu spu = goods.getSpu();
        //2.添加sku集合
        long l = idWorker.nextId();
        spu.setId(String.valueOf(l));
        //3.设置删除状态
        spu.setIsDelete("0");
        //上架状态
        spu.setIsMarketable("0");
        //审核状态
        spu.setStatus("0");

        saveSkuList(goods);
        spuMapper.insertSelective(spu);


    }

    /**
     * 添加sku数据
     *
     * @param goods
     */
    public void saveSkuList(Goods goods) {
        //生成表示id
        long l = idWorker.nextId();
        //获取sku
        List<Sku> skuList = goods.getSkuList();
        //获取spu
        Spu spu = goods.getSpu();
        //查询分类对象
        Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());
        //查询品牌
        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
        /**
         * 添加分类与品牌之间的关联
         */
        CategoryBrand categoryBrand = new CategoryBrand();
        categoryBrand.setBrandId(spu.getBrandId());
        categoryBrand.setCategoryId(spu.getCategory3Id());
        int i = categoryBrandMapper.selectCount(categoryBrand);
        if (i == 0) {
            //说明没有关系添加关系
            categoryBrandMapper.insert(categoryBrand);
        }
        if (skuList != null) {
            for (Sku sku : skuList) {
                sku.setId(String.valueOf(l));
                if (StringUtils.isEmpty(sku.getSpec())) {
                    sku.setSpec("{}");
                }
                String spuName = spu.getName();
                //将规格json字符串转换成map集合
                Map<String, String> map = JSON.parseObject(sku.getSpec(), Map.class);
                if (map != null && map.size() > 0) {
                    for (String value : map.values()) {
                        spuName += " " + value;
                    }

                }
                //设置名称
                sku.setName(spuName);
                //设置id
                sku.setSpuId(spu.getId());
                //设置添加日期
                sku.setCreateTime(new Date());
                //设置修改日期
                sku.setUpdateTime(new Date());
                //设置商品分类id
                sku.setCategoryId(category.getId());
                //设置商品分类名称
                sku.setCategoryName(category.getName());
                //设置品牌名字
                sku.setBrandName(brand.getName());
                //插入sku数据
                skuMapper.insertSelective(sku);

            }
        }

    }

    /**
     * 修改
     *
     * @param spu
     */
    @Override
    public void updateSpu(Spu spu) {
        spuMapper.updateByPrimaryKey(spu);
    }

    @Transactional
    @Override
    public void updateGoods(Goods goods) {
        Spu spu = goods.getSpu();
        spuMapper.updateByPrimaryKey(spu);
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId", spu.getId());
        skuMapper.deleteByPrimaryKey(example);
        saveSkuList(goods);

    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (!spu.getIsMarketable().equals("0")) {
            throw new RuntimeException("必须先下架再删除");
        }
        spu.setIsDelete("1");
        spu.setStatus("0");
        spuMapper.updateByPrimaryKeySelective(spu);

    }


    /**
     * 条件查询
     *
     * @param searchMap
     * @return
     */
    @Override
    public List<Spu> findSpuList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return spuMapper.selectByExample(example);
    }

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Spu> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        return (Page<Spu>) spuMapper.selectAll();
    }

    /**
     * 条件+分页查询
     *
     * @param searchMap 查询条件
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @Override
    public Page<Spu> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = createExample(searchMap);
        return (Page<Spu>) spuMapper.selectByExample(example);
    }

    /**
     * 恢复商品
     *
     * @param id
     */
    @Override
    public void restore(String id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu == null) {
            throw new RuntimeException("当前商品不存在");
        }
        if (!spu.getIsDelete().equals("1")) {
            throw new RuntimeException("商品未删除");
        }
        spu.setStatus("0");
        spu.setIsDelete("0");
        spuMapper.updateByPrimaryKeySelective(spu);

    }

    /**
     * 物理删除商品
     *
     * @param id
     */
    @Override
    public void realDelete(String id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu == null) {
            throw new RuntimeException("当前商品不存在");
        }
        if (!spu.getIsDelete().equals("1")) {
            throw new RuntimeException("商品未删除");
        }
        spuMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据id上架商品
     *
     * @param id
     */
    @Transactional
    @Override
    public void put(String id) {

        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu == null) {
            throw new RuntimeException("当前商品不存在");
        }
        if (spu.getIsDelete().equals("1")) {
            throw new RuntimeException("当前商品已删除");
        }
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKey(spu);
    }

    /**
     * 根据id下架商品
     *
     * @param id
     */
    @Transactional
    @Override
    public void pull(String id) {
        //获取当前spu对象
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu == null) {
            throw new RuntimeException("当前商品不存在");
        }
        if (spu.getIsDelete().equals("1")) {
            throw new RuntimeException("当前商品已删除");
        }
        spu.setIsMarketable("0");
        spuMapper.updateByPrimaryKey(spu);
    }

    /**
     * 商品审核及上架
     *
     * @param id
     */
    @Transactional
    @Override
    public void audit(String id) {
        //通过id获取当前spu对象
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu == null) {
            throw new RuntimeException("当前商品不存在");
        }
        String isDelete = spu.getIsDelete();
        if (isDelete.equals(1)) {
            throw new RuntimeException("当前商品处于删除状态");

        }
        spu.setIsMarketable("1");
        spu.setStatus("1");
        spuMapper.updateByPrimaryKey(spu);


    }

    /**
     * 根据id查询商品
     *
     * @param id
     * @return
     */
    @Override
    public Goods findGoodsById(String id) {
        //查询spu列表
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //查询sku
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId", id);
        List<Sku> skus = skuMapper.selectByExample(example);
        Goods goods = new Goods();
        goods.setSpu(spu);
        goods.setSkuList(skus);
        return goods;


    }

    /**
     * 构建查询对象
     *
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchMap != null) {
            // 主键
            if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                criteria.andEqualTo("id", searchMap.get("id"));
            }
            // 货号
            if (searchMap.get("sn") != null && !"".equals(searchMap.get("sn"))) {
                criteria.andEqualTo("sn", searchMap.get("sn"));
            }
            // SPU名
            if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                criteria.andLike("name", "%" + searchMap.get("name") + "%");
            }
            // 副标题
            if (searchMap.get("caption") != null && !"".equals(searchMap.get("caption"))) {
                criteria.andLike("caption", "%" + searchMap.get("caption") + "%");
            }
            // 图片
            if (searchMap.get("image") != null && !"".equals(searchMap.get("image"))) {
                criteria.andLike("image", "%" + searchMap.get("image") + "%");
            }
            // 图片列表
            if (searchMap.get("images") != null && !"".equals(searchMap.get("images"))) {
                criteria.andLike("images", "%" + searchMap.get("images") + "%");
            }
            // 售后服务
            if (searchMap.get("saleService") != null && !"".equals(searchMap.get("saleService"))) {
                criteria.andLike("saleService", "%" + searchMap.get("saleService") + "%");
            }
            // 介绍
            if (searchMap.get("introduction") != null && !"".equals(searchMap.get("introduction"))) {
                criteria.andLike("introduction", "%" + searchMap.get("introduction") + "%");
            }
            // 规格列表
            if (searchMap.get("specItems") != null && !"".equals(searchMap.get("specItems"))) {
                criteria.andLike("specItems", "%" + searchMap.get("specItems") + "%");
            }
            // 参数列表
            if (searchMap.get("paraItems") != null && !"".equals(searchMap.get("paraItems"))) {
                criteria.andLike("paraItems", "%" + searchMap.get("paraItems") + "%");
            }
            // 是否上架
            if (searchMap.get("isMarketable") != null && !"".equals(searchMap.get("isMarketable"))) {
                criteria.andEqualTo("isMarketable", searchMap.get("isMarketable"));
            }
            // 是否启用规格
            if (searchMap.get("isEnableSpec") != null && !"".equals(searchMap.get("isEnableSpec"))) {
                criteria.andEqualTo("isEnableSpec", searchMap.get("isEnableSpec"));
            }
            // 是否删除
            if (searchMap.get("isDelete") != null && !"".equals(searchMap.get("isDelete"))) {
                criteria.andEqualTo("isDelete", searchMap.get("isDelete"));
            }
            // 审核状态
            if (searchMap.get("status") != null && !"".equals(searchMap.get("status"))) {
                criteria.andEqualTo("status", searchMap.get("status"));
            }

            // 品牌ID
            if (searchMap.get("brandId") != null) {
                criteria.andEqualTo("brandId", searchMap.get("brandId"));
            }
            // 一级分类
            if (searchMap.get("category1Id") != null) {
                criteria.andEqualTo("category1Id", searchMap.get("category1Id"));
            }
            // 二级分类
            if (searchMap.get("category2Id") != null) {
                criteria.andEqualTo("category2Id", searchMap.get("category2Id"));
            }
            // 三级分类
            if (searchMap.get("category3Id") != null) {
                criteria.andEqualTo("category3Id", searchMap.get("category3Id"));
            }
            // 模板ID
            if (searchMap.get("templateId") != null) {
                criteria.andEqualTo("templateId", searchMap.get("templateId"));
            }
            // 运费模板id
            if (searchMap.get("freightId") != null) {
                criteria.andEqualTo("freightId", searchMap.get("freightId"));
            }
            // 销量
            if (searchMap.get("saleNum") != null) {
                criteria.andEqualTo("saleNum", searchMap.get("saleNum"));
            }
            // 评论数
            if (searchMap.get("commentNum") != null) {
                criteria.andEqualTo("commentNum", searchMap.get("commentNum"));
            }

        }
        return example;
    }

}
