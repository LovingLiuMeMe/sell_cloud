package cn.lovingliu.apigateway.filter;

import cn.lovingliu.apigateway.exception.RateLimitException;
import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @Author：LovingLiu
 * @Description: 限流（一般用于注册的时候）
 * @Date：Created in 2019-10-19
 */
@Component
public class RateLimitFilter extends ZuulFilter {
    /**
     * @Desc 创建令牌桶 guava已实现(1秒投放一次)
     * @Author LovingLiu
    */
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(100);

    @Override
    public String filterType() {
        /**
         * @Desc
         * @Author LovingLiu
        */
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        /**
         * 优先级应该高于 所有框架自带的Filter
        */
        return FilterConstants.SERVLET_DETECTION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        if (!RATE_LIMITER.tryAcquire(1)){ // 取令牌
            // 未取到
            throw new RateLimitException("当前获取令牌的人数过多");
        }
        return null;
    }
}
