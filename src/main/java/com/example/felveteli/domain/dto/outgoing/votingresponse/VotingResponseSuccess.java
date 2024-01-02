package com.example.felveteli.domain.dto.outgoing.votingresponse;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VotingResponseSuccess implements VotingResponse {
    private String szavazasId;

    public VotingResponseSuccess(String szavazasId) {
        this.szavazasId = szavazasId;
    }
}
