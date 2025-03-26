package org.example.repository;

import org.example.domain.Anime;
import org.example.domain.DevTavaresUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DevTavaresUserRepository extends JpaRepository<DevTavaresUser, Long> {
    DevTavaresUser findByUsername (String username);
}
