package com.huydon.reknow.repository;

import com.huydon.reknow.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    Optional<Reminder> findByUserId (Long userId);

    List<Reminder> findByIsActiveTrue();
}
