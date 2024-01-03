package com.example.felveteli.repositories;

import com.example.felveteli.domain.Voting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface VotingRepository extends JpaRepository<Voting, Long> {

    @Query("SELECT v FROM Voting v WHERE v.dateTime = :localDateTime")
    Voting findVotingByDateTime(@Param("localDateTime") LocalDateTime localDateTime);
}
