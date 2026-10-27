package pe.edu.upeu.agrosierra.mapper;
import pe.edu.upeu.agrosierra.entity.*;
import pe.edu.upeu.agrosierra.dto.ApiDtos.*;
public final class AgroMapper {private AgroMapper(){}
 public static LoteResponse toLote(Lote x){return new LoteResponse(x.id,x.codigo,x.nombre,x.hectareas,x.cultivo,x.valle,x.campaniaCerrada,x.dosisMaximaPorHectarea);}
 public static LaborResponse toLabor(Labor x){return new LaborResponse(x.id,x.lote.id,x.tipo,x.fecha,x.responsable.nombre,x.cantidad,x.observacion);}
}
