package ru.otus.homework.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.homework.model.entity.Client;

@Repository
public interface ClientRepository extends ListCrudRepository<Client, Long> {}
