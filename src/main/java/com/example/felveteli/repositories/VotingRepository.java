package com.example.felveteli.repositories;

import com.example.felveteli.domain.Voting;
import com.example.felveteli.domain.enums.VotingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VotingRepository extends JpaRepository<Voting, Long> {
    @Query("SELECT v FROM Voting v WHERE v.dateTime = :localDateTime")
    Voting findVotingByDateTime(@Param("localDateTime") LocalDateTime localDateTime);

    @Query("SELECT v FROM Voting v WHERE v.id =:id")
    Voting findVotingById(@Param("id") long id);

    @Query("SELECT v FROM  Voting  v WHERE" +
            " v.dateTime < :dateTime AND v.votingType = :votingType" +
            " ORDER BY v.dateTime DESC LIMIT 1")
    Voting findNextAttendanceVotingBeforeDateTime(@Param("dateTime") LocalDateTime dateTime,
                                                  @Param("votingType") VotingType votingType);

    @Query("SELECT v FROM Voting v WHERE v.dateTime >= :startOfDay AND v.dateTime <= :endOfDay")
    List<Voting> findVotingsByDate(@Param("startOfDay") LocalDateTime startOfDay,
                                   @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT v FROM Voting v WHERE" +
            " v.votingType != :votingType AND " +
            "(v.dateTime >= :startDateTime AND v.dateTime <= :endDateTime)")
    List<Voting> findVotingsInPeriod(@Param("startDateTime") LocalDateTime startDateTime,
                                     @Param("endDateTime") LocalDateTime endDateTime,
                                     @Param("votingType") VotingType votingType);

}
