
package ar.edu.iua.iw3.model.persistence;

import ar.edu.iua.iw3.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByCodigoExterno(String codigoExterno);

}
