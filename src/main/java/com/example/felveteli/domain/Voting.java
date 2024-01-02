package com.example.felveteli.domain;

import com.example.felveteli.domain.enums.VotingType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "votings", schema = "DATA")
@Data
@NoArgsConstructor
public class Voting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime dateTime;
    private String subject;
    @Enumerated(EnumType.STRING)
    private VotingType votingType;
    @ManyToOne
    private Representative chairman;

    @OneToMany(mappedBy = "voting")
    private List<Vote> votes = new ArrayList<>();
}
