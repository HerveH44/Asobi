package com.hhuneau.asobi.mtg.game;

public class AuthTokenDTO {
    public String authToken;
    public long gameId;

    public static AuthTokenDTO of(long gameId, String authToken) {
        final AuthTokenDTO dto = new AuthTokenDTO();
        dto.authToken = authToken;
        dto.gameId = gameId;
        return dto;
    }
}
