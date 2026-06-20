package br.ufpr.oscar.repository;
 
import br.ufpr.oscar.entity.Sessao;
import br.ufpr.oscar.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
 
public interface SessaoRepository extends JpaRepository<Sessao, Long> {
    Optional<Sessao> findByUsuario(Usuario usuario);
    Optional<Sessao> findByToken(Integer token);
}