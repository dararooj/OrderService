package com.example.OrderService.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomException extends RuntimeException{
    private String errorCode;
    private int status;
    public CustomException(String message,String errorCode,int status){
    super(message);
    this.errorCode=errorCode;
    this.status=status;
    }
}
