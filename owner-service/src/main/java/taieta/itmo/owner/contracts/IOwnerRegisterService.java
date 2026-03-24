package taieta.itmo.owner.contracts;

import taieta.itmo.owner.entities.Owner;

import java.time.LocalDate;

public interface IOwnerRegisterService {
    Owner register(String name, LocalDate birthday, String username, String password);
}
