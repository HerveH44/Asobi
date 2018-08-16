package com.hhuneau.asobi.websocket.messages;

import com.hhuneau.asobi.mtg.game.AuthTokenDTO;

public class AuthTokenMessage extends Message {

    public static AuthTokenMessage of(AuthTokenDTO dto) {
        final AuthTokenMessage o = new AuthTokenMessage();
        o.setType("AUTH_TOKEN");
        o.setPayload(dto);
        return o;
    }
}
