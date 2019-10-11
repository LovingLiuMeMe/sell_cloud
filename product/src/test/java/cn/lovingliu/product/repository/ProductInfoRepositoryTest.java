package cn.lovingliu.product.repository;

import cn.lovingliu.product.dataobject.ProductInfo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ProductInfoRepositoryTest {

    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Test
    public void findByProductIdIn() {
        List<String> list = Lists.list("1570451410455390493","test","test1");
        List<ProductInfo> productInfoList = productInfoRepository.findByProductIdIn(list);
        log.info("info=>{}",productInfoList);
        Assert.assertTrue(productInfoList.size()>0);
    }
}