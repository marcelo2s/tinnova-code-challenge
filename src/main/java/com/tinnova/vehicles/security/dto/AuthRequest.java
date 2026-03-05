package com.tinnova.vehicles.security.dto;

public record AuthRequest(
        String username,
        String password
) {}
