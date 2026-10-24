
package ar.edu.iua.iw3.model.persistence;

import ar.edu.iua.iw3.model.Chofer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChoferRepository extends JpaRepository<Chofer, Long> {

    Optional<Chofer> findByCodigoExterno(String codigoExterno);

}
