package com.hhuneau.asobi.game;

import com.hhuneau.asobi.game.sets.MTGSet;
import com.hhuneau.asobi.game.sets.MTGSetsService;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hhuneau.asobi.game.Status.STARTED;

@Service
@Transactional
public class DefaultGameService implements GameService {
    private final GameRepository gameRepository;
    private final MTGSetsService setService;

    public DefaultGameService(GameRepository gameRepository, MTGSetsService setService) {
        this.gameRepository = gameRepository;
        this.setService = setService;
    }

    public Game createGame(CreateGameEvent event) {
        final List<MTGSet> sets = event.sets.stream().map(this::apply).collect(Collectors.toList());
        final Game game = Game.of(event, sets);
        return gameRepository.save(game);
    }

    // TODO: upgrade to SpringBoot 2 and use JPA native methods
    @Override
    public Optional<Game> getGame(long gameId) {
        final Game game = gameRepository.findOne(gameId);
        return game != null ? Optional.of(game) : Optional.empty();
    }

    public void startGame(Game game) {
        game.setStatus(STARTED);
        gameRepository.save(game);
    }

    public GameType getGameType(long gameId) {
        return gameRepository.getOne(gameId).getGameType();
    }

    private MTGSet apply(String s) {
        return setService.getSet(s);
    }
}
