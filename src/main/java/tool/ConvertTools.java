package tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * @author renxz
 * @description pojo 对象转化工具类
 * @date 2022/11/3 11:13 上午
 */
public class ConvertTools {

    public static final Logger log = LoggerFactory.getLogger(ConvertTools.class);

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper(JsonFactory.builder().build());
        // 忽略大小写
        objectMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        // 忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        // 支持1.8 时间序列化
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        // 序列化不包括 null值属性
        //  objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    }

    @SneakyThrows
    public static <S, T> T convertProperties(S source, Class<T> target) {
        Assert.notNull(source, "ConvertTools ｜ convertProperties ｜ source cannot be null !");
        Assert.notNull(target, "ConvertTools ｜ convertProperties ｜ target cannot be null !");
        String sourceJsonStr = objectMapper.writeValueAsString(source);
        if (StringUtils.isNotBlank(sourceJsonStr)) {
            return objectMapper.readValue(sourceJsonStr, target);
        }
        return null;
    }

    public static <S, T> List<T> convertPropertiesList(List<S> sources, Class<T> target) {
        Assert.notNull(sources, "ConvertTools ｜ convertProperties ｜ source cannot be null !");
        Assert.notNull(target, "ConvertTools ｜ convertProperties ｜ target cannot be null !");
        LinkedList<T> list = Lists.newLinkedList();
        sources.forEach(source -> {
            String sourceJsonStr = null;
            try {
                sourceJsonStr = objectMapper.writeValueAsString(source);
                if (StringUtils.isNotBlank(sourceJsonStr)) {
                    list.add(objectMapper.readValue(sourceJsonStr, target));
                }
            } catch (JsonProcessingException e) {
                log.error("Method | convertPropertiesList |error :{}", e.getMessage());
            }

        });
        return list;
    }

    public static <S, T> T convertProperties(S source, Function<S, T> function) {
        Assert.notNull(source, "ConvertTools ｜ convertProperties ｜ source cannot be null !");
        return function.apply(source);
    }

    public static <S, T> T map(S source, Class<T> target) {
        Assert.notNull(source, "ConvertTools ｜ convertProperties ｜ source cannot be null !");
        return BeanUtils.mapToBean((JSONObject) JSON.toJSON(source), target);
    }
}
