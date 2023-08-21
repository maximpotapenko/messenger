package messenger.common.component.logging;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoggerMvcInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getDispatcherType().equals(DispatcherType.ERROR)) {
            log.info("[EXCEPTION-HANDLE][PATH: \"{}\"][ADDRESS: {}]", request.getServletPath(), request.getRemoteAddr());
        } else {
            log.info("[MVC][{}][PATH: \"{}\"][ADDRESS: {}]", request.getMethod(), request.getServletPath(), request.getRemoteAddr());
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        if(ex!=null) {
            log.error("Exception while processing request: {}", ex.getMessage(), ex);
            return;
        }

        if(request.getDispatcherType().equals(DispatcherType.ERROR)) {
            log.info("[EXCEPTION-RESPONSE][STATUS: {}][ADDRESS: {}]", response.getStatus(), request.getRemoteAddr());
            return;
        }

        log.info("[MVC][RESPONSE][STATUS: {}][ADDRESS: {}]", response.getStatus(), request.getRemoteAddr());
    }
}
