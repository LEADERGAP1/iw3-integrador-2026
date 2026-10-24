
package ar.edu.iua.iw3.model.persistence;

import ar.edu.iua.iw3.model.HistorialEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialEstadoRepository
        extends JpaRepository<HistorialEstado, Long> {

    List<HistorialEstado> findByOrdenIdOrderByFechaCambioAsc(
            Long ordenId
    );
}
