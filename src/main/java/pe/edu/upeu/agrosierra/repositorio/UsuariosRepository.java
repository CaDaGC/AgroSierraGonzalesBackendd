package pe.edu.upeu.agrosierra.repositorio;
import pe.edu.upeu.agrosierra.dominio.Dominio.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UsuariosRepository extends JpaRepository<Usuario,Long>{Optional<Usuario> findByUsername(String username);}
