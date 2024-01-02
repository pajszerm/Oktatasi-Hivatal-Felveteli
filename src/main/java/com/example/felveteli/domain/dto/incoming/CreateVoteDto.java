package com.example.felveteli.domain.dto.incoming;

import com.example.felveteli.domain.enums.VoteOption;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateVoteDto {
    private String kepviselo;
    private VoteOption szavazat;
}
