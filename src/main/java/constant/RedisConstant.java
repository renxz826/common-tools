package constant;

/**
 * redis 常量池
 *
 * @author renxunzhen
 * @date 2022/11/4 3:45 下午
 */
public interface RedisConstant {

    String KEY_COMPANY = "beeeeego";

    String KEY_COMMON_SEPARATOR = ":";

    String KEY_PREFIX = "redis";

    String KEY_PROJECT_MODULE = "souecefile";

    String LOCK = KEY_PREFIX + KEY_COMMON_SEPARATOR + KEY_PROJECT_MODULE + KEY_COMMON_SEPARATOR + "lock" + KEY_COMMON_SEPARATOR;

    // ------------------------------------------表名字-------------------------------------------

    String KEY_TABLE_NAME = "bg_file_group_info";


    // ------------------------------------------列名字------------------------------------------


    String id = "id";


}
