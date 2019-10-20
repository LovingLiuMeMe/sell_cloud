package cn.lovingliu.apigateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author：LovingLiu
 * @Description: 网关统一异常处理
 * @Date：Created in 2019-10-19
 */
@RestController
@Slf4j
public class ExceptionController  implements ErrorController {
    private static final String GET_STRING_TEMPLATE = "GetRequestError【%s】";
    private static final String POST_STRING_TEMPLATE = "PostRequestError【%s】";

    @Value("${error.path}")
    private String errorPath;

    @GetMapping(value = "${error.path}")
    public String getError(HttpServletRequest request) {
        Exception e = (Exception) request.getAttribute("javax.servlet.error.exception");
        String message = e.getCause().getMessage();
        log.error(String.format(GET_STRING_TEMPLATE,message));
        return String.format(GET_STRING_TEMPLATE,message);
    }
    @PostMapping(value = "${error.path}")
    public String postError(HttpServletRequest request){
        Exception e = (Exception) request.getAttribute("javax.servlet.error.exception");
        String message = e.getCause().getMessage();
        log.error(String.format(POST_STRING_TEMPLATE,message));
        return String.format(POST_STRING_TEMPLATE,message);
    }

    @Override
    public String getErrorPath() {
        return errorPath;
    }
}
