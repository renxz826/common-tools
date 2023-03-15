package event;

import com.alibaba.fastjson.JSON;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import tool.ApplicationContextHelper;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 事件监听器
 *
 * @author renxz
 * @date 2022/11/30 5:02 下午
 */
@Component
public class EventListener implements ApplicationListener<BaseEvent> {

    private static final Logger log = LoggerFactory.getLogger(EventListener.class);

    @Override
    public void onApplicationEvent(@NotNull BaseEvent event) {
        log.info("EventListener | 监听事件 Event:{}", JSON.toJSONString(event));
        // 获取事件订阅者
        EventSubscriberCache<Class<?>, Method> cache = EventSubscriberCache.init();
        for (Map.Entry<Class<?>, Method> entry : cache.entrySet()) {
            Class<?> aClass = entry.getKey();
            Method method = entry.getValue();
            try {
                Class<?> bean = (Class<?>) ApplicationContextHelper.getBean(aClass);
                // invoke
                ReflectionUtils.invokeMethod(method, bean, event);
            }catch (Exception e){
               log.error(e.getMessage());
            }

        }
    }


}
