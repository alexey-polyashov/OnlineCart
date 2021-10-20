package ru.polyan.onlinecart.exception;

import java.util.List;
import java.util.stream.Collectors;

public class InvalidInputDataException extends RuntimeException{

    private List<String> messages;

    public List<String> getMessages() {
        return messages;
    }

    public InvalidInputDataException(List<String> messages) {
        super(messages.stream().collect(Collectors.joining(", ")));
        this.messages = messages;
    }

    public InvalidInputDataException(String message) {
        this(List.of(message));
    }

}
