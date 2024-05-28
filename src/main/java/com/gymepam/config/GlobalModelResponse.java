package com.gymepam.config;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class GlobalModelResponse implements Serializable {
    private Object response;
    private String message;
    private Map<String, Object> additionalData;
}
