package event;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 事件执行器
 * @author renxz
 * @date 2022/11/30 4:52 下午
 */
@Component
public class EventBus implements IEventBus, ApplicationEventPublisherAware {

    private static final Logger log= LoggerFactory.getLogger(EventBus.class);

    private static ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        eventPublisher=applicationEventPublisher;
    }
    @Async
    @Override
    public <T extends BaseEvent> void publish(T event) {
        if (event==null) {return;}
        log.info("EventBus ｜ event:{} publish success:{}", event.getDescription(),JSON.toJSONString(event.getSource()));
        eventPublisher.publishEvent(event);
    }

}