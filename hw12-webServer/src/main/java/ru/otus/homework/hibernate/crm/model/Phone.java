package ru.otus.homework.hibernate.crm.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "phone")
public class Phone implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phones_generator")
    @SequenceGenerator(name = "phones_generator", sequenceName = "phones_sequence", allocationSize = 1)
    @Column(name = "phone_id")
    private Long phoneId;

    private String number;

    @ManyToOne()
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(foreignKeyDefinition = ""))
    private Client client;

    public Phone(Long id, String number) {
        this.phoneId = id;
        this.number = number;
    }

    public Phone(String number) {
        this.number = number;
    }

    @Override
    public Phone clone() {
        return new Phone(phoneId, number);
    }
}
