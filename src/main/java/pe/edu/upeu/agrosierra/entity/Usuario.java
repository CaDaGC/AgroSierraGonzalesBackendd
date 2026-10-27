package pe.edu.upeu.agrosierra.entity;
import pe.edu.upeu.agrosierra.enums.Rol;
import jakarta.persistence.*;
@Entity @Table(name="usuarios")
public class Usuario {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false,unique=true,length=60) public String username;
 @Column(nullable=false) public String password;
 @Column(nullable=false,length=100) public String nombre;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=30) public Rol rol;
 protected Usuario(){}
 public Usuario(String username,String password,String nombre,Rol rol){this.username=username;this.password=password;this.nombre=nombre;this.rol=rol;}
}
