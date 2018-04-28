package com.hhuneau.asobi.game;

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

    public DefaultGameService(GameRepository gameRepository, MTGSetsService setService) {
        this.gameRepository = gameRepository;
        this.setService = setService;
    }

    public Game createGame(CreateGameEvent event) {
        final List<MTGSet> sets = setService.getSets(event.sets);
        final Game game = Game.of(event, sets);
        return gameRepository.save(game);
    }

    @Override
    public Optional<Game> getGame(long gameId) {
        return gameRepository.findById(gameId);
    }

    @Override
    public void startGame(Game game) {
        game.setStatus(STARTED);
        gameRepository.save(game);
    }

    public GameType getGameType(long gameId) {
        return gameRepository.getOne(gameId).getGameType();
    }
}
