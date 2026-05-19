package br.com.verso.caixa.repository;

import br.com.verso.caixa.entity.Simulation;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SimulationRepository implements PanacheRepository<Simulation> {

    public Simulation findById(Long id) {
        return find("id", id).firstResult();
    }
}

