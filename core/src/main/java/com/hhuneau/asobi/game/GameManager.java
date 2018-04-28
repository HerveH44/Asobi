package com.hhuneau.asobi.game;

import com.hhuneau.asobi.game.engine.GameEngine;
import com.hhuneau.asobi.game.engine.GameEngineFactory;
import com.hhuneau.asobi.websocket.events.StartGameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GameManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameManager.class);
    private final DefaultGameService gameService;
    private final GameEngineFactory gameEngineFactory;

    public GameManager(DefaultGameService gameService, GameEngineFactory gameEngineFactory) {
        this.gameService = gameService;
        this.gameEngineFactory = gameEngineFactory;
    }

    public void startGame(StartGameEvent evt) {
        LOGGER.info("Starting game " + evt.gameId);
        final GameType gameType = gameService.getGameType(evt.gameId);
        final GameEngine engine = gameEngineFactory.getEngine(gameType);
        engine.start(evt.gameId);
    }

}
