package com.hhuneau.asobi.mtg.game;

public class CreateGameDTO {
    public long gameId;
    public String authToken;

    public static CreateGameDTO of(long gameId, String authToken) {
        final CreateGameDTO dto = new CreateGameDTO();
        dto.gameId = gameId;
        dto.authToken = authToken;
        return dto;
    }
}