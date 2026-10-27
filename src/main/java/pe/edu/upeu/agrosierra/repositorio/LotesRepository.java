package pe.edu.upeu.agrosierra.repositorio;
import pe.edu.upeu.agrosierra.dominio.Dominio.Lote;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LotesRepository extends JpaRepository<Lote,Long>{
 Page<Lote> findByValleIgnoreCase(String valle,Pageable p);
 Page<Lote> findByCampaniaCerrada(boolean cerrada,Pageable p);
 Page<Lote> findByValleIgnoreCaseAndCampaniaCerrada(String valle,boolean cerrada,Pageable p);
}
