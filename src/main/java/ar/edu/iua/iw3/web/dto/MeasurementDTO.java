package ar.edu.iua.iw3.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeasurementDTO {
    private double masa;
    private double densidad;
    private double temperatura;
    private double caudal; 
}