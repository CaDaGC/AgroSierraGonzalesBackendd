package pe.edu.upeu.agrosierra.config;
import pe.edu.upeu.agrosierra.entity.*;
import pe.edu.upeu.agrosierra.enums.*;
import pe.edu.upeu.agrosierra.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.util.*;
@Configuration
public class DatosIniciales {
 @Bean CommandLineRunner cargarDatos(UsuariosRepository usuarios,LotesRepository lotes,LaboresRepository labores,PasswordEncoder encoder){return args->{
  if(usuarios.count()>0||lotes.count()>0)return;
  Usuario tecnico=usuarios.save(new Usuario("tecnico1",encoder.encode("Agro2026*"),"Técnico Uno",Rol.TECNICO));
  Usuario jefe=usuarios.save(new Usuario("jefe1",encoder.encode("Agro2026*"),"Jefe de Campo",Rol.JEFE_CAMPO));
  usuarios.save(new Usuario("admin",encoder.encode("Agro2026*"),"Administrador",Rol.ADMINISTRADOR));
  List<Lote> l=lotes.saveAll(List.of(new Lote("LOT-001","Andenes Norte",4.5,"Papa","Valle Alto",false,2),new Lote("LOT-002","Quebrada Verde",3.2,"Maíz","Valle Alto",false,1.5),new Lote("LOT-003","Pampa Dorada",5,"Quinua","Valle Medio",false,1.2),new Lote("LOT-004","Ladera Sur",2.8,"Papa","Valle Medio",false,2),new Lote("LOT-005","Río Claro",6.1,"Maíz","Valle Bajo",false,1.5),new Lote("LOT-006","Cumbre Azul",3.7,"Quinua","Valle Bajo",false,1.2),new Lote("LOT-007","Campaña Cerrada 1",2,"Papa","Valle Alto",true,2),new Lote("LOT-008","Campaña Cerrada 2",2.4,"Maíz","Valle Bajo",true,1.5)));
  LocalDate hoy=LocalDate.now();List<Labor> seed=new ArrayList<>();
  for(int i=0;i<8;i++)seed.add(new Labor(l.get(i),i==0||i==2||i>=6?TipoLabor.COSECHA:TipoLabor.RIEGO,hoy.minusDays(i+1),i%2==0?tecnico:jefe,5+i,"Labor semilla"));
  for(int i=0;i<6;i++)seed.add(new Labor(l.get(i),TipoLabor.RIEGO,hoy.minusDays(i+10),tecnico,4+i,"Segunda labor"));
  seed.add(new Labor(l.get(0),TipoLabor.FUMIGACION,hoy.minusDays(20),jefe,8,"Control"));seed.add(new Labor(l.get(1),TipoLabor.RIEGO,hoy.minusDays(21),jefe,9,"Riego"));
  seed.add(new Labor(l.get(2),TipoLabor.FUMIGACION,hoy.minusDays(22),jefe,5,"Control"));seed.add(new Labor(l.get(3),TipoLabor.COSECHA,hoy.minusDays(23),jefe,7,"Cosecha"));
  seed.add(new Labor(l.get(4),TipoLabor.FUMIGACION,hoy.minusDays(24),jefe,5,"Control"));seed.add(new Labor(l.get(5),TipoLabor.COSECHA,hoy.minusDays(25),jefe,8,"Cosecha"));
  labores.saveAll(seed);
 };}
}

