package com.example.felveteli.services;

import com.example.felveteli.domain.Vote;
import com.example.felveteli.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VoteService {

    private final VoteRepository voteRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public void saveVote(Vote vote) {
        voteRepository.save(vote);
    }

    public Vote findVoteByVotingIdAndRepresentativeName(long votingId, String representativeName) {
        return voteRepository.findVoteByVotingIdAndRepresentativeName(votingId, representativeName);
    }

    public List<Vote> findVotesByVoting(long id) {
        return voteRepository.findVotesByVotingId(id);
    }
}
