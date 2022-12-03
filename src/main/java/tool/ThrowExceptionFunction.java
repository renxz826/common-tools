package tool;


/**
 * 异常处理 function 函数
 *
 * @author renxunzhen
 */
@FunctionalInterface
public interface ThrowExceptionFunction {
    /**
     * 异常处理
     *
     * @param message
     */
    void throwException(String message);
}
