package pe.edu.upeu.agrosierra.controller;
import pe.edu.upeu.agrosierra.dto.ApiDtos.*;
import pe.edu.upeu.agrosierra.entity.*;
import pe.edu.upeu.agrosierra.mapper.AgroMapper;
import pe.edu.upeu.agrosierra.service.AgroService;
import pe.edu.upeu.agrosierra.util.ApiLimits;
import jakarta.validation.constraints.*;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RestController @Validated @RequestMapping("/api/v1/lotes")
public class LotesController {
 private final AgroService service;public LotesController(AgroService s){service=s;}
 @GetMapping @PreAuthorize("hasAuthority('LOTE_LEER')") public PageResponse<LoteResponse> listar(@RequestParam(defaultValue="0")@Min(0)int pagina,@RequestParam(defaultValue="20")@Min(1)@Max(ApiLimits.MAX_PAGE_SIZE)int tamanio,@RequestParam(required=false)String valle,@RequestParam(required=false)Boolean campaniaCerrada){Page<Lote> p=service.listarLotes(pagina,tamanio,valle,campaniaCerrada);return PageResponse.from(p,AgroMapper::toLote);}
 @GetMapping("/{id}")@PreAuthorize("hasAuthority('LOTE_LEER')") public LoteResponse obtener(@PathVariable long id){return AgroMapper.toLote(service.obtenerLote(id));}
 @GetMapping("/{id}/labores")@PreAuthorize("hasAuthority('LABOR_LEER')") public PageResponse<LaborResponse> labores(@PathVariable long id,@RequestParam(defaultValue="0")@Min(0)int pagina,@RequestParam(defaultValue="20")@Min(1)@Max(ApiLimits.MAX_PAGE_SIZE)int tamanio){return PageResponse.from(service.listarLabores(id,pagina,tamanio),AgroMapper::toLabor);}
 @PostMapping("/{id}/cerrar-campania")@PreAuthorize("hasAuthority('CAMPANIA_CERRAR')") public LoteResponse cerrar(@PathVariable long id){return AgroMapper.toLote(service.cerrarCampania(id));}
}

