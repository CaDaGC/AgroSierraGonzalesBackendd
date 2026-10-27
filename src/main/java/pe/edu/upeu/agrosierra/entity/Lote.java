package pe.edu.upeu.agrosierra.entity;
import jakarta.persistence.*;
@Entity @Table(name="lotes")
public class Lote {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false,unique=true,length=20) public String codigo;
 @Column(nullable=false,length=100) public String nombre;
 @Column(nullable=false) public double hectareas;
 @Column(nullable=false,length=60) public String cultivo;
 @Column(nullable=false,length=60) public String valle;
 @Column(nullable=false) public boolean campaniaCerrada;
 @Column(nullable=false) public double dosisMaximaPorHectarea;
 protected Lote(){}
 public Lote(String codigo,String nombre,double hectareas,String cultivo,String valle,boolean cerrada,double dosis){this.codigo=codigo;this.nombre=nombre;this.hectareas=hectareas;this.cultivo=cultivo;this.valle=valle;this.campaniaCerrada=cerrada;this.dosisMaximaPorHectarea=dosis;}
}
