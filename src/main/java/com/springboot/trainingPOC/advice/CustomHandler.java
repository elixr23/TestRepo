package com.springboot.trainingPOC.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.NoSuchFileException;

@ControllerAdvice
public class CustomHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Response response = new Response("False", "File can not be empty. Please attach a .txt file.");
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Response> handleMismatch(MethodArgumentTypeMismatchException ex) {
        Response response = new Response("False", "The given ID is invalid." +
                "Please provide correct UUID.");
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Response response = new Response("False", "Username is mandatory." +
                "Please provide userName.");
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFileFormatException.class)
    protected ResponseEntity<Response> handleInvalidFileFormat(InvalidFileFormatException ex) {
        Response response = new Response("False", ex.getMessage());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyInputException.class)
    protected ResponseEntity<Response> handleEmptyInput(EmptyInputException ex) {
        Response response = new Response("False", ex.getMessage());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyOutputException.class)
    protected ResponseEntity<Response> handleEmptyOutput(EmptyOutputException ex) {
        Response response = new Response("False", ex.getMessage());
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoFileFoundException.class)
    protected ResponseEntity<Response> handleNoFileFound(NoFileFoundException ex) {
        Response response = new Response("False", ex.getMessage());
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(NoSuchFileException.class)
    protected ResponseEntity<Response> handleNoSuchFileException(NoSuchFileException ex){
        Response response = new Response("False", ex.getMessage());
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }



   /* @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<String>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + " -> " + error.getDefaultMessage());
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + " -> " + error.getDefaultMessage());
        }

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), errors);
        //return handleExceptionInternal(ex, errorResponse, headers, errorResponse.getStatus(), request);

        // If you want to throw apiError directly, uncomment this
        return new ResponseEntity(errorResponse, errorResponse.getStatus());

    }*/


}
