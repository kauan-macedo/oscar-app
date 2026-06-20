package br.ufpr.oscar.repository;
 
import br.ufpr.oscar.entity.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
 
public interface FilmeRepository extends JpaRepository<Filme, Long> {}