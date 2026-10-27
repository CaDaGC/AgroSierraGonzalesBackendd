package pe.edu.upeu.agrosierra.security;
import com.fasterxml.jackson.databind.ObjectMapper;
import pe.edu.upeu.agrosierra.dto.ApiDtos.ErrorResponse;
import jakarta.servlet.http.*;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
 private final ObjectMapper mapper; public RestAccessDeniedHandler(ObjectMapper mapper){this.mapper=mapper;}
 @Override public void handle(HttpServletRequest req,HttpServletResponse res,AccessDeniedException ex)throws IOException{res.setStatus(403);res.setContentType(MediaType.APPLICATION_JSON_VALUE);mapper.writeValue(res.getOutputStream(),new ErrorResponse(LocalDateTime.now(),403,"Forbidden","No tienes el privilegio requerido",req.getRequestURI(),Map.of()));}
}
