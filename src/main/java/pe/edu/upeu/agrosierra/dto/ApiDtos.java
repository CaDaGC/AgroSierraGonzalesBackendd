package pe.edu.upeu.agrosierra.dto;
import pe.edu.upeu.agrosierra.enums.TipoLabor;
import jakarta.validation.constraints.*;
import org.springframework.data.domain.Page;
import java.time.*;
import java.util.*;
public final class ApiDtos {private ApiDtos(){}
 public record LoginRequest(@NotBlank String usuario,@NotBlank String contrasena){}
 public record LoginResponse(String token,String usuario,String nombre,String rol,Set<String> privilegios){}
 public record UsuarioResponse(Long id,String usuario,String nombre,String rol,Set<String> privilegios){}
 public record LaborRequest(@NotNull Long loteId,@NotNull TipoLabor tipo,@NotNull LocalDate fecha,@Positive double cantidad,@Size(max=1000)String observacion){}
 public record LoteResponse(Long id,String codigo,String nombre,double hectareas,String cultivo,String valle,boolean campaniaCerrada,double dosisMaximaPorHectarea){}
 public record LaborResponse(Long id,Long loteId,TipoLabor tipo,LocalDate fecha,String responsable,double cantidad,String observacion){}
 public record PageResponse<T>(List<T> contenido,int pagina,int tamanio,long totalElementos,int totalPaginas,boolean ultima){
  public static <A,B> PageResponse<B> from(Page<A> page,java.util.function.Function<A,B> mapper){return new PageResponse<>(page.getContent().stream().map(mapper).toList(),page.getNumber(),page.getSize(),page.getTotalElements(),page.getTotalPages(),page.isLast());}
 }
 public record ErrorResponse(LocalDateTime timestamp,int status,String error,String message,String path,Map<String,String> validationErrors){}
}
