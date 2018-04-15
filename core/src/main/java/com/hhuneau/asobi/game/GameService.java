package com.hhuneau.asobi.game;

import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import com.hhuneau.asobi.websocket.events.JoinGameEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public GameService(GameRepository gameRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    public Game createGame(CreateGameEvent event) {
        final Game game = Game.of(event);
        return gameRepository.save(game);
    }

    public void joinGame(WebSocketSession session, JoinGameEvent evt) {
        final Game game = gameRepository.findOne(evt.gameId);
        //TODO: Gérer les exceptions (game introuvable ou commencée/finie)
        if (game == null) {
            throw new RuntimeException(String.format("Game %s not found", evt.gameId));
        }
        if (game.getStatus().hasStarted()) {
            throw new RuntimeException(String.format("Game %s already started", evt.gameId));
        }
        final Player player = new Player(session.getId(), game);
        playerRepository.save(player);
    }

    public Game getGame(long gameId) {
        return gameRepository.findOne(gameId);
    }
}
