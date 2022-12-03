package tool;

/**
 * if 条件 function
 *
 * @author renxz
 * @date 2022/11/4 2:31 下午
 */
@FunctionalInterface
public interface IfCondition {
    /**
     * if function
     *
     * @param yes 条件为真
     * @param no  条件为否
     */
    void then(Runnable yes, Runnable no);

}
