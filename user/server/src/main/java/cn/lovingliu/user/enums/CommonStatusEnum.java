package cn.lovingliu.user.enums;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
public enum CommonStatusEnum implements CodeEnum {
    SUCCESS(200, "成功"),
    ERROR(400, "失败"),
    LOGIN_FAIL(600,"登录失败"),
    BUYER(0,"买家"),
    SELLER(1,"卖家"),
    ROLE_ERROR(601,"角色权限限制");

    private final Integer code;
    private final String msg;

    CommonStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
