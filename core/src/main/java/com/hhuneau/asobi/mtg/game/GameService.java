package com.hhuneau.asobi.mtg.game;

import com.hhuneau.asobi.mtg.player.Player;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import com.hhuneau.asobi.websocket.events.game.StartGameEvent;

import java.util.List;
import java.util.Optional;

public interface GameService {

    Optional<Game> getGame(long gameId);

    CreateGameDTO createGame(CreateGameEvent evt);

    Player addPlayer(Game game, Player player);

    void startGame(StartGameEvent evt);

    void finishGame(Game game);

    void save(Game game);

    Player getNextPlayer(Game game, Player player);

    boolean isRoundFinished(Game game);

    void startNewRound(Game game);

    List<Game> getAllCurrentGames();

    void pick(Game game, Player player, String cardI);
}
