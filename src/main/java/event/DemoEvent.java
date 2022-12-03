package event;

/**
 * demo event
 * @author renxz
 */
public class DemoEvent extends BaseEvent{

    public DemoEvent(Object source) {
        super(source);
    }

    public DemoEvent(String description, Object source) {
        super(description, source);
    }
}
