package com.hhuneau.asobi.game;

import com.hhuneau.asobi.game.pool.PoolService;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import com.hhuneau.asobi.websocket.events.JoinGameEvent;
import com.hhuneau.asobi.websocket.events.StartGameEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

@Service
public class GameManager {

    private final GameService gameService;
    private PoolService poolService;

    public GameManager(GameService gameService, PoolService poolService) {
        this.gameService = gameService;
        this.poolService = poolService;
    }

    public long createGame(CreateGameEvent evt) {
        return gameService.createGame(evt).getGameId();
    }

    public void joinGame(WebSocketSession session, JoinGameEvent evt) {
        gameService.joinGame(session, evt);
    }

    public void startGame(StartGameEvent evt) {
        final Game game = gameService.getGame(evt.gameId);
        final Set<Player> players = game.getPlayers();
        poolService.createPools(players, game);
    }
}
