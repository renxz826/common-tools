package event;

/**
 * 事件通知
 *
 * @author renxz
 * @date 2022/11/30 4:42 下午
 */
public interface IEventBus {
    /**
     * 发布事件
     *
     * @param event 事件类型
     * @param <T>
     */
    <T extends BaseEvent> void publish(T event);
}
