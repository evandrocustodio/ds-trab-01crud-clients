package br.com.evandrocustodio.clients.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;	

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.evandrocustodio.clients.entities.Client;
import br.com.evandrocustodio.clients.entities.ClientDTO;
import br.com.evandrocustodio.clients.exceptions.DatabaseException;
import br.com.evandrocustodio.clients.exceptions.ResourceNotFoundException;
import br.com.evandrocustodio.clients.repositories.ClientRepository;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
		Page<Client> clients = clientRepository.findAll(pageRequest);
		return clients.map(c -> new ClientDTO(c));
	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> client = clientRepository.findById(id);

		Client entity = client.orElseThrow(() -> new ResourceNotFoundException("Entity not Found"));

		return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client entity = new Client(dto);
		entity = clientRepository.save(entity);
		return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		Client entity;
		try {
			entity = clientRepository.getById(id);
			entity.setName(dto.getName());
			entity.setCpf(dto.getCpf());
			entity.setBirthDate(dto.getBirthDate());
			entity.setIncome(dto.getIncome());
			entity.setChildren(dto.getChildren());
			entity = clientRepository.save(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id Not Found: " + id);
		}
		return new ClientDTO(entity);
	}

	public void delete(Long id) {
		try {
			clientRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id Not Found: " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity Violation");
		}
	}
}
