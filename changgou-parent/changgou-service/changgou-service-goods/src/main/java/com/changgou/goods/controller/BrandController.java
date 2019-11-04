package com.changgou.goods.controller;

import com.changgou.entity.Page;
import com.changgou.entity.PageResult;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 品牌controller
 *
 * @author Joan
 * @date 2019-11-01 21:54
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 查询所有品牌
     *
     * @return
     */
    @GetMapping(value = "/findAll")
    public Result findAll() {
        List<Brand> brandList = brandService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", brandList);
    }

    /**
     * 根据id查询品牌
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public Result findById(@PathVariable Integer id) {
        Brand brand = brandService.findById(id);
        return new Result(true, StatusCode.OK, "查询成功", brand);
    }

    /**
     * 添加品牌
     *
     * @param brand
     * @return
     */
    @PostMapping
    public Result addBrand(@RequestBody Brand brand) {
        brandService.addBrand(brand);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /**
     * 修改品牌
     *
     * @param brand
     * @param id
     * @return
     */
    @PostMapping(value = "/{id}")
    public Result updateBrand(@RequestParam Brand brand, @PathVariable Integer id) {
        brand.setId(id);
        brandService.updateBrand(brand);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除品牌
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result deleteBrandById(@PathVariable Integer id) {
        brandService.deleteBrandById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 多条件查询品牌
     *
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search")
    public Result searchBrandListByMap(@PathVariable Map searchMap) {
        List<Brand> list = brandService.searchBrandListByMap(searchMap);
        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    /**
     * 查询并分页
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}")
    public Result findPage(@PathVariable Integer page, @PathVariable Integer size) {
        Page<Brand> pageList = brandService.findPage(page, size);
        PageResult pageResult = new PageResult(pageList.getTotal(), pageList.getList());
        return new Result(true, StatusCode.OK, "分页成功", pageResult);
    }

    /**
     * 多条件查询并分页
     *
     * @param page
     * @param size
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/searchMap/{page}/{size}")
    public Result findPageByMap(@PathVariable Integer page, @PathVariable Integer size, @RequestParam Map searchMap) {

        Page<Brand> pageByMap = brandService.findPageByMap(searchMap, page, size);
        PageResult pageResult = new PageResult(pageByMap.getTotal(), pageByMap.getList());
        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }

    /**
     * 根据商品分类名称查询品牌名称
     *
     * @param categoryName
     * @return
     */
    @GetMapping(value = "/category/{categoryName}")
    public Result findBrandListByCategoryName(@PathVariable String categoryName) {
        List<Map> listByCategoryName = brandService.findBrandListByCategoryName(categoryName);
        return new Result(true, StatusCode.OK, "查询成功", listByCategoryName);
    }



}
