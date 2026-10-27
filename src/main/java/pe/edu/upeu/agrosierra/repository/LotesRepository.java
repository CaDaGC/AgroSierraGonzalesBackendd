package pe.edu.upeu.agrosierra.repository;
import pe.edu.upeu.agrosierra.entity.Lote;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LotesRepository extends JpaRepository<Lote,Long>{
 Page<Lote> findByValleIgnoreCase(String valle,Pageable pageable);
 Page<Lote> findByCampaniaCerrada(boolean cerrada,Pageable pageable);
 Page<Lote> findByValleIgnoreCaseAndCampaniaCerrada(String valle,boolean cerrada,Pageable pageable);
}
