package com.example.demo1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
