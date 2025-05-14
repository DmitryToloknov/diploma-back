package org.diploma.user.exception;

import java.util.List;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> methodArgumentNotValidException(MethodArgumentNotValidException e) {
    var errors = e.getBindingResult().getFieldErrors().stream()
      .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .findFirst()
        .orElse("");
    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<String> feignExceptionConflict(CustomException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
  }

}
