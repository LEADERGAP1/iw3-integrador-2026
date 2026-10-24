
package ar.edu.iua.iw3.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "codigo_externo",
        nullable = false,
        unique = true,
        length = 50
    )
    private String codigoExterno;

    @Column(
        name = "razon_social",
        nullable = false,
        length = 200
    )
    private String razonSocial;

    @Column(length = 255)
    private String contacto;
}
