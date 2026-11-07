package ar.edu.iua.iw3;

import ar.edu.iua.iw3.model.Order;
import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderSerializationTest {

    @Test
    void noDebeExponerLaClaveDeActivacion() throws Exception {

        Order order = new Order();

        order.setNumeroOrden(15040);
        order.setClaveActivacion("12345");
        order.setEstado(2);

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(order);

        assertFalse(json.contains("claveActivacion"));
        assertFalse(json.contains("\"password\""));
        assertFalse(json.contains("12345"));
    }
}
