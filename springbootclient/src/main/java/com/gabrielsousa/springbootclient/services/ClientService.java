package com.gabrielsousa.springbootclient.services;

import com.gabrielsousa.springbootclient.dto.ClientDTO;
import com.gabrielsousa.springbootclient.entities.Client;
import com.gabrielsousa.springbootclient.repositories.ClientRepository;
import com.gabrielsousa.springbootclient.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
        Page<Client> list = repository.findAll(pageRequest);
        return list.map(x -> new ClientDTO(x));
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        Optional<Client> entity = repository.findById(id);
        return new ClientDTO(entity.orElseThrow(() -> new ResourceNotFoundException("Id " + id + " not found")));
    }
}
