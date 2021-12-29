package com.revature.project2backend.jsonmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

@NoArgsConstructor
@Data
public class JsonResponse {

    private String message;

    private boolean success;


    private Object data;

    private String redirect;

    private static final Logger logger = Logger.getLogger (JsonResponse.class);

    public JsonResponse (String message, boolean success, Object data, String redirect) {
        this.message = message;
        this.success = success;
        this.data = data;
        this.redirect = redirect;

        try {
            logger.log (success ? Level.INFO : Level.ERROR, "Response: " + new ObjectMapper().writeValueAsString (this));
        }

        catch (JsonProcessingException exception) {
            logger.error ("Error!", exception);
        }
    }

}
