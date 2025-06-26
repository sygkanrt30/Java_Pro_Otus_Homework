package ru.otus.homework.hibernate.crm.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clients_generator")
    @SequenceGenerator(name = "clients_generator", sequenceName = "clients_sequence", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "client")
    private List<Phone> phoneList;

    public Client(Long id, String name, Address address, List<Phone> phoneList) {
        this.id = id;
        this.name = name;
        this.address = address;
        phoneList.forEach(p -> p.setClient(this));
        this.phoneList = phoneList;
    }

    public void setPhoneList(List<Phone> phones) {
        phoneList.forEach(phone -> phone.setClient(this));
        this.phoneList = phones;
    }

    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    public Client clone() {
        Address cloneAddress = (address != null) ? address.clone() : new Address();
        List<Phone> clonedPhones =
                (phoneList != null) ? phoneList.stream().map(Phone::clone).toList() : new ArrayList<>();
        var newClient = new Client(id, name, cloneAddress, clonedPhones);
        newClient.setPhoneList(clonedPhones);
        return newClient;
    }
}
