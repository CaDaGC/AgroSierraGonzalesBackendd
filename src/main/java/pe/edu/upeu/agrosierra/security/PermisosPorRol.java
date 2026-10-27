package pe.edu.upeu.agrosierra.security;
import pe.edu.upeu.agrosierra.enums.Rol;
import java.util.*;
public final class PermisosPorRol {private PermisosPorRol(){}
 public static Set<String> obtener(Rol rol){return switch(rol){
  case TECNICO->Set.of("LOTE_LEER","LABOR_LEER","LABOR_CREAR","LABOR_EDITAR","LABOR_EDITAR_PROPIAS");
  case JEFE_CAMPO->Set.of("LOTE_LEER","LABOR_LEER","LABOR_CREAR","LABOR_EDITAR","LABOR_EDITAR_TODAS","LABOR_ELIMINAR","CAMPANIA_CERRAR");
  case ADMINISTRADOR->Set.of("LOTE_LEER","LABOR_LEER","USUARIO_GESTIONAR");};}
}
