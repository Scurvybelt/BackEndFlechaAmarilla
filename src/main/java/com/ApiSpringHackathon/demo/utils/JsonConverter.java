package com.ApiSpringHackathon.demo.utils;

import Utils.Responses.Stamp.SuccessV1Response;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {
    public <T> T convertJsonStringToObject(String jsonString, Class<T> clazz) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, clazz);
    }
}