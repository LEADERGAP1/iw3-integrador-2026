
package ar.edu.iua.iw3.model.persistence;

import ar.edu.iua.iw3.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository
        extends JpaRepository<Order, Long> {

    Optional<Order> findByNumeroOrden(Integer numeroOrden);

    boolean existsByNumeroOrden(Integer numeroOrden);
}
