package ru.otus.homework.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.homework.model.dto.ClientDto;
import ru.otus.homework.model.entity.Address;
import ru.otus.homework.model.entity.Client;
import ru.otus.homework.model.entity.Phone;
import ru.otus.homework.repository.ClientRepository;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client save(ClientDto clientDto) {
        var client = new Client(
                null,
                clientDto.name(),
                new Address(null, clientDto.street()),
                Set.of(new Phone(null, clientDto.phone(), null))
        );
        return clientRepository.save(client);
    }

}
