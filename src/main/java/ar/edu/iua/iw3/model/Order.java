package ar.edu.iua.iw3.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

// Esqueleto temporal para que tu código compile. 
// Cuando Giuliano termine, unirán esta clase con la suya.
@Entity
@Table(name = "ordenes")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int estado; // 1, 2, 3 o 4
    private String password;
    private double preset;
    private double tara;
    private double pesajeFinal;

    // Valores del caudalímetro
    private double ultimaMasaAcumulada;
    private double ultimaDensidad;
    private double ultimaTemperatura;
}