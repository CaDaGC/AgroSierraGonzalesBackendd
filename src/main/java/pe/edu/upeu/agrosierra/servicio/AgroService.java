package pe.edu.upeu.agrosierra.servicio;
import pe.edu.upeu.agrosierra.dominio.Dominio.*;
import pe.edu.upeu.agrosierra.repositorio.LotesRepository;
import pe.edu.upeu.agrosierra.repositorio.LaboresRepository;
import pe.edu.upeu.agrosierra.repositorio.UsuariosRepository;
import pe.edu.upeu.agrosierra.web.Api.LaborIn;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
@Service
public class AgroService {
 final LotesRepository lotes; final LaboresRepository labores; final UsuariosRepository usuarios;
 public AgroService(LotesRepository a,LaboresRepository b,UsuariosRepository c){lotes=a;labores=b;usuarios=c;}
 public Page<Lote> lotes(int page,int size,String valle,Boolean cerrada){Pageable p=PageRequest.of(page,size,Sort.by("codigo"));
  if(valle!=null&&!valle.isBlank()&&cerrada!=null)return lotes.findByValleIgnoreCaseAndCampaniaCerrada(valle,cerrada,p);
  if(valle!=null&&!valle.isBlank())return lotes.findByValleIgnoreCase(valle,p);
  if(cerrada!=null)return lotes.findByCampaniaCerrada(cerrada,p);return lotes.findAll(p);}
 public Lote lote(long id){return lotes.findById(id).orElseThrow(()->err(HttpStatus.NOT_FOUND,"Lote no encontrado"));}
 public Page<Labor> labores(long id,int page,int size){lote(id);return labores.findByLoteId(id,PageRequest.of(page,size,Sort.by(Sort.Order.desc("fecha"),Sort.Order.desc("id"))));}
 @Transactional public Labor crear(LaborIn r,Authentication a){Lote l=lote(r.loteId());abierta(l);dosis(l,r);return labores.save(new Labor(l,r.tipo(),r.fecha(),usuario(a),r.cantidad(),texto(r.observacion())));}
 @Transactional public Labor editar(long id,LaborIn r,Authentication a){Labor l=labores.findById(id).orElseThrow(()->err(HttpStatus.NOT_FOUND,"Labor no encontrada"));
  if(!l.lote.id.equals(r.loteId()))throw err(HttpStatus.NOT_FOUND,"Labor no encontrada");abierta(l.lote);
  Usuario actor=usuario(a);boolean todas=a.getAuthorities().stream().anyMatch(x->x.getAuthority().equals("LABOR_EDITAR_TODAS"));
  if(!todas&&(!l.responsable.id.equals(actor.id)||l.registradoEn.isBefore(java.time.Instant.now().minus(java.time.Duration.ofHours(24)))))throw err(HttpStatus.FORBIDDEN,"Solo puedes corregir tus labores dentro de las 24 horas siguientes");
  dosis(l.lote,r);l.tipo=r.tipo();l.fecha=r.fecha();l.cantidad=r.cantidad();l.observacion=texto(r.observacion());return labores.save(l);}
 @Transactional public void eliminar(long id){if(!labores.existsById(id))throw err(HttpStatus.NOT_FOUND,"Labor no encontrada");labores.deleteById(id);}
 @Transactional public Lote cerrar(long id){Lote l=lote(id);if(!labores.existsByLoteIdAndTipo(id,TipoLabor.COSECHA))throw err(HttpStatus.CONFLICT,"No se puede cerrar campaña sin una labor COSECHA");l.campaniaCerrada=true;return lotes.save(l);}
 public Usuario usuario(Authentication a){return usuarios.findByUsername(a.getName()).orElseThrow(()->err(HttpStatus.NOT_FOUND,"Usuario no encontrado"));}
 void abierta(Lote l){if(l.campaniaCerrada)throw err(HttpStatus.CONFLICT,"La campaña del lote está cerrada");}
 void dosis(Lote l,LaborIn r){if(r.tipo()==TipoLabor.FUMIGACION&&r.cantidad()>l.dosisMaximaPorHectarea*l.hectareas)throw new CantidadInvalidaException("La cantidad supera la dosis máxima por hectárea del lote");}
 String texto(String s){return s==null?"":s;}
 ResponseStatusException err(HttpStatus s,String m){return new ResponseStatusException(s,m);}
}


