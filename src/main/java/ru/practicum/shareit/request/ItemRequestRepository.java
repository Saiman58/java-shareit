package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    //Найти все запросы пользователя, отсортированные по дате создания (новые сверху)
    List<ItemRequest> findByRequesterIdOrderByCreatedDesc(Long requesterId);

    //Найти все запросы, созданные не указанным пользователем
    //Используется в: GET /requests/all

    Page<ItemRequest> findByRequesterIdNot(Long requesterId, Pageable pageable);

    @Query("SELECT DISTINCT ir FROM ItemRequest ir " +
            "LEFT JOIN FETCH ir.items i " +
            "LEFT JOIN FETCH i.owner " +
            "WHERE ir.id = :requestId")
    Optional<ItemRequest> findByIdWithItems(@Param("requestId") Long requestId);

}
