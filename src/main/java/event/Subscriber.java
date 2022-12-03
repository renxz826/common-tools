package event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 事件订阅注解 由此注解标识为事件订阅者
 * @author renxz
 * @date 2022/12/1 9:28 上午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface Subscriber {

     /**
      * value 值为事件类型,事件需要继承BaseEvent value需指定事件类型不能为null否则不生效
      * @return
      */
     Class<? extends BaseEvent> value() ;

}
