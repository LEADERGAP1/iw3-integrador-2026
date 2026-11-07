
package ar.edu.iua.iw3.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "camiones")
@Getter
@Setter
public class Camion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_externo", nullable = false, unique = true, length = 50)
    private String codigoExterno;

    @Column(nullable = false, length = 20)
    private String patente;

    @Column(length = 255)
    private String descripcion;
}
