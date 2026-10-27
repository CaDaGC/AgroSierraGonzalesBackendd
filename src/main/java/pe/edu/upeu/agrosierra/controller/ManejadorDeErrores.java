package pe.edu.upeu.agrosierra.controller;
import pe.edu.upeu.agrosierra.dto.ApiDtos.ErrorResponse;
import pe.edu.upeu.agrosierra.exception.CantidadInvalidaException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.*;
@RestControllerAdvice
public class ManejadorDeErrores {
 @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<ErrorResponse> validar(MethodArgumentNotValidException ex,HttpServletRequest req){Map<String,String> fields=new LinkedHashMap<>();for(FieldError e:ex.getBindingResult().getFieldErrors())fields.putIfAbsent(e.getField(),e.getDefaultMessage());return respuesta(HttpStatus.BAD_REQUEST,"Datos inválidos",req,fields);}
 @ExceptionHandler(CantidadInvalidaException.class) public ResponseEntity<ErrorResponse> cantidad(CantidadInvalidaException ex,HttpServletRequest req){return respuesta(HttpStatus.BAD_REQUEST,ex.getMessage(),req,Map.of("cantidad",ex.getMessage()));}
 @ExceptionHandler(BadCredentialsException.class) public ResponseEntity<ErrorResponse> credenciales(HttpServletRequest req){return respuesta(HttpStatus.UNAUTHORIZED,"Credenciales incorrectas",req,Map.of());}
 @ExceptionHandler(ResponseStatusException.class) public ResponseEntity<ErrorResponse> estado(ResponseStatusException ex,HttpServletRequest req){return respuesta(HttpStatus.valueOf(ex.getStatusCode().value()),ex.getReason(),req,Map.of());}
 @ExceptionHandler(Exception.class) public ResponseEntity<ErrorResponse> inesperado(HttpServletRequest req){return respuesta(HttpStatus.INTERNAL_SERVER_ERROR,"Error interno del servidor",req,Map.of());}
 private ResponseEntity<ErrorResponse> respuesta(HttpStatus s,String m,HttpServletRequest req,Map<String,String> fields){return ResponseEntity.status(s).body(new ErrorResponse(LocalDateTime.now(),s.value(),s.getReasonPhrase(),m,req.getRequestURI(),fields));}
}
