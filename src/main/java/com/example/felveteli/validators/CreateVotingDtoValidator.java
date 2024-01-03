package com.example.felveteli.validators;

import com.example.felveteli.domain.dto.incoming.CreateVoteDto;
import com.example.felveteli.domain.dto.incoming.CreateVotingDto;
import com.example.felveteli.services.VotingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.HashSet;
import java.util.List;

@Component
public class CreateVotingDtoValidator implements Validator {

    private VotingService votingService;

    @Autowired
    public CreateVotingDtoValidator(VotingService votingService) {
        this.votingService = votingService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateVotingDto.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {

        CreateVotingDto dto = (CreateVotingDto) o;

        try {
            JsonNode jsonSchema = JsonLoader.fromResource("/VotingSchema.json");
            JsonSchema schema = JsonSchemaFactory.byDefault().getJsonSchema(jsonSchema);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonData = objectMapper.valueToTree(dto);

            ProcessingReport report = schema.validate(jsonData);

            if (!report.isSuccess()) {
                errors.reject("json.validation.error", "JSON validation failed");
            }
        } catch (Exception e) {
            errors.reject("json.validation.error", "Error loading or validating JSON schema");
        }

        List<String> representatives = dto.getSzavazatok().stream().map(CreateVoteDto::getKepviselo).toList();
        if (!representatives.contains(dto.getElnok())) {
            errors.reject("chairman.no.vote", "The chairman has no vote");
        }

        if (votingService.checkVotingDateTime(dto.getIdopont())) {
            errors.reject("date.already.exists", "Voting with the same date already exists");
        }

        if (representatives.size() != new HashSet<>(representatives).size()) {
            errors.reject("multiple.votes.by.one", "Multiple votes by one representative");
        }
    }
}
