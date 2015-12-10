package com.huotu.ymr.interceptor;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.exception.*;
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
            } catch (AmountErrorException e) {
                result.setSystemResultCode(CommonEnum.AppCode.PARAMETER_ERROR.getValue());
                result.setSystemResultDescription("认购金额小于起购金额");
            }catch (CrowdFundingNotExitsException e){
                result.setSystemResultCode(CommonEnum.AppCode.PARAMETER_ERROR.getValue());
                result.setSystemResultDescription("众筹项目不存在");
            }catch (CrowdFundingPublicIllegalException e){
                result.setSystemResultCode(CommonEnum.AppCode.PARAMETER_ERROR.getValue());
                result.setSystemResultDescription("众筹项目合作发起人信息非法");
            }catch (PhoneFormatErrorException e){
                result.setSystemResultCode(CommonEnum.AppCode.PARAMETER_ERROR.getValue());
                result.setSystemResultDescription("手机格式错误");
            }catch (UserLevelErrorException e){
                result.setSystemResultCode(CommonEnum.AppCode.PARAMETER_ERROR.getValue());
                result.setSystemResultDescription("该用户级别不被允许此操作");
            }catch (UserNotExitsException e){
                result.setSystemResultCode(CommonEnum.AppCode.PARAMETER_ERROR.getValue());
                result.setSystemResultDescription("用户不存在");
            }catch (UserRequestIllegalException e){
                result.setSystemResultCode(CommonEnum.AppCode.PARAMETER_ERROR.getValue());
                result.setSystemResultDescription("用户请求非法");
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
