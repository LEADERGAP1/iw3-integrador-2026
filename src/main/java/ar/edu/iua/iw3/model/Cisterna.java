
package ar.edu.iua.iw3.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(
    name = "cisternas",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"camion_id", "numero"}
    )
)
@Getter
@Setter
public class Cisterna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String numero;

    @Column(
        name = "capacidad_litros",
        nullable = false,
        precision = 12,
        scale = 2
    )
    private BigDecimal capacidadLitros;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camion_id", nullable = false)
    private Camion camion;
}
