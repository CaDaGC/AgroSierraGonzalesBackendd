package pe.edu.upeu.agrosierra.controller;
import pe.edu.upeu.agrosierra.dto.ApiDtos.*;
import pe.edu.upeu.agrosierra.mapper.AgroMapper;
import pe.edu.upeu.agrosierra.service.AgroService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/labores")
public class LaboresController {
 private final AgroService service;public LaboresController(AgroService s){service=s;}
 @PostMapping @PreAuthorize("hasAuthority('LABOR_CREAR')") public ResponseEntity<LaborResponse> crear(@Valid @RequestBody LaborRequest r,Authentication a){return ResponseEntity.status(201).body(AgroMapper.toLabor(service.crearLabor(r,a)));}
 @PutMapping("/{id}")@PreAuthorize("hasAuthority('LABOR_EDITAR')") public LaborResponse corregir(@PathVariable long id,@Valid @RequestBody LaborRequest r,Authentication a){return AgroMapper.toLabor(service.corregirLabor(id,r,a));}
 @DeleteMapping("/{id}")@PreAuthorize("hasAuthority('LABOR_ELIMINAR')") public ResponseEntity<Void> eliminar(@PathVariable long id){service.eliminarLabor(id);return ResponseEntity.noContent().build();}
}
