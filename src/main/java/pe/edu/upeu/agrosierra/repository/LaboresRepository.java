package pe.edu.upeu.agrosierra.repository;
import pe.edu.upeu.agrosierra.entity.Labor;
import pe.edu.upeu.agrosierra.enums.TipoLabor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LaboresRepository extends JpaRepository<Labor,Long>{
 Page<Labor> findByLoteId(Long loteId,Pageable pageable);
 boolean existsByLoteIdAndTipo(Long loteId,TipoLabor tipo);
}
