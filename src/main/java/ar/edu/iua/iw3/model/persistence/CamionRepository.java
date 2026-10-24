
package ar.edu.iua.iw3.model.persistence;

import ar.edu.iua.iw3.model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CamionRepository extends JpaRepository<Camion, Long> {

    Optional<Camion> findByCodigoExterno(String codigoExterno);

}
