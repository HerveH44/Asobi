package com.hhuneau.asobi.mtg.eventhandler.started;

import com.hhuneau.asobi.mtg.game.Game;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.hhuneau.asobi.mtg.game.GameType.DRAFT;

@Component
@Transactional
public class DraftStartedEventHandler extends GameStartedEventHandler {
    @Override
    public boolean isInterested(Game game) {
        return super.isInterested(game) && game.getGameType().equals(DRAFT);
    }
}
