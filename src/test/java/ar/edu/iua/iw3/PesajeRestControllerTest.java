package ar.edu.iua.iw3;

import ar.edu.iua.iw3.business.IPesajeBusiness;
import ar.edu.iua.iw3.model.Order;
import ar.edu.iua.iw3.web.controller.PesajeRestController;
import ar.edu.iua.iw3.web.dto.InitialWeightDTO;
import ar.edu.iua.iw3.web.dto.InitialWeightResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PesajeRestControllerTest {

    @Test
    void respuestaDePesajeInicialTipadaIncluyePassword() throws Exception {
        IPesajeBusiness pesajeBusiness = mock(IPesajeBusiness.class);
        Order order = new Order();
        order.setNumeroOrden(15040);
        order.setEstado(2);
        order.setClaveActivacion("12345");
        when(pesajeBusiness.registerInitialWeight(
                org.mockito.ArgumentMatchers.any(InitialWeightDTO.class)))
                .thenReturn(order);

        PesajeRestController controller = new PesajeRestController(pesajeBusiness);
        ResponseEntity<?> response = controller.registerInitialWeight(
                new InitialWeightDTO());

        InitialWeightResponseDTO body = assertInstanceOf(
                InitialWeightResponseDTO.class, response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals("12345", body.getPassword());
        assertTrue(new ObjectMapper().writeValueAsString(body)
                .contains("\"password\":\"12345\""));
    }
}
