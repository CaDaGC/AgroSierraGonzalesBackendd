package pe.edu.upeu.agrosierra.service;
import pe.edu.upeu.agrosierra.dto.ApiDtos.LaborRequest;
import pe.edu.upeu.agrosierra.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
public interface AgroService {
 Page<Lote> listarLotes(int pagina,int tamanio,String valle,Boolean campaniaCerrada);
 Lote obtenerLote(long id);
 Page<Labor> listarLabores(long loteId,int pagina,int tamanio);
 Labor crearLabor(LaborRequest request,Authentication actor);
 Labor corregirLabor(long id,LaborRequest request,Authentication actor);
 void eliminarLabor(long id);
 Lote cerrarCampania(long id);
 Usuario usuarioActual(Authentication actor);
}
