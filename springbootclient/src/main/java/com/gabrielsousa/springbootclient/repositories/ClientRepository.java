package com.gabrielsousa.springbootclient.repositories;

import com.gabrielsousa.springbootclient.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
