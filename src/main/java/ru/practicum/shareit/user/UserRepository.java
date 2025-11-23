package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // проверка если email занят другим пользователем
    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByEmail(String email);

}
