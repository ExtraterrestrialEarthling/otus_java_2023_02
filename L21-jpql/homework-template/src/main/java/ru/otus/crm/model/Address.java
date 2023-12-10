package ru.otus.crm.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "addresses")
@AllArgsConstructor
@ToString
public class Address {

    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "address_gen", sequenceName = "address_seq",
            initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
    private String street;

    public Address(String street) {
        this.street = street;
    }

//    @OneToOne
//    @JoinColumn(name = "client_id")
//    private Client client;
}
