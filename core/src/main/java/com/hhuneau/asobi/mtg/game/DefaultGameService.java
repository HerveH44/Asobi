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
    private final PlayerService playerService;
    private final CustomerService customerService;

    public DefaultGameService(GameRepository gameRepository, MTGSetsService setService, PlayerService playerService, CustomerService customerService) {
        this.gameRepository = gameRepository;
        this.setService = setService;
        this.playerService = playerService;
        this.customerService = customerService;
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
    public boolean canStart(Game game, String authToken) {
        return game.getAuthToken().equals(authToken) && !game.getStatus().hasStarted();
    }

    @Override
    public void broadcastState(Game game) {
//        final Map<String, Integer> gameState = mtg.getPlayers()
//            .stream()
//            .collect(Collectors.toMap(Player::getName, player -> player.getRemainingPacks().size()));
//        mtg.getPlayers().forEach(player -> {
//            customerService.send(player.getUserId(), GameStateMessage.of(gameState));
//        });
    }

    @Override
    public void save(Game game) {
        gameRepository.save(game);
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
    public Player addPlayer(Game game, Player player) {
        final Player savedPlayer = playerService.save(player);
        game.getPlayers().add(savedPlayer);
        gameRepository.save(game);
        return savedPlayer;
    }
}
