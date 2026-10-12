package com.iot.repositories;

import com.iot.models.entities.AccessAttempt;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA access for {@link AccessAttempt}. Inherits standard CRUD operations like save,
 * findAll, etc.
 */
@Repository
public interface AccessRepository extends JpaRepository<AccessAttempt, Integer> {

  /**
   * Busca los intentos de acceso filtrando por su resultado (exitoso o fallido). Útil si en el
   * futuro quieres auditar cuántos accesos fueron rechazados.
   */
  List<AccessAttempt> findByExitoso(boolean exitoso);
}
