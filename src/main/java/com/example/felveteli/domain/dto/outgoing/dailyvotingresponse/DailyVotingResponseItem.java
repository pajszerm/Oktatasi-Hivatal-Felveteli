package com.example.felveteli.domain.dto.outgoing.dailyvotingresponse;

import com.example.felveteli.domain.enums.VotingResult;
import com.example.felveteli.domain.enums.VotingType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DailyVotingResponseItem {

    private String idopont;
    private String targy;
    private VotingType tipus;
    private String elnok;
    private VotingResult eredmeny;
    private int kepviselokSzama;
    private List<DailyVotingResponseVoteItem> szavazatok = new ArrayList<>();

}
