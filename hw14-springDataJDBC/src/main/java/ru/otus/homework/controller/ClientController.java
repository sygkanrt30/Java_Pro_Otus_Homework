package ru.otus.homework.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.model.dto.ClientDto;
import ru.otus.homework.model.entity.Client;
import ru.otus.homework.service.ClientService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("${api.client.path}")
@AllArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody @Valid ClientDto clientDto) {
        Client savedClient = clientService.save(clientDto);
        return ResponseEntity.ok(savedClient);
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.findAll();
        return ResponseEntity.ok(clients);
    }
}
