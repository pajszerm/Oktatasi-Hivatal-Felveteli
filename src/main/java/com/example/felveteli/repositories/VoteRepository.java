package com.example.felveteli.repositories;

import com.example.felveteli.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("SELECT v FROM Vote v WHERE v.voting.id = :id AND v.representative.name = :name")
    Vote findVoteByVotingIdAndRepresentativeName(@Param("id") long id, @Param("name") String name);

    @Query("SELECT v FROM Vote v WHERE v.voting.id = :id")
    List<Vote> findVotesByVotingId(@Param("id") long id);
}
