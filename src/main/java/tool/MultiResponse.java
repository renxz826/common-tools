package tool;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * @author renxz
 * @description 多个响应实体
 * @date 2022/11/3 10:40 上午
 */
@Accessors(chain = true)
@Data
public class MultiResponse<T> extends Response {
    private Integer code;

    private String msg;

    private Collection<T> data;

    public MultiResponse() {
    }

    /**
     * 构造
     *
     * @return
     */
    public static <T> MultiResponse<T> of() {
        MultiResponse<T> response = new MultiResponse<>();
        return response;
    }

    /**
     * 构造
     *
     * @return
     */
    public static <T> MultiResponse<T> of(String guid) {
        MultiResponse<T> response = new MultiResponse<>();
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
    public static <T> MultiResponse<T> failed(Integer code, String msg) {
        MultiResponse<T> response = new MultiResponse<>();
        response.setCode(code);
        response.setMsg(msg);
        response.setData(Lists.newArrayList());
        return response;
    }

    public static <T> MultiResponse<T> success(Collection<T> data) {
        MultiResponse<T> response = new MultiResponse<>();
        response.setCode(200);
        response.setMsg("SUCCESS");
        if (CollectionUtils.isEmpty(data)) {
            response.setData(Lists.newArrayList());
        } else {
            response.setData(data);
        }

        return response;
    }

    public static <T> MultiResponse<T> success(Collection<T> data, String guid) {
        MultiResponse<T> response = new MultiResponse<>();
        response.setCode(200);
        response.setGuid(guid);
        response.setMsg("SUCCESS");
        if (CollectionUtils.isEmpty(data)) {
            response.setData(Lists.newArrayList());
        } else {
            response.setData(data);
        }
        return response;
    }
}
