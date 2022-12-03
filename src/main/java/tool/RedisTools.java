package tool;

import cn.huahanedu.beeeeego.data.sourcefile.constant.RedisConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * redis 工具类
 * @author renxz
 * @date 2022/11/3 4:24 下午
 */
@Component
public class RedisTools {

    private static final String LOCK_PREFIX = RedisConstant.LOCK;
    @Resource
    private RedisTemplate redisTemplate;

    public boolean lock() {
        // TODO ...
       // redisTemplate.opsForValue().setIfAbsent()
        return false;
    }


}
