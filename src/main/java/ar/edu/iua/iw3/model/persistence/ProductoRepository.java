
package ar.edu.iua.iw3.model.persistence;

import ar.edu.iua.iw3.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigoExterno(String codigoExterno);

}
