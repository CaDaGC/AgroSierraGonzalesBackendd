package pe.edu.upeu.agrosierra.security;
import com.fasterxml.jackson.databind.ObjectMapper;
import pe.edu.upeu.agrosierra.dto.ApiDtos.ErrorResponse;
import jakarta.servlet.http.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
 private final ObjectMapper mapper; public RestAuthenticationEntryPoint(ObjectMapper mapper){this.mapper=mapper;}
 @Override public void commence(HttpServletRequest req,HttpServletResponse res,AuthenticationException ex)throws IOException{res.setStatus(401);res.setContentType(MediaType.APPLICATION_JSON_VALUE);mapper.writeValue(res.getOutputStream(),new ErrorResponse(LocalDateTime.now(),401,"Unauthorized","Token ausente, inválido o vencido",req.getRequestURI(),Map.of()));}
}
