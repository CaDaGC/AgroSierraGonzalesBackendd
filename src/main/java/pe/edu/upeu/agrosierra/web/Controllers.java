package pe.edu.upeu.agrosierra.web;
import pe.edu.upeu.agrosierra.dominio.Dominio.*;
import pe.edu.upeu.agrosierra.repositorio.UsuariosRepository;
import pe.edu.upeu.agrosierra.seguridad.Seguridad;
import pe.edu.upeu.agrosierra.servicio.AgroService;
import pe.edu.upeu.agrosierra.web.Api.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.*;
@RestController
class AuthController {
 final AuthenticationManager manager;final UsuariosRepository users;final Seguridad.Tokens tokens;
 AuthController(AuthenticationManager m,UsuariosRepository u,Seguridad.Tokens t){manager=m;users=u;tokens=t;}
 @PostMapping("/api/v1/auth/login") LoginOut login(@Valid @RequestBody Login in){manager.authenticate(new UsernamePasswordAuthenticationToken(in.usuario(),in.contrasena()));
  Usuario u=users.findByUsername(in.usuario()).orElseThrow();Set<String> p=Seguridad.privilegios(u.rol);return new LoginOut(tokens.create(u.username,p),u.username,u.nombre,u.rol.name(),p);}
}
@RestController
class LotesController {
 final AgroService s;LotesController(AgroService s){this.s=s;}
 @GetMapping("/api/v1/lotes") @PreAuthorize("hasAuthority('LOTE_LEER')")
 PageOut<LoteOut> all(@RequestParam(defaultValue="0")@Min(0)int pagina,@RequestParam(defaultValue="20")@Min(1)@Max(100)int tamanio,@RequestParam(required=false)String valle,@RequestParam(required=false)Boolean campaniaCerrada){return PageOut.of(s.lotes(pagina,tamanio,valle,campaniaCerrada),LoteOut::of);}
 @GetMapping("/api/v1/lotes/{id}")@PreAuthorize("hasAuthority('LOTE_LEER')") LoteOut one(@PathVariable long id){return LoteOut.of(s.lote(id));}
 @GetMapping("/api/v1/lotes/{id}/labores")@PreAuthorize("hasAuthority('LABOR_LEER')")
 PageOut<LaborOut> labs(@PathVariable long id,@RequestParam(defaultValue="0")@Min(0)int pagina,@RequestParam(defaultValue="20")@Min(1)@Max(100)int tamanio){return PageOut.of(s.labores(id,pagina,tamanio),LaborOut::of);}
 @PostMapping("/api/v1/lotes/{id}/cerrar-campania")@PreAuthorize("hasAuthority('CAMPANIA_CERRAR')") LoteOut close(@PathVariable long id){return LoteOut.of(s.cerrar(id));}
}
@RestController
class LaboresController {
 final AgroService s;LaboresController(AgroService s){this.s=s;}
 @PostMapping("/api/v1/labores")@PreAuthorize("hasAuthority('LABOR_CREAR')")
 ResponseEntity<LaborOut> create(@Valid @RequestBody LaborIn in,Authentication a){return ResponseEntity.status(201).body(LaborOut.of(s.crear(in,a)));}
 @PutMapping("/api/v1/labores/{id}")@PreAuthorize("hasAuthority('LABOR_EDITAR')")
 LaborOut update(@PathVariable long id,@Valid @RequestBody LaborIn in,Authentication a){return LaborOut.of(s.editar(id,in,a));}
 @DeleteMapping("/api/v1/labores/{id}")@PreAuthorize("hasAuthority('LABOR_ELIMINAR')")
 ResponseEntity<Void> delete(@PathVariable long id){s.eliminar(id);return ResponseEntity.noContent().build();}
}
@RestController
class UsuarioController {
 final AgroService s;UsuarioController(AgroService s){this.s=s;}
 @GetMapping("/api/v1/usuarios/me") Map<String,Object> me(Authentication a){Usuario u=s.usuario(a);return Map.of("id",u.id,"usuario",u.username,"nombre",u.nombre,"rol",u.rol.name(),"privilegios",Seguridad.privilegios(u.rol));}
}
@RestControllerAdvice
class ManejadorDeErrores {
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ErrorOut> validation(MethodArgumentNotValidException e,HttpServletRequest r){Map<String,String> m=new LinkedHashMap<>();e.getBindingResult().getFieldErrors().forEach(x->m.putIfAbsent(x.getField(),x.getDefaultMessage()));return out(400,"Datos invalidos",r,m);}
 @ExceptionHandler(pe.edu.upeu.agrosierra.servicio.CantidadInvalidaException.class) ResponseEntity<ErrorOut> cantidad(pe.edu.upeu.agrosierra.servicio.CantidadInvalidaException e,HttpServletRequest r){return out(400,e.getMessage(),r,Map.of("cantidad",e.getMessage()));}
 @ExceptionHandler(BadCredentialsException.class) ResponseEntity<ErrorOut> badAuth(HttpServletRequest r){return out(401,"Credenciales incorrectas",r,Map.of());}
 @ExceptionHandler(ResponseStatusException.class) ResponseEntity<ErrorOut> status(ResponseStatusException e,HttpServletRequest r){return out(e.getStatusCode().value(),e.getReason(),r,Map.of());}
 @ExceptionHandler(Exception.class) ResponseEntity<ErrorOut> unexpected(HttpServletRequest r){return out(500,"Error interno del servidor",r,Map.of());}
 ResponseEntity<ErrorOut> out(int status,String msg,HttpServletRequest r,Map<String,String> fields){HttpStatus h=HttpStatus.valueOf(status);return ResponseEntity.status(status).body(new ErrorOut(LocalDateTime.now(),status,h.getReasonPhrase(),msg,r.getRequestURI(),fields));}
}


