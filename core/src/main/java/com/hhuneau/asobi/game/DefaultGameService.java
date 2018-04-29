package com.hhuneau.asobi.game;

import com.hhuneau.asobi.game.actions.creategame.CreateGameDTO;
import com.hhuneau.asobi.game.engine.GameEngine;
import com.hhuneau.asobi.game.engine.GameEngineFactory;
import com.hhuneau.asobi.game.player.Player;
import com.hhuneau.asobi.game.player.PlayerService;
import com.hhuneau.asobi.game.sets.MTGSet;
import com.hhuneau.asobi.game.sets.MTGSetsService;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.hhuneau.asobi.game.Status.STARTED;

@Service
@Transactional
public class DefaultGameService implements GameService {
    private final GameRepository gameRepository;
    private final MTGSetsService setService;
    private final GameEngineFactory gameEngineFactory;
    private final PlayerService playerService;

    public DefaultGameService(GameRepository gameRepository, MTGSetsService setService, GameEngineFactory gameEngineFactory, PlayerService playerService) {
        this.gameRepository = gameRepository;
        this.setService = setService;
        this.gameEngineFactory = gameEngineFactory;
        this.playerService = playerService;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Game> getGame(long gameId) {
        return gameRepository.findById(gameId);
    }

    @Override
    public CreateGameDTO createGame(CreateGameEvent evt) {
        final List<MTGSet> sets = setService.getSets(evt.sets);
        //TODO: create Token using strong hash?
        final String authToken = evt.sessionId;
        final Game game = Game.of(evt.title, evt.seats, evt.isPrivate, evt.gameMode, evt.gameType, sets, authToken);
        final Game savedGame = gameRepository.save(game);
        return CreateGameDTO.of(savedGame.getGameId(), savedGame.getAuthToken());
    }

    @Override
    public void startGame(long gameId, String authToken) {
        gameRepository.findByGameIdAndAuthToken(gameId, authToken).ifPresent(game -> {
            final GameType gameType = game.getGameType();
            final GameEngine engine = gameEngineFactory.getEngine(gameType);
            engine.start(game);
            game.setStatus(STARTED);
            gameRepository.save(game);
        });
    }

    @Override
    public boolean isPresent(long gameId) {
        return getGame(gameId).isPresent();
    }

    @Override
    public boolean hasStarted(long gameId) {
        final Optional<Game> game = getGame(gameId);
        if (!game.isPresent()) {
            throw new IllegalArgumentException(String.format("%s does not exist", gameId));
        }
        return game.get().getStatus().hasStarted();
    }

    @Override
    public boolean accept(long gameId, Player player) {
        final Optional<Game> optionalGame = getGame(gameId);
        if (!optionalGame.isPresent()) {
            throw new IllegalArgumentException(String.format("%s does not exist", gameId));
        }

        final Game game = optionalGame.get();
        if (game.getSeats() > game.getPlayers().size()) {
            player.setGame(game);
            final Player savedPlayer = playerService.save(player);
            game.getPlayers().add(savedPlayer);
            gameRepository.save(game);
            return true;
        } else {
            return false;
        }
    }
}
