package pe.edu.upeu.agrosierra.entity;
import pe.edu.upeu.agrosierra.enums.TipoLabor;
import jakarta.persistence.*;
import java.time.*;
@Entity @Table(name="labores")
public class Labor {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="lote_id") public Lote lote;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) public TipoLabor tipo;
 @Column(nullable=false) public LocalDate fecha;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="responsable_id") public Usuario responsable;
 @Column(nullable=false) public double cantidad;
 @Column(nullable=false,length=1000) public String observacion;
 @Column(nullable=false,updatable=false) public Instant registradoEn;
 protected Labor(){}
 @PrePersist void registrar(){if(registradoEn==null)registradoEn=Instant.now();}
 public Labor(Lote lote,TipoLabor tipo,LocalDate fecha,Usuario responsable,double cantidad,String observacion){this.lote=lote;this.tipo=tipo;this.fecha=fecha;this.responsable=responsable;this.cantidad=cantidad;this.observacion=observacion;}
}
