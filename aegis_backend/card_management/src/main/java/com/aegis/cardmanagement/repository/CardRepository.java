package com.aegis.cardmanagement.repository;

import com.aegis.cardmanagement.modal.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {

    List<Card> findByHardwareId(String hardwareId);

    @Query("SELECT P FROM Card P WHERE P.isDeleted=false and P.id=:cardId")
    Optional<Card> findById(@Param("cardId") UUID cardId);


    @Query("SELECT P FROM Card P WHERE P.isDeleted=false ")
    List<Card> findAll();

    @Modifying
    @Query("UPDATE Card c SET c.isDeleted=true WHERE c.id=:cardId")
    void deleteById(@Param("cardId") UUID cardId);

}
