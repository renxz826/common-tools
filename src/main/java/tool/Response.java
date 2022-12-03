package tool;

import java.io.Serializable;

/**
 * @author renxz
 * @date 2022/11/3 10:23 上午
 */
public abstract class Response implements Serializable {
    /**
     * 链路标识
     */
    private String guid;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
