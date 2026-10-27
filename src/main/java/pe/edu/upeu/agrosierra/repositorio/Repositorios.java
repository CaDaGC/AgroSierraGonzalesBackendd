package pe.edu.upeu.agrosierra.repositorio;
import pe.edu.upeu.agrosierra.dominio.Dominio.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.*;
import java.util.Optional;
public final class Repositorios { private Repositorios() {}
 public interface Usuarios extends JpaRepository<Usuario,Long>{Optional<Usuario> findByUsername(String username);}
 public interface Lotes extends JpaRepository<Lote,Long>{
  Page<Lote> findByValleIgnoreCase(String valle,Pageable p);
  Page<Lote> findByCampaniaCerrada(boolean cerrada,Pageable p);
  Page<Lote> findByValleIgnoreCaseAndCampaniaCerrada(String valle,boolean cerrada,Pageable p);
 }
 public interface Labores extends JpaRepository<Labor,Long>{
  Page<Labor> findByLoteId(Long id,Pageable p);
  boolean existsByLoteIdAndTipo(Long id,TipoLabor tipo);
 }
}
