package com.example.felveteli.domain.dto.outgoing.votingresponse;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class VotingResponseError implements VotingResponse {
    List<String> errors = new ArrayList<>();

    public VotingResponseError(List<String> errors) {
        this.errors = errors;
    }
}
