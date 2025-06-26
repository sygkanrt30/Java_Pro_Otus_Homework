package ru.otus.homework.hibernate.crm.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "address")
public class Address implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addresses_generator")
    @SequenceGenerator(name = "addresses_generator", sequenceName = "addresses_sequence", allocationSize = 1)
    @Column(name = "address_id")
    private Long addressId;

    private String street;

    public Address(String street) {
        this.street = street;
    }
    @Override
    public Address clone() {
        return new Address(addressId, street);
    }
}
