package com.hhuneau.asobi.game;

import com.hhuneau.asobi.game.sets.MTGSet;
import com.hhuneau.asobi.game.sets.MTGSetsService;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import com.hhuneau.asobi.websocket.events.JoinGameEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.hhuneau.asobi.game.Status.STARTED;

@Service
@Transactional
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private PlayerSessionService playerSessionService;
    private MTGSetsService setService;

    public GameService(GameRepository gameRepository, PlayerRepository playerRepository, PlayerSessionService playerSessionService, MTGSetsService setService) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.playerSessionService = playerSessionService;
        this.setService = setService;
    }

    @Transactional
    public Game createGame(CreateGameEvent event) {
        final List<MTGSet> sets = event.sets.stream().map(s -> {
            return setService.getSet(s);
        }).collect(Collectors.toList());
        final Game game = Game.of(event, sets);
        return gameRepository.save(game);
    }

    public void joinGame(WebSocketSession session, JoinGameEvent evt) {
        final Game game = gameRepository.findOne(evt.gameId);
        //TODO: Gérer les exceptions (game introuvable ou commencée/finie)
        if (game == null) {
            throw new RuntimeException(String.format("Game %s not found", evt.gameId));
        }
        if (game.getStatus().hasStarted()) {
            throw new IllegalStateException(String.format("Game %s already started", evt.gameId));
        }

        //Get player from userId if exists. If not create a new player
        final Player player = game.getPlayers().stream()
            .filter(p -> evt.id.equals(p.getUserId()))
            .findFirst()
            .orElseGet(() -> playerRepository.save(Player.of(evt.id, evt.name, game)));

        //Connect player with session
        playerSessionService.add(player, session);
    }

    public Game getGame(long gameId) {
        return gameRepository.findOne(gameId);
    }

    @Transactional
    public void startGame(Game game) {
        game.setStatus(STARTED);
        gameRepository.save(game);
    }

    public GameMode getGameMode(long gameId) {
        return gameRepository.getOne(gameId).getGameMode();
    }

    public GameType getGameType(long gameId) {
        return gameRepository.getOne(gameId).getGameType();
    }
}
