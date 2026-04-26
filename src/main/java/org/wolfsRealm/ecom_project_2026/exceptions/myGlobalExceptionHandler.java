package org.wolfsRealm.ecom_project_2026.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.wolfsRealm.ecom_project_2026.payload.APIResponse;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class myGlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> response = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError) err).getField();
            String message =  err.getDefaultMessage();
            response.put(fieldName, message);
        });

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse>myResourceNotFoundException(ResourceNotFoundException e){
        String message= e.getMessage();
        APIResponse APIresponse= new APIResponse(message,false);
        return new ResponseEntity<>(APIresponse,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse>myAPIException(APIException e){
        String message= e.getMessage();
        APIResponse APIresponse= new APIResponse(message,false);
        return new ResponseEntity<>(APIresponse,HttpStatus.BAD_REQUEST);
    }


}

