package tool;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * feign 调用获取data
 *
 * @author renxz
 * @date 2022/11/4 5:19 下午
 */
public class ClientTools {

    /**
     * 获取feign 请求单个data
     *
     * @param supplier
     * @param <T>
     * @return
     */
    public static <T> T execute(Supplier<SingleResponse<T>> supplier) {
        SingleResponse<T> response = supplier.get();
        if (response != null) {
            if (Objects.equals(BusinessStatusCode.SUCCESS.getCode(), response.getCode())) {
                return response.getData();
            }
        }
        return null;
    }

    /**
     * 获取feign 多个data
     *
     * @param supplier
     * @param <T>
     * @return
     */
    public static <T> Collection<T> executeMulti(Supplier<MultiResponse<T>> supplier) {
        MultiResponse<T> response = supplier.get();
        if (response != null) {
            if (Objects.equals(BusinessStatusCode.SUCCESS.getCode(), response.getCode())) {
                return response.getData();
            }
        }
        return null;
    }
}
