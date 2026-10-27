package pe.edu.upeu.agrosierra.dominio;
import jakarta.persistence.*;
import java.time.LocalDate;
public final class Dominio { private Dominio() {} 
 public enum TipoLabor { RIEGO, FUMIGACION, COSECHA }
 public enum Rol { TECNICO, JEFE_CAMPO, ADMINISTRADOR }
 @Entity @Table(name="usuarios") public static class Usuario {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  @Column(nullable=false,unique=true) public String username;
  @Column(nullable=false) public String password;
  @Column(nullable=false) public String nombre;
  @Enumerated(EnumType.STRING) @Column(nullable=false) public Rol rol;
  protected Usuario() {}
  public Usuario(String u,String p,String n,Rol r){username=u;password=p;nombre=n;rol=r;}
 }
 @Entity @Table(name="lotes") public static class Lote {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  @Column(nullable=false,unique=true) public String codigo;
  @Column(nullable=false) public String nombre;
  @Column(nullable=false) public double hectareas;
  @Column(nullable=false) public String cultivo;
  @Column(nullable=false) public String valle;
  @Column(nullable=false) public boolean campaniaCerrada;
  @Column(nullable=false) public double dosisMaximaPorHectarea;
  protected Lote() {}
  public Lote(String c,String n,double h,String cu,String v,boolean cerrada,double dosis){codigo=c;nombre=n;hectareas=h;cultivo=cu;valle=v;campaniaCerrada=cerrada;dosisMaximaPorHectarea=dosis;}
 }
 @Entity @Table(name="labores") public static class Labor {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="lote_id") public Lote lote;
  @Enumerated(EnumType.STRING) @Column(nullable=false) public TipoLabor tipo;
  @Column(nullable=false) public LocalDate fecha;
  @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="responsable_id") public Usuario responsable;
  @Column(nullable=false) public double cantidad;
  @Column(nullable=false,length=1000) public String observacion;
  @Column(nullable=false,updatable=false) public java.time.Instant registradoEn;
  protected Labor() {}
  @PrePersist void registrar(){if(registradoEn==null)registradoEn=java.time.Instant.now();}
  public Labor(Lote l,TipoLabor t,LocalDate f,Usuario u,double c,String o){lote=l;tipo=t;fecha=f;responsable=u;cantidad=c;observacion=o;}
 }
}

