package com.hhuneau.asobi.mtg.game;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.mtg.player.Player;
import com.hhuneau.asobi.mtg.player.PlayerService;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.MTGSetsService;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.hhuneau.asobi.mtg.game.Status.FINISHED;
import static com.hhuneau.asobi.mtg.game.Status.STARTED;

@Service
@Transactional
public class DefaultGameService implements GameService {
    private final GameRepository gameRepository;
    private final MTGSetsService setService;

    public DefaultGameService(GameRepository gameRepository, MTGSetsService setService, PlayerService playerService, CustomerService customerService) {
        this.gameRepository = gameRepository;
        this.setService = setService;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Game> getGame(long gameId) {
        return gameRepository.findById(gameId);
    }

    @Override
    public CreateGameDTO createGame(CreateGameEvent evt) {
        final List<MTGSet> sets = setService.getSets(evt.sets);
        final String authToken = UUID.randomUUID().toString();
        final Game game = Game.of(evt.title, evt.seats, evt.isPrivate, evt.gameMode, evt.gameType, sets, authToken);
        final Game savedGame = gameRepository.save(game);
        return CreateGameDTO.of(savedGame.getGameId(), savedGame.getAuthToken());
    }

    @Override
    public void startGame(long gameId) {
        gameRepository.findById(gameId)
            .ifPresent(game -> {
                game.setStatus(STARTED);
                gameRepository.save(game);
            });
    }

    @Override
    public void finishGame(Game game) {
        game.setStatus(FINISHED);
        gameRepository.save(game);
    }

    @Override
    public void save(Game game) {
        gameRepository.save(game);
    }

    @Override
    public Player addPlayer(Game game, Player player) {
        game.getPlayers().add(player);
        gameRepository.save(game);
        return game.getPlayers().stream()
            .filter(player1 -> player1.getUserId().equals(player.getUserId()))
            .findFirst()
            .orElse(null);
    }
}