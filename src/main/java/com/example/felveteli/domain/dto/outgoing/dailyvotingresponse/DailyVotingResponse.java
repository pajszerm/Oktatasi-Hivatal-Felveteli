package com.example.felveteli.domain.dto.outgoing.dailyvotingresponse;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DailyVotingResponse {
    private List<DailyVotingResponseItem> szavazasok = new ArrayList<>();
}
