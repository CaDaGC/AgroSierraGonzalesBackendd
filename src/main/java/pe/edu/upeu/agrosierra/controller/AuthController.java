package pe.edu.upeu.agrosierra.controller;
import pe.edu.upeu.agrosierra.dto.ApiDtos.*;
import pe.edu.upeu.agrosierra.entity.Usuario;
import pe.edu.upeu.agrosierra.repository.UsuariosRepository;
import pe.edu.upeu.agrosierra.security.*;
import jakarta.validation.Valid;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;
import java.util.Set;
@RestController @RequestMapping("/api/v1/auth")
public class AuthController {
 private final AuthenticationManager manager;private final UsuariosRepository usuarios;private final JwtService jwt;
 public AuthController(AuthenticationManager m,UsuariosRepository u,JwtService j){manager=m;usuarios=u;jwt=j;}
 @PostMapping("/login") public LoginResponse login(@Valid @RequestBody LoginRequest r){manager.authenticate(new UsernamePasswordAuthenticationToken(r.usuario(),r.contrasena()));Usuario u=usuarios.findByUsername(r.usuario()).orElseThrow();Set<String> p=PermisosPorRol.obtener(u.rol);return new LoginResponse(jwt.generar(u.username,p),u.username,u.nombre,u.rol.name(),p);}
}
