package event;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 扫描Subscriber此注解修饰方法的订阅者
 * <p>
 * 注: 此扫描类只会存一个类中一个订阅的方法
 *
 * @param <K> 类名 xxx.xxx.class
 * @param <V> 注解Subscriber修饰的方法 java.lang.reflect.Method
 * @author renxz
 * @date 2022/12/1 2:33 下午
 * @see Subscriber
 */
public class EventSubscriberCache<K, V> extends HashMap<K, V> {

    private static final Logger log = LoggerFactory.getLogger(EventSubscriberCache.class);

    private static final String POSTFIX_CLASSNAME = ".class";

    private static final String PACKAGE_URL = "";

    private static final String CLASS_PATH = Thread.currentThread().getContextClassLoader().getResource(PACKAGE_URL) == null ? "" : Thread.currentThread().getContextClassLoader().getResource(PACKAGE_URL).getPath();

    private EventSubscriberCache() {
    }

    public static EventSubscriberCache<Class<?>, Method> init() {
        return EventSubscriberCacheBuilder.cache;
    }

    public static class EventSubscriberCacheBuilder {
        public static EventSubscriberCache<Class<?>, Method> cache = new EventSubscriberCache<Class<?>, Method>();

        static {
            // 扫描所有类
            LinkedList<Class<?>> cLasses = Lists.newLinkedList();
            List<Class<?>> aClasslist = null;
            if (!StringUtils.isBlank(CLASS_PATH)) {
                try {
                    aClasslist = getAllClass(new File(CLASS_PATH), cLasses);
                } catch (ClassNotFoundException e) {
                    log.error("onApplicationEvent error :{}", e.getMessage());
                }
            }
            if (!CollectionUtils.isEmpty(aClasslist)) {
                aClasslist.forEach(aClass -> {
                    Method[] methodArrary = aClass.getMethods();
                    if (methodArrary.length > 0) {
                        List<Method> methods = List.of(methodArrary);
                        methods.forEach(method -> {
                            if (method.isAnnotationPresent(Subscriber.class)) {
                                Subscriber annotation = method.getAnnotation(Subscriber.class);
                                Class<? extends BaseEvent> annoVal = annotation.value();
                                if (annoVal == null) {
                                    log.error("Method :{} 事件监听注解:{} value is null ！", method.getName(), annotation.getClass());
                                }
                                Class<?>[] parameterTypes = method.getParameterTypes();
                                if (parameterTypes.length == 1 && parameterTypes[0].getName().equals(annoVal == null ? "" : annoVal.getName())) {
                                    cache.put(aClass, method);
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    private static List<Class<?>> getAllClass(File file, List<Class<?>> classes) throws ClassNotFoundException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File subfile : files) {
                getAllClass(subfile, classes);
            }
        }
        if (!file.isDirectory()) {
            if (file.getName().endsWith(POSTFIX_CLASSNAME)) {
                String repClassPath = file.getPath().replace(POSTFIX_CLASSNAME, "")
                        .replace(CLASS_PATH, "")
                        .replace("/", ".");
                classes.add(Class.forName(repClassPath));
            }
        }
        return classes;
    }

}
