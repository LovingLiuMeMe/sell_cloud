package cn.lovingliu.apigateway.filter.auth;

import cn.lovingliu.apigateway.constants.CookieConstant;
import cn.lovingliu.apigateway.constants.RedisConstant;
import cn.lovingliu.apigateway.util.CookieUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author：LovingLiu
 * @Description: 权限拦截 （区分卖家和买家）
 * @Date：Created in 2019-10-18
 */
@Component
@Slf4j
public class AuthSellerFilter extends ZuulFilter {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }
    @Override
    public int filterOrder() {
        return FilterConstants.SERVLET_DETECTION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        if(request.getRequestURI().equals("/myOrder/order/finish")){
            return true;
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        /**
         * /order/finish 只能卖家访问 （特征: cookie中有cookie,对应的redis中有值）
        */
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if(cookie == null
                || StringUtils.isBlank(cookie.getValue())
                || StringUtils.isBlank(redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue())))){
            throw new RuntimeException("无权限访问");
        }
        return null;
    }

}
