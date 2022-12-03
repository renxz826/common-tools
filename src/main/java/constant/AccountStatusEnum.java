package constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 账号状态
 *
 * @author lty
 */
@Getter
public enum AccountStatusEnum {
    /**
     * 已注销
     */
    CANCELED(0),
    /**
     * 已启用
     */
    ENABLED(1),
    /**
     * 已锁定
     */
    LOCKED(2),
    /**
     * 已过期
     */
    EXPIRED(3);

    /**
     * 标记数据库要存的值
     */
    @EnumValue
    private final int value;

    AccountStatusEnum(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
