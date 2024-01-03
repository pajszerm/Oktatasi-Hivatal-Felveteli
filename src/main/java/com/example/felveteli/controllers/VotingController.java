package com.example.felveteli.controllers;

import com.example.felveteli.domain.dto.incoming.CreateVotingDto;
import com.example.felveteli.domain.dto.outgoing.RepresentativeAvgVotingResponse;
import com.example.felveteli.domain.dto.outgoing.VoteResponse;
import com.example.felveteli.domain.dto.outgoing.VotingResultResponse;
import com.example.felveteli.domain.dto.outgoing.dailyvotingresponse.DailyVotingResponse;
import com.example.felveteli.domain.dto.outgoing.votingresponse.VotingResponse;
import com.example.felveteli.domain.dto.outgoing.votingresponse.VotingResponseError;
import com.example.felveteli.domain.dto.outgoing.votingresponse.VotingResponseSuccess;
import com.example.felveteli.services.VotingService;
import com.example.felveteli.validators.CreateVotingDtoValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/szavazasok")
public class VotingController {

    private final VotingService votingService;

    private final CreateVotingDtoValidator createVotingDtoValidator;

    @Autowired
    public VotingController(VotingService votingService, CreateVotingDtoValidator createVotingDtoValidator) {
        this.votingService = votingService;
        this.createVotingDtoValidator = createVotingDtoValidator;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(createVotingDtoValidator);
    }

    @PostMapping("/szavazas")
    public ResponseEntity<VotingResponse> saveNewVoting(
            @Valid
            @RequestBody CreateVotingDto createVotingData,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getGlobalErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            return new ResponseEntity<>(new VotingResponseError(errors), HttpStatus.BAD_REQUEST);
        }
        String votingId = votingService.createAndSaveVoting(createVotingData);
        return new ResponseEntity<>(new VotingResponseSuccess(votingId), HttpStatus.OK);
    }

    @GetMapping("/szavazat/{szavazas}/{kepviselo}")
    public ResponseEntity<VoteResponse> getVoteOfRepresentative(
            @PathVariable("szavazas") long szavazas,
            @PathVariable("kepviselo") String kepviselo) {
        VoteResponse voteResponse = votingService.createVoteResponse(szavazas, kepviselo);
        if (voteResponse != null) {
            return new ResponseEntity<>(voteResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/eredmeny/{szavazas}")
    public ResponseEntity<VotingResultResponse> getResultOfVoting(@PathVariable long szavazas) {
        VotingResultResponse votingResultResponse = votingService.createVotingResultResponse(szavazas);
        if (votingResultResponse != null) {
            return new ResponseEntity<>(votingResultResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{napi-szavazasok}")
    public ResponseEntity<DailyVotingResponse> getVotingListByDay(
            @PathVariable("napi-szavazasok") String napiSzavazasok) {
        DailyVotingResponse dailyVotingResponse = votingService.createDailyVotingResponse(napiSzavazasok);
        if (dailyVotingResponse != null) {
            return new ResponseEntity<>(dailyVotingResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/kepviselo-reszvetel-atlag/{idoszak-kezdete}/{idoszak-vege}")
    public ResponseEntity<RepresentativeAvgVotingResponse> getRepresentativeAvgVoting(
            @PathVariable("idoszak-kezdete") String idoszakkezdete,
            @PathVariable("idoszak-vege") String idoszakVege) {
        RepresentativeAvgVotingResponse representativeAvgVotingResponse =
                votingService.createRepresentativeAvgVotingResponse(idoszakkezdete, idoszakVege);
        return new ResponseEntity<>(representativeAvgVotingResponse, HttpStatus.OK);
    }
}
