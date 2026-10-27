package pe.edu.upeu.agrosierra.seguridad;
import pe.edu.upeu.agrosierra.dominio.Dominio.*;
import pe.edu.upeu.agrosierra.repositorio.UsuariosRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import pe.edu.upeu.agrosierra.web.Api.ErrorOut;
import java.time.LocalDateTime;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.*;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
public final class Seguridad { private Seguridad() {}
 public static Set<String> privilegios(Rol r){return switch(r){
  case TECNICO->Set.of("LOTE_LEER","LABOR_LEER","LABOR_CREAR","LABOR_EDITAR","LABOR_EDITAR_PROPIAS");
  case JEFE_CAMPO->Set.of("LOTE_LEER","LABOR_LEER","LABOR_CREAR","LABOR_EDITAR","LABOR_EDITAR_TODAS","LABOR_ELIMINAR","CAMPANIA_CERRAR");
  case ADMINISTRADOR->Set.of("LOTE_LEER","LABOR_LEER","USUARIO_GESTIONAR");};}
 @Service public static class Details implements UserDetailsService{
  final UsuariosRepository usuarios; public Details(UsuariosRepository u){usuarios=u;}
  public UserDetails loadUserByUsername(String name){Usuario u=usuarios.findByUsername(name).orElseThrow(()->new UsernameNotFoundException(name));
   var auth=privilegios(u.rol).stream().map(SimpleGrantedAuthority::new).toList();
   return User.withUsername(u.username).password(u.password).authorities(auth).build();}
 }
 @Service public static class Tokens {
  final SecretKey key; final long expiry;
  public Tokens(@Value("${jwt.secret}")String secret,@Value("${jwt.expiration-ms}")long exp){key=Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));expiry=exp;}
  public String create(String user,Set<String> p){Date now=new Date();return Jwts.builder().subject(user).claim("privilegios",p).issuedAt(now).expiration(new Date(now.getTime()+expiry)).signWith(key).compact();}
  public Claims parse(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();}
 }
 @Component public static class JwtFilter extends OncePerRequestFilter {
  final Tokens tokens; public JwtFilter(Tokens t){tokens=t;}
  protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{
   String h=req.getHeader("Authorization"); if(h!=null&&h.startsWith("Bearer "))try{
    Claims c=tokens.parse(h.substring(7)); @SuppressWarnings("unchecked") List<String> p=(List<String>)c.get("privilegios");
    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(c.getSubject(),null,p.stream().map(SimpleGrantedAuthority::new).toList()));
   }catch(JwtException|IllegalArgumentException ex){SecurityContextHolder.clearContext();}
   chain.doFilter(req,res);
  }
 }
 @Configuration @EnableMethodSecurity public static class Config {
  @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
  @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration c)throws Exception{return c.getAuthenticationManager();}
  @Bean SecurityFilterChain filterChain(HttpSecurity http,JwtFilter jwt,ObjectMapper mapper)throws Exception{return http.csrf(c->c.disable())
   .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   .exceptionHandling(e->e.authenticationEntryPoint((q,r,x)->{r.setStatus(401);r.setContentType("application/json");mapper.writeValue(r.getOutputStream(),new ErrorOut(LocalDateTime.now(),401,"Unauthorized","Token ausente, inválido o vencido",q.getRequestURI(),Map.of()));}).accessDeniedHandler((q,r,x)->{r.setStatus(403);r.setContentType("application/json");mapper.writeValue(r.getOutputStream(),new ErrorOut(LocalDateTime.now(),403,"Forbidden","No tienes el privilegio requerido",q.getRequestURI(),Map.of()));}))
   .authorizeHttpRequests(a->a.requestMatchers("/api/v1/auth/login").permitAll().anyRequest().authenticated())
   .addFilterBefore(jwt,UsernamePasswordAuthenticationFilter.class).build();}
 }
}



