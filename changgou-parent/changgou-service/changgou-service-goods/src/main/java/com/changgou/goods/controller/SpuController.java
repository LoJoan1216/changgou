package com.changgou.goods.controller;

import com.changgou.entity.PageResult;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Spu;
import com.changgou.goods.service.SpuService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/spu")
public class SpuController {


    @Autowired
    private SpuService spuService;

    /**
     * 查询全部数据
     *
     * @return
     */
    @GetMapping
    public Result findAll() {
        List<Spu> spuList = spuService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", spuList);
    }


    /**
     * 根据id查询商品
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result findGoodsById(@PathVariable String id) {
        Goods goods = spuService.findGoodsById(id);
        return new Result(true, StatusCode.OK, "查询成功", goods);
    }

    /***
     * 新增数据
     * @param goods
     * @return
     */
    @PostMapping(value = "/add")
    public Result add(@RequestBody Goods goods) {
        spuService.add(goods);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /**
     * 修改数据
     *
     * @param goods
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result updateGoods(@RequestBody Goods goods) {
        spuService.updateGoods(goods);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        spuService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search")
    public Result findSpuList(@RequestParam Map searchMap) {
        List<Spu> list = spuService.findSpuList(searchMap);
        return new Result(true, StatusCode.OK, "查询成功", list);
    }


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}")
    public Result findPage(@RequestParam Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Spu> pageList = spuService.findPage(searchMap, page, size);
        PageResult pageResult = new PageResult(pageList.getTotal(), pageList.getResult());
        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }

    /**
     * 根据商品id审核商品
     *
     * @param id
     * @return
     */
    @PutMapping(value = "/audit/{id}")
    public Result audit(@PathVariable("id") String id) {
        spuService.audit(id);
        return new Result(true, StatusCode.OK, "商品审核成功");
    }


    /**
     * 根据商品id下架商品
     *
     * @param id
     * @return
     */
    @PutMapping(value = "/pull/{id}")
    public Result pull(@PathVariable("id") String id) {
        spuService.pull(id);
        return new Result(true, StatusCode.OK, "商品下架成功");
    }

    /**
     * 根据商品id上架商品
     *
     * @param id
     * @return
     */
    @PutMapping(value = "/put/{id}")
    public Result put(@PathVariable("id") String id) {
        spuService.put(id);
        return new Result(true, StatusCode.OK, "商品上架成功");
    }

    /**
     * 恢复商品
     * @param id
     * @return
     */
    @PutMapping(value = "/restore/{id}")
    public Result restore(@PathVariable("id") String id) {
        spuService.restore(id);
        return new Result(true, StatusCode.OK, "商品恢复成功");
    }

    /**
     * 物理删除商品
     * @param id
     * @return
     */
    @DeleteMapping(value = "/realDelete/{id}")
    public Result realDelete(@PathVariable("id") String id){
        spuService.realDelete(id);
        return new Result(true, StatusCode.OK, "商品删除成功");

    }

}
