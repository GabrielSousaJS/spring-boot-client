package com.gabrielsousa.springbootclient.services;

import com.gabrielsousa.springbootclient.dto.ClientDTO;
import com.gabrielsousa.springbootclient.entities.Client;
import com.gabrielsousa.springbootclient.repositories.ClientRepository;
import com.gabrielsousa.springbootclient.services.exceptions.DatabaseException;
import com.gabrielsousa.springbootclient.services.exceptions.ResourceNotFoundException;
import com.gabrielsousa.springbootclient.services.exceptions.UniqueConstraintException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAllPaged(Pageable pageable) {
        Page<Client> list = repository.findAll(pageable);
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

    public void deleteById(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id " + id + " not found delete");
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
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
