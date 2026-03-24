package itmo.taieta.gateway.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import models.Role;

import java.time.LocalDate;

@Data
@Entity
@Table(name="owners")
@AllArgsConstructor
public class OwnerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NonNull
    private String name;
    @Column(nullable = false)
    @NonNull
    private LocalDate birthDate;

    @NonNull
    private String username;
    @NonNull
    private String password;
    @Enumerated(EnumType.STRING)
    @NonNull
    private Role role;

    public OwnerModel() { }
}
