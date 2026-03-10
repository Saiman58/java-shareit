package ru.practicum.server.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByItemId(Long itemId);

    List<Comment> findByItemIdIn(List<Long> itemIds);

}
