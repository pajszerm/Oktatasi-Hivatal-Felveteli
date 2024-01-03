package com.example.felveteli.services;

import com.example.felveteli.domain.Representative;
import com.example.felveteli.domain.Vote;
import com.example.felveteli.domain.Voting;
import com.example.felveteli.domain.dto.incoming.CreateVoteDto;
import com.example.felveteli.domain.dto.incoming.CreateVotingDto;
import com.example.felveteli.domain.dto.outgoing.VoteResponse;
import com.example.felveteli.domain.dto.outgoing.VotingResultResponse;
import com.example.felveteli.domain.dto.outgoing.dailyvotingresponse.DailyVotingResponse;
import com.example.felveteli.domain.dto.outgoing.dailyvotingresponse.DailyVotingResponseItem;
import com.example.felveteli.domain.dto.outgoing.dailyvotingresponse.DailyVotingResponseVoteItem;
import com.example.felveteli.domain.enums.VoteOption;
import com.example.felveteli.domain.enums.VotingResult;
import com.example.felveteli.domain.enums.VotingType;
import com.example.felveteli.repositories.VotingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    public boolean checkVotingDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
        Voting voting = votingRepository.findVotingByDateTime(localDateTime);
        return voting != null;
    }

    public VoteResponse createVoteResponse(long votingId, String representativeName) {
        Vote vote = voteService.findVoteByVotingIdAndRepresentativeName(votingId, representativeName);
        if (vote != null) {
            VoteResponse voteResponse = new VoteResponse();
            voteResponse.setSzavazat(vote.getVoteOption());
            return voteResponse;
        }
        return null;
    }

    public VotingResultResponse createVotingResultResponse(long szavazas) {
        Voting voting = votingRepository.findVotingById(szavazas);
        if (voting == null) {
            return null;
        }
        if (voting.getVotingType() == VotingType.j) {
            return createVotingResultResponseTypeJ(voting);
        } else if (voting.getVotingType() == VotingType.e) {
            return createVotingResultResponseTypeE(voting);
        } else {
            return createVotingResultResponseTypeM(voting);
        }
    }

    private VotingResultResponse createVotingResultResponseTypeJ(Voting voting) {
        VotingResultResponse votingResultResponse = new VotingResultResponse();
        List<Vote> votes = voteService.findVotesByVoting(voting.getId());
        setVoteAndRepresentativeCounts(votingResultResponse, votes);
        votingResultResponse.setEredmeny(VotingResult.F);
        return votingResultResponse;
    }

    private VotingResultResponse createVotingResultResponseTypeE(Voting voting) {
        VotingType votingType = VotingType.j;
        Voting lastAttendanceVoting = votingRepository.findNextAttendanceVotingBeforeDateTime(voting.getDateTime(), votingType);
        int numberOfRepresentativesPresent = lastAttendanceVoting.getVotes().size();
        List<Vote> votes = voteService.findVotesByVoting(voting.getId());
        VotingResultResponse votingResultResponse = new VotingResultResponse();
        setVoteAndRepresentativeCounts(votingResultResponse, votes);
        setResult((numberOfRepresentativesPresent / 2), votingResultResponse);
        return votingResultResponse;
    }

    private VotingResultResponse createVotingResultResponseTypeM(Voting voting) {
        int numberOfAllRepresentatives = 200;
        List<Vote> votes = voteService.findVotesByVoting(voting.getId());
        VotingResultResponse votingResultResponse = new VotingResultResponse();
        setVoteAndRepresentativeCounts(votingResultResponse, votes);
        setResult(numberOfAllRepresentatives / 2, votingResultResponse);
        return votingResultResponse;
    }

    private static void setVoteAndRepresentativeCounts(
            VotingResultResponse votingResultResponse,
            List<Vote> votes
    ) {
        votingResultResponse.setIgenekSzama((int) votes.stream()
                .filter(vote -> vote.getVoteOption() == VoteOption.i)
                .count());
        votingResultResponse.setNemekSzama((int) votes.stream()
                .filter(vote -> vote.getVoteOption() == VoteOption.n)
                .count());
        votingResultResponse.setTartozkodasokSzama(((int) votes.stream()
                .filter(vote -> vote.getVoteOption() == VoteOption.t)
                .count()));
        votingResultResponse.setKepviselokSzama(votes.size());
    }

    private static void setResult(int halfOfRepresentatives, VotingResultResponse votingResultResponse) {
        if (halfOfRepresentatives < votingResultResponse.getIgenekSzama()) {
            votingResultResponse.setEredmeny(VotingResult.F);
        } else {
            votingResultResponse.setEredmeny(VotingResult.U);
        }
    }

    public DailyVotingResponse createDailyVotingResponse(String inputDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(inputDate, formatter);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
        List<Voting> votingsOnTheSameDay = votingRepository.findVotingsByDate(startOfDay, endOfDay);
        if (votingsOnTheSameDay.isEmpty()) {
            return null;
        }
        return createDailyVotingResponseItems(votingsOnTheSameDay);
    }

    private DailyVotingResponse createDailyVotingResponseItems(List<Voting> votingsOnTheSameDay) {
        DailyVotingResponse dailyVotingResponse = new DailyVotingResponse();
        for (Voting voting : votingsOnTheSameDay) {
            VotingResultResponse votingResultResponse = createVotingResultResponse(voting.getId());
            List<DailyVotingResponseVoteItem> voteItems = new ArrayList<>();
            for (Vote vote : voting.getVotes()) {
                DailyVotingResponseVoteItem voteItem = new DailyVotingResponseVoteItem();
                voteItem.setSzavazat(vote.getVoteOption());
                voteItem.setKepviselo(vote.getRepresentative().getName());
                voteItems.add(voteItem);
            }
            DailyVotingResponseItem dailyVotingResponseItem = new DailyVotingResponseItem();
            dailyVotingResponseItem.setIdopont(voting.getDateTime().toString());
            dailyVotingResponseItem.setTargy(voting.getSubject());
            dailyVotingResponseItem.setTipus(voting.getVotingType());
            dailyVotingResponseItem.setElnok(voting.getChairman().getName());
            dailyVotingResponseItem.setEredmeny(votingResultResponse.getEredmeny());
            dailyVotingResponseItem.setKepviselokSzama(voting.getVotes().size());
            dailyVotingResponseItem.setSzavazatok(voteItems);
            dailyVotingResponse.getSzavazasok().add(dailyVotingResponseItem);
        }
        return dailyVotingResponse;
    }
}
