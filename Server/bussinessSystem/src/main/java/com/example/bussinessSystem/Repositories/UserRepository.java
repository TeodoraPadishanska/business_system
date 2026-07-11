package com.example.bussinessSystem.Repositories;

import com.example.bussinessSystem.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}
