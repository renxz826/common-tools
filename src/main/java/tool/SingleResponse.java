package tool;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author renxunzhen
 * @description 单个实体响应
 * @date 2022/11/3 10:01 上午
 */
@Accessors(chain = true)
@Data
public class SingleResponse<T> extends Response {

    private Integer code;

    private String msg;

    private T data;

    /**
     * 构造
     *
     * @return
     */
    public static <T> SingleResponse<T> of() {
        SingleResponse<T> response = new SingleResponse<>();
        return response;
    }

    /**
     * @param guid 链路id
     * @param <T>
     * @return
     */
    public static <T> SingleResponse<T> of(String guid) {
        SingleResponse<T> response = new SingleResponse<>();
        response.setGuid(guid);
        return response;
    }

    /**
     * 响应失败
     *
     * @param code
     * @param msg
     * @return
     */
    public static <T> SingleResponse<T> failed(Integer code, String msg) {
        SingleResponse<T> response = new SingleResponse<>();
        Map<String, Object> map = Maps.newHashMap();
        response.setCode(code);
        response.setMsg(msg);
        response.setData((T) map);
        return response;
    }

    /**
     * 响应失败
     *
     * @param code
     * @param msg
     * @param guid
     * @return
     */
    public static <T> SingleResponse<T> failed(Integer code, String msg, String guid) {
        SingleResponse<T> response = new SingleResponse<>();
        Map<String, Object> map = Maps.newHashMap();
        response.setCode(code);
        response.setMsg(msg);
        response.setGuid(guid);
        response.setData((T) map);
        return response;
    }

    /**
     * 响应成功
     *
     * @param data
     * @return
     */
    public static <T> SingleResponse<T> success(T data) {
        SingleResponse<T> response = new SingleResponse<>();
        response.setCode(200);
        response.setMsg("SUCCESS");
        if (null != data) {
            response.setData(data);
        } else {
            Map<String, Object> map = Maps.newHashMap();
            response.setData((T) map);
        }
        return response;
    }

    /**
     * 响应成功
     *
     * @param data
     * @return
     */
    public static <T> SingleResponse<T> success(T data, String guid) {
        SingleResponse<T> response = new SingleResponse<>();
        response.setCode(200);
        response.setMsg("SUCCESS");
        if (null != data) {
            response.setData(data);
        } else {
            Map<String, Object> map = Maps.newHashMap();
            response.setData((T) map);
        }
        response.setGuid(guid);
        return response;
    }
}
