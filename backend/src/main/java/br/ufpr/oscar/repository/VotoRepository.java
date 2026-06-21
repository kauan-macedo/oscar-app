package br.ufpr.oscar.repository;
 
import br.ufpr.oscar.entity.Voto;
import br.ufpr.oscar.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
 
public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsByUsuario(Usuario usuario);
    Optional<Voto> findByUsuario(Usuario usuario);
}
