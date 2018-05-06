package com.hhuneau.asobi.websocket.messages;

import com.hhuneau.asobi.mtg.player.PlayerState;
import com.hhuneau.asobi.mtg.pool.Pack;
import com.hhuneau.asobi.mtg.sets.MTGCard;

import java.util.List;

public class PlayerStateMessage extends Message {
    public static PlayerStateMessage of(PlayerState state) {
        final PlayerStateMessage message = new PlayerStateMessage();
        message.setType("PLAYER_STATE");
        message.setPayload(StateDTO.of(state));
        return message;
    }

    public static class StateDTO {
        public List<MTGCard> pickedCards;
        public Pack waitingPack;

        public static StateDTO of(PlayerState state) {
            final StateDTO stateDTO = new StateDTO();
            stateDTO.pickedCards = state.getPickedCards();
            stateDTO.waitingPack = state.getWaitingPack();
            return stateDTO;
        }
    }
}
