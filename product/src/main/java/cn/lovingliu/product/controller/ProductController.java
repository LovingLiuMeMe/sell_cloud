package cn.lovingliu.product.controller;

import cn.lovingliu.product.common.ServerResponse;
import cn.lovingliu.product.convert.ProductInfoToProductInfoVO;
import cn.lovingliu.product.dataobject.ProductCategory;
import cn.lovingliu.product.dataobject.ProductInfo;
import cn.lovingliu.product.enums.CommonStatusEnum;
import cn.lovingliu.product.service.ProductCategoryService;
import cn.lovingliu.product.service.ProductService;
import cn.lovingliu.product.vo.ProductCategoryVO;
import cn.lovingliu.product.vo.ProductInfoVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：LovingLiu
 * @Description: 商品服务的controller
 * @Date：Created in 2019-10-11
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/list")
    public ServerResponse<List<ProductCategoryVO>> list(@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                                                        @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                                        @RequestParam(value = "sortBy",defaultValue = "productPrice")String sortBy,
                                                        @RequestParam(value = "sortLift",defaultValue = "desc")String sortLift){

        // 1.更具条件查询所有的商品
        Sort sort = new Sort(sortLift.equals("desc")? Sort.Direction.DESC : Sort.Direction.ASC,sortBy);

        PageRequest pageRequest = PageRequest.of(pageNum-1,pageSize,sort);
        Page<ProductInfo> page = productService.findUpAll(CommonStatusEnum.UP.getCode(),pageRequest);
        List<ProductInfo> productInfoList = page.getContent();
        // 2.查询出上述商品中的分类(一次查询:只进行一次查询 即可拿出所有信息)
        // java8 lambda表达式
        List<Integer> categorytypeList = productInfoList
                .stream()
                .map(e -> e.getCategoryType())
                .collect(Collectors.toList());

        List<ProductCategory> productCategoryList = productCategoryService.findByCategoryTypeInIdList(categorytypeList);
        // 3.数据封装
        List<ProductCategoryVO> productCategoryVOList = Lists.newArrayList();
        for(ProductCategory productCategory:productCategoryList){
            ProductCategoryVO productCategoryVO = new ProductCategoryVO();
            productCategoryVO.setCategoryName(productCategory.getCategoryName());
            productCategoryVO.setCategoryType(productCategory.getCategoryType());

            List<ProductInfoVO> productInfoVOList = ProductInfoToProductInfoVO.convert(productInfoList);

            productCategoryVO.setProductInfoVOList(productInfoVOList);
            productCategoryVOList.add(productCategoryVO);
        }
        return ServerResponse.createBySuccess("成功",productCategoryVOList);
    }
}
