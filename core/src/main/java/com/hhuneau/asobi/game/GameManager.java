package com.hhuneau.asobi.game;

import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import com.hhuneau.asobi.websocket.events.StartGameEvent;
import com.hhuneau.asobi.websocket.events.server.SessionMessageEvent;
import com.hhuneau.asobi.websocket.messages.GameIdMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
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

    @EventListener
    public SessionMessageEvent onGameCreationRequest(CreateGameEvent event) {
        final long gameId = gameService.createGame(event).getGameId();
        final SessionMessageEvent sessionMessageEvent = new SessionMessageEvent();
        sessionMessageEvent.message = GameIdMessage.of(gameId);
        sessionMessageEvent.sessionId = event.sessionId;
        return sessionMessageEvent;
    }
}
