package com.hhuneau.asobi.game.engine;

import com.hhuneau.asobi.game.Game;
import com.hhuneau.asobi.game.GameType;
import org.springframework.stereotype.Component;

import static com.hhuneau.asobi.game.GameType.DRAFT;

@Component
public class DraftEngine implements GameEngine {

    @Override
    public void start(Game game) {

    }

    @Override
    public boolean isInterested(GameType gameType) {
        return gameType == DRAFT;
    }
}
