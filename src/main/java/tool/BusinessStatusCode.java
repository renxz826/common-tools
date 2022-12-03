package tool;

import org.springframework.util.Assert;

import java.util.Objects;

/**
 * 业务状态码
 *
 * @author renxz
 * @date 2022/11/3 4:37 下午
 */
public enum BusinessStatusCode {
    /**
     * 响应成功
     */
    SUCCESS(200),
    /**
     * 系统类异常 500开头
     */
    SYSTEM(500),
    /**
     * 业务类异常状态码 1000 开头
     */
    BUSINESS_CODE(1000);


    Integer code;

    BusinessStatusCode(Integer code) {
        this.code = code;
    }

    public static BusinessStatusCode of(Integer code) {
        Assert.isNull(code, "BusinessStatusCode of param cannot be null !");
        for (BusinessStatusCode businessStatusCode : values()) {
            if (Objects.equals(businessStatusCode.getCode(), code)) {
                return businessStatusCode;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }


}
