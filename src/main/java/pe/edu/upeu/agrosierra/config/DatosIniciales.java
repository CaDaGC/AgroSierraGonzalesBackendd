package pe.edu.upeu.agrosierra.config;
import pe.edu.upeu.agrosierra.dominio.Dominio.*;
import pe.edu.upeu.agrosierra.repositorio.LotesRepository;
import pe.edu.upeu.agrosierra.repositorio.LaboresRepository;
import pe.edu.upeu.agrosierra.repositorio.UsuariosRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.util.*;
@Configuration
public class DatosIniciales {
 @Bean CommandLineRunner seed(UsuariosRepository us,LotesRepository ls,LaboresRepository labs,PasswordEncoder enc){return args->{
  if(us.count()>0||ls.count()>0)return;
  Usuario te=us.save(new Usuario("tecnico1",enc.encode("Agro2026*"),"Técnico Uno",Rol.TECNICO));
  Usuario je=us.save(new Usuario("jefe1",enc.encode("Agro2026*"),"Jefe de Campo",Rol.JEFE_CAMPO));
  us.save(new Usuario("admin",enc.encode("Agro2026*"),"Administrador",Rol.ADMINISTRADOR));
  List<Lote> l=ls.saveAll(List.of(
   new Lote("LOT-001","Andenes Norte",4.5,"Papa","Valle Alto",false,2),new Lote("LOT-002","Quebrada Verde",3.2,"Maíz","Valle Alto",false,1.5),
   new Lote("LOT-003","Pampa Dorada",5,"Quinua","Valle Medio",false,1.2),new Lote("LOT-004","Ladera Sur",2.8,"Papa","Valle Medio",false,2),
   new Lote("LOT-005","Río Claro",6.1,"Maíz","Valle Bajo",false,1.5),new Lote("LOT-006","Cumbre Azul",3.7,"Quinua","Valle Bajo",false,1.2),
   new Lote("LOT-007","Campaña Cerrada 1",2,"Papa","Valle Alto",true,2),new Lote("LOT-008","Campaña Cerrada 2",2.4,"Maíz","Valle Bajo",true,1.5)));
  LocalDate d=LocalDate.now();List<Labor> x=new ArrayList<>();
  for(int i=0;i<8;i++){x.add(new Labor(l.get(i),i==0||i==2||i>=6?TipoLabor.COSECHA:TipoLabor.RIEGO,d.minusDays(i+1),i%2==0?te:je,5+i,"Labor semilla"));}
  for(int i=0;i<6;i++){x.add(new Labor(l.get(i),TipoLabor.RIEGO,d.minusDays(i+10),te,4+i,"Segunda labor"));}
  x.add(new Labor(l.get(0),TipoLabor.FUMIGACION,d.minusDays(20),je,8,"Control"));x.add(new Labor(l.get(1),TipoLabor.RIEGO,d.minusDays(21),je,9,"Riego"));
  x.add(new Labor(l.get(2),TipoLabor.FUMIGACION,d.minusDays(22),je,10,"Control"));x.add(new Labor(l.get(3),TipoLabor.COSECHA,d.minusDays(23),je,7,"Cosecha"));
  x.add(new Labor(l.get(4),TipoLabor.FUMIGACION,d.minusDays(24),je,10,"Control"));x.add(new Labor(l.get(5),TipoLabor.COSECHA,d.minusDays(25),je,8,"Cosecha"));
  labs.saveAll(x);
 }; }
}


