package com.aegis.cardmanagement.repository;

import com.aegis.cardmanagement.modal.CardAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardActionRepository extends JpaRepository<CardAction, UUID> {
    List<CardAction> findAllByCardId(UUID cardId);
}
