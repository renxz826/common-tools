package event;

import org.springframework.context.ApplicationEvent;

/**
 * 订阅事件基类
 * @author renxz
 * @date 2022/11/30 4:47 下午
 */
public abstract class BaseEvent extends ApplicationEvent {
    /**
     * 事件描述
     */
    private String description;
    /**
     * 事件数据源
     */
    private Object source;

    public BaseEvent(Object source) {
        super(source);
    }
    public BaseEvent(String description,Object source){
        super(source);
        this.description=description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}

