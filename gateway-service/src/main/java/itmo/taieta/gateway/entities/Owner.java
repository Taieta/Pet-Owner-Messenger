package itmo.taieta.gateway.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Role;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class Owner {
    private Long id;
    private String name;
    private LocalDate birthDate;

    private String username;
    private String password;
    private Role role;
}
