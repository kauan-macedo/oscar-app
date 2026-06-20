package br.ufpr.oscar.repository;
 
import br.ufpr.oscar.entity.Diretor;
import org.springframework.data.jpa.repository.JpaRepository;
 
public interface DiretorRepository extends JpaRepository<Diretor, Long> {}