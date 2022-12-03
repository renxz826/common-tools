package tool;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * function 函数工具类
 *
 * @author renxz
 * @date 2022/11/4 2:41 下午
 */
public class SelfFunctionHelper {


    private static final Logger log = LoggerFactory.getLogger(SelfFunctionHelper.class);

    /**
     * if function 工具类
     *
     * @param b 为真 执行y.run 为假执行 n.run
     * @return
     */
    public static IfCondition ifs(boolean b) {
        return (y, n) -> {
            if (b) {
                y.run();
            } else {
                n.run();
            }
        };
    }

    /**
     * 判断是否为null
     *
     * @param supplier
     * @param consumer
     * @param <T>
     */
    public static <T> void isNotNull(Supplier<T> supplier, Consumer<T> consumer) {
        T t = supplier.get();
        if (t != null) {
            consumer.accept(t);
        }


    }

    /**
     * 判断是否为null
     *
     * @param t
     * @param consumer
     * @param <T>
     */
    public static <T> void isNotNull(T t, Consumer<T> consumer) {
        if (t != null) {
            consumer.accept(t);
        }
    }

    /**
     * 判断是否为空
     *
     * @param t
     * @param consumer
     * @param <T>
     */
    public static <T> void isNotBlank(T t, Consumer<T> consumer) {
        if (!"".equals(t) && null != t) {
            consumer.accept(t);
        }
    }

    /**
     * 判断是否为空
     *
     * @param supplier
     * @param consumer
     * @param <T>
     */
    public static <T> void isNotBlank(Supplier<T> supplier, Consumer<T> consumer) {
        T t = supplier.get();
        if (!"".equals(t) && null != t) {
            consumer.accept(t);
        }

    }

    /**
     * 条件为假 抛异常
     *
     * @param b
     * @return
     */
    public static ThrowExceptionFunction isFalse(boolean b) {
        return (msg) -> {
            if (!b) {
                log.info("SelfFunctionHelper | isFalse info :{}", msg);
                throw new BaseException(msg);
            }
        };
    }

    /**
     * 参数为null 抛异常
     *
     * @param supplier
     * @return
     */
    public static <T> ThrowExceptionFunction isNull(Supplier<T> supplier) {
        T t = supplier.get();
        return msg -> {
            if (null == t) {
                log.info("SelfFunctionHelper | isNull info :{}", msg);
                throw new BaseException(msg);
            }
        };
    }

    public static <T> ThrowExceptionFunction isNull(T t) {
        return msg -> {
            if (null == t) {
                log.info("SelfFunctionHelper | isNull info :{}", msg);
                throw new BaseException(msg);
            }
        };
    }

    /**
     * 判断参数为空 抛出异常
     *
     * @param t
     * @return
     */
    public static ThrowExceptionFunction isBlank(String t) {
        return msg -> {
            if (StringUtils.isBlank(t)) {
                log.info("SelfFunctionHelper | isBlank info :{}", msg);
                throw new BaseException(msg);
            }
        };
    }

    public static ThrowExceptionFunction isBlank(Supplier<String> t) {
        return msg -> {
            if (StringUtils.isBlank(t.get())) {
                log.info("SelfFunctionHelper | isBlank info :{}", msg);
                throw new BaseException(msg);
            }
        };
    }

}
