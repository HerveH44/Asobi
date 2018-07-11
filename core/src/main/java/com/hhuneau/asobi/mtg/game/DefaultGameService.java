package com.hhuneau.asobi.mtg.game;

import com.hhuneau.asobi.mtg.player.Player;
import com.hhuneau.asobi.mtg.player.PlayerState;
import com.hhuneau.asobi.mtg.pool.Booster;
import com.hhuneau.asobi.mtg.pool.Pack;
import com.hhuneau.asobi.mtg.pool.PoolService;
import com.hhuneau.asobi.mtg.sets.MTGCard;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.MTGSetsService;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final PoolService poolService;

    public DefaultGameService(GameRepository gameRepository, MTGSetsService setService, PoolService poolService) {
        this.gameRepository = gameRepository;
        this.setService = setService;
        this.poolService = poolService;
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
    public Player getNextPlayer(Game game, Player player) {
        final Player[] players = game.getPlayers().toArray(new Player[0]);
        for (int i = 0; i < players.length; i++) {
            final Player p = players[i];

            // Get Player index
            if (p.getPlayerId() == player.getPlayerId()) {

                // Get next player according to round
                final int round = game.getRound();
                final int calculatedIndex = round % 2 == 0 ? i + 1 : i - 1;
                int nextPlayerSeat = calculatedIndex % players.length;

                // If the index is negative, we cycle from end of the list
                if (nextPlayerSeat < 0) {
                    nextPlayerSeat += players.length;
                }
                return players[nextPlayerSeat];
            }
        }

        //Should never happened...
        return player;
    }

    @Override
    public boolean isRoundFinished(Game game) {
        return game.getPlayers().stream()
                   .map(Player::getPlayerState)
                   .map(PlayerState::getWaitingPacks)
                   .allMatch(List::isEmpty);
    }

    @Override
    public void startNewRound(Game game) {
        game.setRound(game.getRound() + 1);

        game.getPlayers().forEach(player -> {
            final List<Booster> pool = player.getPool();
            if (!pool.isEmpty()) {
                //put the first booster as available
                final Pack firstPack = new Pack();
                final Booster booster = pool.remove(0);
                final List<MTGCard> poolCards = booster.getCards();
                firstPack.setCards(new ArrayList<>(poolCards));
                player.getPlayerState().getWaitingPacks().add(firstPack);
                poolService.delete(booster);
            }
        });

        save(game);
    }

    @Override
    public Player addPlayer(Game game, Player player) {
        game.getPlayers().add(player);
        gameRepository.save(game);
        return game.getPlayers().stream()
                   .filter(p -> p.getUserId().equals(player.getUserId()))
                   .findFirst()
                   .orElse(null);
    }
}
