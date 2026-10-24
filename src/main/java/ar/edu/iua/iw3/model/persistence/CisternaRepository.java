
package ar.edu.iua.iw3.model.persistence;

import ar.edu.iua.iw3.model.Cisterna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CisternaRepository
        extends JpaRepository<Cisterna, Long> {

    List<Cisterna> findByCamionId(Long camionId);

    Optional<Cisterna> findByCamionIdAndNumero(
            Long camionId,
            String numero
    );
}
