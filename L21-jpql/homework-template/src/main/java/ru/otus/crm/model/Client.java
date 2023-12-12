package ru.otus.crm.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "clients")
public class Client implements Cloneable {

    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq",
            initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Phone> phoneNumbers;

    public Client(Long id, String name, Address address, List<Phone> phoneNumbers) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumbers = phoneNumbers;
        for (Phone phone : phoneNumbers){
            phone.setClient(this);
        }
    }

    public void setPhoneNumbers(List<Phone> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
        for (Phone phone : phoneNumbers){
            phone.setClient(this);
        }
    }

    public Client(String name, Address address, List<Phone> phoneNumbers) {
        this.name = name;
        this.address = address;
        this.phoneNumbers = phoneNumbers;
        for (Phone phone : phoneNumbers){
            phone.setClient(this);
        }
    }

    public Client(String name) {
        this.name = name;
    }

    @Override
    public Client clone() {
        Client clientClone = new Client(this.id, this.name, this.address, new ArrayList<>(this.phoneNumbers));
        for (Phone phone : clientClone.getPhoneNumbers()){
            phone.setClient(clientClone);
        }
        return clientClone;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", phoneNumbers=" + phoneNumbers +
                '}';
    }
}
