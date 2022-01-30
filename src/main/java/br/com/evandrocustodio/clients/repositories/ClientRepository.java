package br.com.evandrocustodio.clients.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.evandrocustodio.clients.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}
