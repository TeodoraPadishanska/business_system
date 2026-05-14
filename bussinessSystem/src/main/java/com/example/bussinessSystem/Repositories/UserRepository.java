package com.example.bussinessSystem.Repositories;

import com.example.bussinessSystem.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
