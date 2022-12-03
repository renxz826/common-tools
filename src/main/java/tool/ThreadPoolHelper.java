package tool;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author renxz
 * @description 线程池
 * @date 2022/12/2 4:05 下午
 */
public class ThreadPoolHelper {

    private static volatile Executor executor;

    private ThreadPoolHelper() {

    }

    public static Executor initPool() {
        if (executor == null) {
            synchronized (ThreadPoolHelper.class) {
                if (executor == null) {
                    executor = new ThreadPoolExecutor(10, 20, 10, TimeUnit.SECONDS, new LinkedBlockingDeque(1000), new ThreadPoolExecutor.AbortPolicy());
                }
            }
        }
        return executor;
    }

    public static Executor initPool(int corePoolSize, int maximumPoolSize) {
        if (executor == null) {
            synchronized (ThreadPoolHelper.class) {
                if (executor == null) {
                    executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 10, TimeUnit.SECONDS, new LinkedBlockingDeque(1000), new ThreadPoolExecutor.AbortPolicy());
                }
            }
        }
        return executor;
    }


}
