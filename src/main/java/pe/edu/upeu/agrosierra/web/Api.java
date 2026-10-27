package pe.edu.upeu.agrosierra.web;
import pe.edu.upeu.agrosierra.dominio.Dominio.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;
import org.springframework.data.domain.Page;
public final class Api { private Api(){}
 public record Login(@NotBlank String usuario,@NotBlank String contrasena){}
 public record LoginOut(String token,String usuario,String nombre,String rol,Set<String> privilegios){}
 public record LaborIn(@NotNull Long loteId,@NotNull TipoLabor tipo,@NotNull LocalDate fecha,@Positive double cantidad,@Size(max=1000)String observacion){}
 public record LoteOut(Long id,String codigo,String nombre,double hectareas,String cultivo,String valle,boolean campaniaCerrada,double dosisMaximaPorHectarea){
  static LoteOut of(Lote l){return new LoteOut(l.id,l.codigo,l.nombre,l.hectareas,l.cultivo,l.valle,l.campaniaCerrada,l.dosisMaximaPorHectarea);}
 }
 public record LaborOut(Long id,Long loteId,TipoLabor tipo,LocalDate fecha,String responsable,double cantidad,String observacion){
  static LaborOut of(Labor l){return new LaborOut(l.id,l.lote.id,l.tipo,l.fecha,l.responsable.nombre,l.cantidad,l.observacion);}
 }
 public record PageOut<T>(List<T> contenido,int pagina,int tamanio,long totalElementos,int totalPaginas,boolean ultima){
  static <A,B> PageOut<B> of(Page<A> p,java.util.function.Function<A,B> f){return new PageOut<>(p.getContent().stream().map(f).toList(),p.getNumber(),p.getSize(),p.getTotalElements(),p.getTotalPages(),p.isLast());}
 }
 public record ErrorOut(java.time.LocalDateTime timestamp,int status,String error,String message,String path,Map<String,String> validationErrors){}
}
