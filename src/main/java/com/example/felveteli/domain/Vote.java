package com.example.felveteli.domain;

import com.example.felveteli.domain.enums.VoteOption;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vote", schema = "DATA")
@Data
@NoArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Representative representative;

    @Enumerated(EnumType.STRING)
    private VoteOption voteOption;

    @ManyToOne
    private Voting voting;
}
