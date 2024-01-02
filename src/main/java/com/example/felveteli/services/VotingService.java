package com.example.felveteli.services;

import com.example.felveteli.domain.Representative;
import com.example.felveteli.domain.Vote;
import com.example.felveteli.domain.Voting;
import com.example.felveteli.domain.dto.incoming.CreateVoteDto;
import com.example.felveteli.domain.dto.incoming.CreateVotingDto;
import com.example.felveteli.repositories.VotingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
public class VotingService {

    private final VotingRepository votingRepository;

    private final VoteService voteService;

    private final RepresentativeService representativeService;

    @Autowired
    public VotingService(VotingRepository votingRepository, VoteService voteService, RepresentativeService representativeService) {
        this.votingRepository = votingRepository;
        this.voteService = voteService;
        this.representativeService = representativeService;
    }

    public String createAndSaveVoting(CreateVotingDto createVotingData) {
        representativeService.saveRepresentatives(createVotingData.getSzavazatok());
        Voting votingToSave = saveVoting(createVotingData);
        return Long.toString(votingToSave.getId());
    }

    private Voting saveVoting(CreateVotingDto createVotingData) {
        Voting votingToSave = createVoting(createVotingData);
        votingRepository.save(votingToSave);
        createAndSaveVotes(createVotingData, votingToSave);
        return votingToSave;
    }

    private void createAndSaveVotes(CreateVotingDto createVotingData, Voting votingToSave) {
        for (CreateVoteDto data : createVotingData.getSzavazatok()) {
            Vote vote = new Vote();
            vote.setRepresentative(representativeService.findRepresentativeByName(data.getKepviselo()));
            vote.setVoteOption(data.getSzavazat());
            vote.setVoting(votingToSave);
            voteService.saveVote(vote);
        }
    }

    private Voting createVoting(CreateVotingDto createVotingData) {
        Voting votingToSave = new Voting();
        Representative chairman = representativeService.findRepresentativeByName(createVotingData.getElnok());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(createVotingData.getIdopont(), formatter);
        votingToSave.setVotingType(createVotingData.getTipus());
        votingToSave.setSubject(createVotingData.getTargy());
        votingToSave.setDateTime(localDateTime);
        votingToSave.setChairman(chairman);
        return votingToSave;
    }

}
