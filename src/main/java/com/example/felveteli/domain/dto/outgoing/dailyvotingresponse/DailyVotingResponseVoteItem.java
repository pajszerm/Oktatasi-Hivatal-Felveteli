package com.example.felveteli.domain.dto.outgoing.dailyvotingresponse;

import com.example.felveteli.domain.enums.VoteOption;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DailyVotingResponseVoteItem {
    private String kepviselo;
    private VoteOption szavazat;
}
