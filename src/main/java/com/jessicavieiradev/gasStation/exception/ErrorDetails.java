package com.jessicavieiradev.gasStation.exception;

import java.time.LocalDateTime;

public record ErrorDetails(
        int status,
        String message,
        LocalDateTime timestamp
) {}
