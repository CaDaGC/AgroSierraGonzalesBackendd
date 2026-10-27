package pe.edu.upeu.agrosierra.service.impl;
import pe.edu.upeu.agrosierra.dto.ApiDtos.LaborRequest;
import pe.edu.upeu.agrosierra.entity.*;
import pe.edu.upeu.agrosierra.enums.TipoLabor;
import pe.edu.upeu.agrosierra.exception.CantidadInvalidaException;
import pe.edu.upeu.agrosierra.repository.*;
import pe.edu.upeu.agrosierra.service.AgroService;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.*;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class AgroServiceImpl implements AgroService {
 private final LotesRepository lotes; private final LaboresRepository labores; private final UsuariosRepository usuarios;
 public AgroServiceImpl(LotesRepository l,LaboresRepository b,UsuariosRepository u){lotes=l;labores=b;usuarios=u;}
 @Override public Page<Lote> listarLotes(int pagina,int tamanio,String valle,Boolean cerrada){Pageable p=PageRequest.of(pagina,tamanio,Sort.by("codigo"));if(valle!=null&&!valle.isBlank()&&cerrada!=null)return lotes.findByValleIgnoreCaseAndCampaniaCerrada(valle,cerrada,p);if(valle!=null&&!valle.isBlank())return lotes.findByValleIgnoreCase(valle,p);if(cerrada!=null)return lotes.findByCampaniaCerrada(cerrada,p);return lotes.findAll(p);}
 @Override public Lote obtenerLote(long id){return lotes.findById(id).orElseThrow(()->error(HttpStatus.NOT_FOUND,"Lote no encontrado"));}
 @Override public Page<Labor> listarLabores(long id,int pagina,int tamanio){obtenerLote(id);return labores.findByLoteId(id,PageRequest.of(pagina,tamanio,Sort.by(Sort.Order.desc("fecha"),Sort.Order.desc("id"))));}
 @Override @Transactional public Labor crearLabor(LaborRequest r,Authentication a){Lote l=obtenerLote(r.loteId());campaniaAbierta(l);validarDosis(l,r);return labores.save(new Labor(l,r.tipo(),r.fecha(),usuarioActual(a),r.cantidad(),texto(r.observacion())));}
 @Override @Transactional public Labor corregirLabor(long id,LaborRequest r,Authentication a){Labor l=labores.findById(id).orElseThrow(()->error(HttpStatus.NOT_FOUND,"Labor no encontrada"));if(!l.lote.id.equals(r.loteId()))throw error(HttpStatus.NOT_FOUND,"Labor no encontrada");campaniaAbierta(l.lote);Usuario actor=usuarioActual(a);Set<String> p=a.getAuthorities().stream().map(x->x.getAuthority()).collect(Collectors.toSet());if(!p.contains("LABOR_EDITAR_TODAS")&&(!l.responsable.id.equals(actor.id)||l.registradoEn.isBefore(Instant.now().minus(Duration.ofHours(24)))))throw error(HttpStatus.FORBIDDEN,"Solo puedes corregir tus labores dentro de las 24 horas siguientes");validarDosis(l.lote,r);l.tipo=r.tipo();l.fecha=r.fecha();l.cantidad=r.cantidad();l.observacion=texto(r.observacion());return labores.save(l);}
 @Override @Transactional public void eliminarLabor(long id){if(!labores.existsById(id))throw error(HttpStatus.NOT_FOUND,"Labor no encontrada");labores.deleteById(id);}
 @Override @Transactional public Lote cerrarCampania(long id){Lote l=obtenerLote(id);if(!labores.existsByLoteIdAndTipo(id,TipoLabor.COSECHA))throw error(HttpStatus.CONFLICT,"No se puede cerrar campaña sin una labor COSECHA");l.campaniaCerrada=true;return lotes.save(l);}
 @Override public Usuario usuarioActual(Authentication a){return usuarios.findByUsername(a.getName()).orElseThrow(()->error(HttpStatus.NOT_FOUND,"Usuario no encontrado"));}
 private void campaniaAbierta(Lote l){if(l.campaniaCerrada)throw error(HttpStatus.CONFLICT,"La campaña del lote está cerrada");}
 private void validarDosis(Lote l,LaborRequest r){if(r.tipo()==TipoLabor.FUMIGACION&&r.cantidad()>l.dosisMaximaPorHectarea*l.hectareas)throw new CantidadInvalidaException("La cantidad supera la dosis máxima permitida para el lote");}
 private String texto(String s){return s==null?"":s;}
 private ResponseStatusException error(HttpStatus s,String m){return new ResponseStatusException(s,m);}
}
