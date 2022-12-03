package tool;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author renxz
 * @description 全局异常处理 （未使用 与公共core包冲突）
 * @date 2022/11/3 4:56 下午
 */
@Deprecated
public class GlobalException implements HandlerExceptionResolver {

    private static final Logger log = LoggerFactory.getLogger(GlobalException.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 链路id
        String tradeId = request.getHeader("X-Guid");
        SingleResponse<Object> rsp = new SingleResponse<>();
        if (ex instanceof BaseException) {
            BaseException e = (BaseException) ex;
            rsp.setCode(BusinessStatusCode.SYSTEM.getCode());
            rsp.setMsg(e.getMsg());

            rsp.setGuid(tradeId);
            log.error("BaseException | message is :{}", e.getMsg());
        } else {
            rsp.setCode(BusinessStatusCode.BUSINESS_CODE.getCode());
            rsp.setMsg(ex.getMessage());
            rsp.setGuid(tradeId);
            log.error("{} | message is :{}", ex.getClass().getName(), ex.getMessage());
        }
        try {
            response.getWriter().write(JSON.toJSON(rsp).toString());
        } catch (IOException e) {
            log.error("GlobalException | resolveException error :{}", e.getMessage());
        }


        return null;
    }
}
