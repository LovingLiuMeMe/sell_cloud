package cn.lovingliu.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @Author：LovingLiu
 * @Description: zuul 前置过滤器
 * @Date：Created in 2019-10-18
 */
@Component
@Slf4j
public class TokenFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
//        RequestContext requestContext = RequestContext.getCurrentContext();
//        HttpServletRequest request = requestContext.getRequest();
//        // 这里是从URL参数里获取,也可以从cookie和header中获取
//        String token = request.getParameter("token");
//        if(StringUtils.isBlank(token)){
//            log.error("验证未通过,token参数缺失");
//            // 统一异常处理
//            throw new RuntimeException("验证未通过,token参数缺失");
//            //requestContext.setSendZuulResponse(false);
//            //requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());// 401
//        }
        return null;
    }

}
