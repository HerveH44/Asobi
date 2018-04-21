package com.hhuneau.asobi.game;

import com.hhuneau.asobi.game.pool.PoolService;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import com.hhuneau.asobi.websocket.events.JoinGameEvent;
import com.hhuneau.asobi.websocket.events.StartGameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class GameManager {

    private final GameService gameService;
    private final PoolService poolService;
    private final GameEngineFactory gameEngineFactory;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameManager.class);

    public GameManager(GameService gameService, PoolService poolService, GameEngineFactory gameEngineFactory) {
        this.gameService = gameService;
        this.poolService = poolService;
        this.gameEngineFactory = gameEngineFactory;
    }

    public long createGame(CreateGameEvent evt) {
        return gameService.createGame(evt).getGameId();
    }

    public void joinGame(WebSocketSession session, JoinGameEvent evt) {
        gameService.joinGame(session, evt);
    }

    public void startGame(StartGameEvent evt) {
        LOGGER.info("Starting game " + evt.gameId);
        final GameType gameType = gameService.getGameType(evt.gameId);
        final GameEngine engine = gameEngineFactory.getEngine(gameType);
        engine.start(evt.gameId);
        // engine start game => getSessionForPlayer => getPoolsForPlayer => SendPoolToPlayer
        // the engine, or something else, must be a component to ask the playerSessionService the WS
        // Or the Factory gives it to him, so each game has its own engine
    }
}
