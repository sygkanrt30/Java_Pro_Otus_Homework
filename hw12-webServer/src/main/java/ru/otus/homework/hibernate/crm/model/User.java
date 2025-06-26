package ru.otus.homework.hibernate.crm.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_generator")
    @SequenceGenerator(name = "users_generator", sequenceName = "users_sequence", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    private String name;

    private String password;

    @Override
    public User clone() {
        return new User(id, name, password);
    }
}
