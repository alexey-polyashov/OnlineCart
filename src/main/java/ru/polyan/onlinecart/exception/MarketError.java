package ru.polyan.onlinecart.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class MarketError {
    private String message;
    private List<String> fieldErrors;
    private Date timestamp;

    public MarketError(String message) {
        this.message = message;
        this.timestamp = new Date();
    }
}
