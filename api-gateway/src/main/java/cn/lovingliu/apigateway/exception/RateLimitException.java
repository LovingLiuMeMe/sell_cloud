package cn.lovingliu.apigateway.exception;

/**
 * @Author：LovingLiu
 * @Description: 获取令牌的异常
 * @Date：Created in 2019-10-19
 */
public class RateLimitException extends RuntimeException {
    public RateLimitException(String message) {
        super(message);
    }
}
