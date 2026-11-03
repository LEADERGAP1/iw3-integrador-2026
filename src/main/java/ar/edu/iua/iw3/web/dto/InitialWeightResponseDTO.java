package ar.edu.iua.iw3.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InitialWeightResponseDTO {

    private final String mensaje;
    private final Integer numeroOrden;
    private final int estado;
    private final String password;
}
