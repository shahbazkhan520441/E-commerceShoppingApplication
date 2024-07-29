package com.ecommerce.shopping.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class FilterExceptionHandle {
    public static void handleJwtExpire(HttpServletResponse response,
                                int status,
                                String message,
                                String rootCause) throws IOException {
        response.setStatus(status);
        ErrorStructure<String> errorStructure = new ErrorStructure<String>()
                .setStatus(status)
                .setMessage(message)
                .setRootCause(rootCause);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), errorStructure);
//        test commit
    }
}
