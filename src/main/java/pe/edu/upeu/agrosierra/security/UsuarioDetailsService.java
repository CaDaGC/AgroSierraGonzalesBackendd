package pe.edu.upeu.agrosierra.security;
import pe.edu.upeu.agrosierra.entity.Usuario;
import pe.edu.upeu.agrosierra.repository.UsuariosRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
@Service
public class UsuarioDetailsService implements UserDetailsService {
 private final UsuariosRepository usuarios;
 public UsuarioDetailsService(UsuariosRepository usuarios){this.usuarios=usuarios;}
 @Override public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException{Usuario u=usuarios.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("Usuario no encontrado"));return User.withUsername(u.username).password(u.password).authorities(PermisosPorRol.obtener(u.rol).stream().map(SimpleGrantedAuthority::new).toList()).build();}
}
