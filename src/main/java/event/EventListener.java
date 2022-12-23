package event;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import tool.ApplicationContextHelper;

import java.lang.reflect.Method;

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
    public void onApplicationEvent(BaseEvent event) {
        log.info("EventListener | 监听事件 Event:{}", JSON.toJSONString(event));
        // 获取事件订阅者
        EventSubscriberCache<Class<?>, Method> cache = EventSubscriberCache.init();
        cache.entrySet().forEach( entry ->{
            Method method = entry.getValue();
            Class<?> aClass = entry.getKey();
            Object bean = ApplicationContextHelper.getBean(aClass);
            if (bean==null) {
                return;
            }
            // invoke
            ReflectionUtils.invokeMethod(method,bean,event);
        });

    }


}
