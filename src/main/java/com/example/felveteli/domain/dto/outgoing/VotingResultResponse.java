package com.example.felveteli.domain.dto.outgoing;

import com.example.felveteli.domain.enums.VotingResult;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VotingResultResponse {
    @Enumerated(EnumType.STRING)
    private VotingResult eredmeny;
    private int kepviselokSzama;
    private int igenekSzama;
    private int nemekSzama;
    private int tartozkodasokSzama;
}
