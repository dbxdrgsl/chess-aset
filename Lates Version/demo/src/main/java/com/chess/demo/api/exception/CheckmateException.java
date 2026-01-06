package com.chess.demo.api.exception;

public class CheckmateException extends RuntimeException {
    public CheckmateException() { super(); }
    public CheckmateException(String message) { super(message); }
    public CheckmateException(String message, Throwable cause) { super(message, cause); }
}