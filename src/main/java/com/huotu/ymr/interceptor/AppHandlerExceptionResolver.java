package com.huotu.ymr.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by lgh on 2015/12/9.
 */
public class AppHandlerExceptionResolver implements HandlerExceptionResolver {
    private static Log log = LogFactory.getLog(AppHandlerExceptionResolver.class);
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String requestURI = request.getRequestURI().substring(request.getContextPath().length());
        if (requestURI.startsWith("/app/")) {
            ModelAndView mv = new ModelAndView();
            PhysicalApiResult result = new PhysicalApiResult();
            result.setSystemResultDescription(ex.getLocalizedMessage());
            try {
                throw ex;
//            } catch (ShopCloseException e) {
//                result.setSystemResultCode(CommonEnum.AppCode.ERROR_USER_SHOPCLOSE.getValue());
//                result.setSystemResultDescription("商城已经关闭");
//            }catch (ShopExpiredException e){
//                result.setSystemResultCode(CommonEnum.AppCode.ERROR_USER_SHOPEXPIRED.getValue());
//                result.setSystemResultDescription("商城已经过期");
            } catch (Exception e) {
                log.error("unExcepted error ",e);
                result.setSystemResultCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }

            mv.addObject("__realResult", result);
            MappingJackson2JsonView jsonView = new MappingJackson2JsonView();

            jsonView.setContentType("application/json;charset=UTF-8");
            jsonView.setModelKey("__realResult");
            jsonView.setExtractValueFromSingleKeyModel(true);
            mv.setView(jsonView);
            return mv;
        }
        return null;
    }
}
