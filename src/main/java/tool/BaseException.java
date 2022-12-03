package tool;

/**
 * @author renxz
 * @description 业务异常 封装类
 * @date 2022/11/3 4:27 下午
 */
public class BaseException extends RuntimeException {

    private Integer code;
    private String msg;

    public BaseException() {
        super();
    }

    public BaseException(Throwable throwable) {
        super(throwable);
        this.code = BusinessStatusCode.SYSTEM.code;
        this.msg = throwable.getMessage();

    }

    public BaseException(String msg, Throwable throwable) {
        super(msg, throwable);
        this.msg = msg;
        this.code = BusinessStatusCode.SYSTEM.code;
    }

    public BaseException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
