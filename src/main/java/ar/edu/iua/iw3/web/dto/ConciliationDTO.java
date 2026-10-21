package ar.edu.iua.iw3.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConciliationDTO {
    private Long idOrden;
    private double tara;
    private double pesajeFinal;
    private double productoCargadoBalanza;
    private double masaCaudalimetro;
    private double diferencia;
    private double temperaturaFinal;
    private double densidadFinal;
}