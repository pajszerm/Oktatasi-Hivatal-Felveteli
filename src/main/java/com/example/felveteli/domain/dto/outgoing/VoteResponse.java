package com.example.felveteli.domain.dto.outgoing;

import com.example.felveteli.domain.enums.VoteOption;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoteResponse {
    private VoteOption szavazat;
}
