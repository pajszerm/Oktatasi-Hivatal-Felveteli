package com.example.felveteli.validators;

import com.example.felveteli.domain.dto.incoming.CreateVotingDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CreateVotingDtoValidator implements Validator {
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
    }
}
