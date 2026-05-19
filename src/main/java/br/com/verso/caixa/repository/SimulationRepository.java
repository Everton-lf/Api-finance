package br.com.verso.caixa.repository;

import br.com.verso.caixa.entity.Simulation;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class SimulationRepository implements PanacheRepository<Simulation> {

    public Simulation findById(Long id) {
        return find("from Simulation s left join fetch s.memories where s.id = ?1", id).firstResult();
    }
    public List<Simulation> findAllWithMemories() {
        return find("select distinct s from Simulation s left join fetch s.memories").list();
    }
}

