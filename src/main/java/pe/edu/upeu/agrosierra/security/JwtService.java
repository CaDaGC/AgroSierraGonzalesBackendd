package pe.edu.upeu.agrosierra.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.*;
@Service
public class JwtService {
 private final SecretKey key; private final long expirationMs;
 public JwtService(@Value("${jwt.secret}")String secret,@Value("${jwt.expiration-ms}")long expirationMs){this.key=Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));this.expirationMs=expirationMs;}
 public String generar(String usuario,Set<String> privilegios){Date now=new Date();return Jwts.builder().subject(usuario).claim("privilegios",privilegios).issuedAt(now).expiration(new Date(now.getTime()+expirationMs)).signWith(key).compact();}
 public Claims validar(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();}
}
