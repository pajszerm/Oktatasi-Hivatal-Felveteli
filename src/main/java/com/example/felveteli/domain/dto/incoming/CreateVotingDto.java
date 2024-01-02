package com.example.felveteli.domain.dto.incoming;

import com.example.felveteli.domain.enums.VotingType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CreateVotingDto {
    private String idopont;
    private String targy;
    private VotingType tipus;
    private String elnok;
    private List<CreateVoteDto> szavazatok;
}

