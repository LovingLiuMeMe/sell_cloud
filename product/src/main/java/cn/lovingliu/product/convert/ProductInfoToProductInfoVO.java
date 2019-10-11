package cn.lovingliu.product.convert;

import cn.lovingliu.product.dataobject.ProductInfo;
import cn.lovingliu.product.enums.CommonStatusEnum;
import cn.lovingliu.product.util.CodeEnumUtil;
import cn.lovingliu.product.util.DateTimeUtil;
import cn.lovingliu.product.vo.ProductInfoVO;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description: info->vo
 * @Date：Created in 2019-10-11
 */
public class ProductInfoToProductInfoVO {
    public static ProductInfoVO convert(ProductInfo productInfo){
        ProductInfoVO productInfoVO = new ProductInfoVO();
        BeanUtils.copyProperties(productInfo, productInfoVO);


        productInfoVO.setCreateTime(DateTimeUtil.dateToStr(productInfo.getCreateTime()));
        productInfoVO.setUpdateTime(DateTimeUtil.dateToStr(productInfo.getUpdateTime()));
        productInfoVO.setProductStatusMessage(CodeEnumUtil.getByCode(productInfo.getProductStatus(), CommonStatusEnum.class).getMsg());

        return productInfoVO;
    }
    public static List<ProductInfoVO> convert(List<ProductInfo> productInfoList){
        List<ProductInfoVO> list = Lists.newArrayList();
        for(ProductInfo productInfo: productInfoList){
            list.add(convert(productInfo));
        }
        return list;
    }
}
