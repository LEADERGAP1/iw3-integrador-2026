package ar.edu.iua.iw3.model.persistence;

import ar.edu.iua.iw3.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Si necesitas alguna query custom, la agregas acá temporalmente
}