package com.hhuneau.asobi.websocket.messages;

import com.hhuneau.asobi.mtg.game.CreateGameDTO;

public class CreatedGameMessage extends Message {

    public static CreatedGameMessage of(CreateGameDTO dto) {
        final CreatedGameMessage o = new CreatedGameMessage();
        o.setType("GAME_ID");
        o.setPayload(dto);
        return o;
    }
}