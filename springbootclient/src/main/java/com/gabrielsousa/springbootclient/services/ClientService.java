package com.gabrielsousa.springbootclient.services;

import com.gabrielsousa.springbootclient.dto.ClientDTO;
import com.gabrielsousa.springbootclient.entities.Client;
import com.gabrielsousa.springbootclient.repositories.ClientRepository;
import com.gabrielsousa.springbootclient.services.exceptions.UniqueConstraintException;
import com.gabrielsousa.springbootclient.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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

    @Transactional
    public ClientDTO insert(ClientDTO dto) {
        try {
            Client entity = new Client();
            updateData(entity, dto);
            entity = repository.save(entity);
            return new ClientDTO(entity);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintException(dto.getCpf() + " already exists");
        }
    }

    @Transactional
    public ClientDTO update(Long id, ClientDTO dto) {
        try {
            Client entity = repository.getReferenceById(id);
            updateData(entity, dto);
            repository.save(entity);
            return new ClientDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id " + id + " not found to update");
        }
    }


    private void updateData(Client entity, ClientDTO dto) {
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());
    }
}
