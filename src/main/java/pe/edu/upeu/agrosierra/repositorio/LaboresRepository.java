package pe.edu.upeu.agrosierra.repositorio;
import pe.edu.upeu.agrosierra.dominio.Dominio.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LaboresRepository extends JpaRepository<Labor,Long>{
 Page<Labor> findByLoteId(Long id,Pageable p);
 boolean existsByLoteIdAndTipo(Long id,TipoLabor tipo);
}
