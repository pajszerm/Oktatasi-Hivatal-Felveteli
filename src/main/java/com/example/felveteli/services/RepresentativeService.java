package com.example.felveteli.services;

import com.example.felveteli.domain.Representative;
import com.example.felveteli.domain.dto.incoming.CreateVoteDto;
import com.example.felveteli.repositories.RepresentativeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RepresentativeService {
    private final RepresentativeRepository representativeRepository;

    @Autowired
    public RepresentativeService(RepresentativeRepository representativeRepository) {
        this.representativeRepository = representativeRepository;
    }

    public void saveRepresentatives(List<CreateVoteDto> voteData) {
        List<Representative> representativesToSave = new ArrayList<>();
        for (CreateVoteDto data : voteData) {
            Representative representativeToSave = new Representative();
            representativeToSave.setName(data.getKepviselo());
            representativesToSave.add(representativeToSave);
        }
        for (Representative representative : representativesToSave) {
            if (findRepresentativeByName(representative.getName()) == null) {
                representativeRepository.save(representative);
            }
        }
    }

    public Representative findRepresentativeByName(String representativeName) {
        return representativeRepository.findRepresentativeByName(representativeName);
    }
}
