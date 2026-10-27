package pe.edu.upeu.agrosierra.controller;
import pe.edu.upeu.agrosierra.dto.ApiDtos.UsuarioResponse;
import pe.edu.upeu.agrosierra.security.PermisosPorRol;
import pe.edu.upeu.agrosierra.service.AgroService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/usuarios")
public class UsuarioController {
 private final AgroService service;public UsuarioController(AgroService s){service=s;}
 @GetMapping("/me") public UsuarioResponse me(Authentication a){var u=service.usuarioActual(a);return new UsuarioResponse(u.id,u.username,u.nombre,u.rol.name(),PermisosPorRol.obtener(u.rol));}
}
