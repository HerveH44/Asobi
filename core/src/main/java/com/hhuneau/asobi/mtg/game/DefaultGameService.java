package com.hhuneau.asobi.mtg.game;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.mtg.player.Player;
import com.hhuneau.asobi.mtg.player.PlayerState;
import com.hhuneau.asobi.mtg.pool.Booster;
import com.hhuneau.asobi.mtg.pool.Pack;
import com.hhuneau.asobi.mtg.pool.PoolService;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.MTGSetsService;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import com.hhuneau.asobi.websocket.events.game.StartGameEvent;
import com.hhuneau.asobi.websocket.messages.PackMessage;
import com.hhuneau.asobi.websocket.messages.PickMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.hhuneau.asobi.mtg.game.Status.FINISHED;
import static com.hhuneau.asobi.mtg.game.Status.STARTED;

@Service
@Transactional
public class DefaultGameService implements GameService {

    private final GameRepository gameRepository;
    private final MTGSetsService setService;
    private final PoolService poolService;
    private final CustomerService customerService;

    public DefaultGameService(GameRepository gameRepository, MTGSetsService setService, PoolService poolService, CustomerService customerService) {
        this.gameRepository = gameRepository;
        this.setService = setService;
        this.poolService = poolService;
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
        final Game game = Game.of(evt.title, evt.seats, evt.isPrivate, evt.gameMode, evt.gameType, sets, authToken, evt.sessionId);
        final Game savedGame = gameRepository.save(game);
        return CreateGameDTO.of(savedGame.getGameId(), savedGame.getAuthToken());
    }

    @Override
    public void startGame(StartGameEvent evt) {
        gameRepository.findById(evt.gameId)
            .ifPresent(game -> {
                game.setStatus(STARTED);
                game.setUseTimer(evt.useTimer);
                game.setTimer(evt.timerLength);

                if (evt.addBots) {
                    final long emptySeats = game.getSeats() - game.getPlayers().size();
                    for (int i = 0; i < emptySeats; i++) {
                        final Player player = Player.of("", "bot" + i, true);
                        addPlayer(game, player);
                    }
                }

                if (evt.shufflePlayers) {
                    Collections.shuffle(game.getPlayers());
                    for (int i = 0; i < game.getPlayers().size(); i++) {
                        final Player player = game.getPlayers().get(i);
                        player.setSeat(i);
                    }
                }

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
                final PlayerState playerState = player.getPlayerState();
                playerState.getWaitingPacks().add(firstPack);
                final int pick = 1;

                if (game.useTimer) {
                    playerState.setTimeLeft(TimeProducer.calc(game.getTimer(), pick));
                }

                playerState.setPick(pick);
                playerState.setPack(game.getRound());
                poolService.delete(booster);

                final String userId = player.getUserId();
                if (userId != null && !userId.equals("")) {
                    customerService.send(userId, PackMessage.of(firstPack.getCards()));
                }
            }
        });

        // Let the bots play
        game.getPlayers().stream()
            .filter(Player::isBot)
            .forEach(bot -> pickAsBot(game, bot));

        save(game);
    }

    private void pickAsBot(Game game, Player botPlayer) {
        final PlayerState botState = botPlayer.getPlayerState();

        while (botState.hasWaitingPack()) {
            final Pack pack = botState.getWaitingPack();
            final MTGCard pickedCard = pack.getCards().get(new Random().nextInt(pack.getCards().size()));
            pack.getCards().remove(pickedCard);
            botState.getPickedCards().add(pickedCard);
            botState.getWaitingPacks().remove(pack);
            botState.setTimeLeft(0);

            final Player nextPlayer = getNextPlayer(game, botPlayer);
            if (!pack.getCards().isEmpty()) {
                nextPlayer.getPlayerState().getWaitingPacks().add(pack);
                passPack(game, nextPlayer);
            }
        }
    }

    @Override
    public List<Game> getAllCurrentGames() {
        return gameRepository.findAllByStatus(STARTED);
    }

    @Override
    public void pick(Game game, Player player, String cardId) {
        final PlayerState playerState = player.getPlayerState();

        if (!playerState.hasWaitingPack()) {
            return;
        }

        final Pack waitingPack = playerState.getWaitingPack();

        // Pick the card
        final MTGCard pickedCard = waitingPack.getCards().stream()
            .filter(card -> card.getId().equals(cardId))
            .findFirst()
            .orElse(waitingPack.getCards().get(new Random().nextInt(waitingPack.getCards().size())));

        playerState.getWaitingPacks().remove(waitingPack);
        waitingPack.getCards().remove(pickedCard);
        playerState.getPickedCards().add(pickedCard);
        playerState.setAutoPickId("");

        final String sessionId = player.getUserId();
        if (sessionId != null && !sessionId.equals("")) {
            customerService.send(sessionId, PickMessage.of(pickedCard));
        }

        // Pass the remaining pack
        if (!waitingPack.getCards().isEmpty()) {
            final Player nextPlayer = getNextPlayer(game, player);
            final PlayerState nextPlayerState = nextPlayer.getPlayerState();
            nextPlayerState.getWaitingPacks().add(waitingPack);
            passPack(game, nextPlayer);
        }

        final int pick = playerState.getPick() + 1;
        playerState.setPick(pick);

        if (playerState.hasWaitingPack()) {
            if (sessionId != null && !sessionId.equals("")) {
                customerService.send(sessionId, PackMessage.of(playerState.getWaitingPack().getCards()));
            }
        }
        if (game.isUseTimer()) {
            final int timeLeft = !playerState.hasWaitingPack() ? 0 : TimeProducer.calc(game.getTimer(), pick);
            player.getPlayerState().setTimeLeft(timeLeft);
        }

        if (isRoundFinished(game)) {
            startNewRound(game);
        }
    }

    @Override
    public boolean decreaseTimeLeft(Game game) {
        Boolean hasChanged = false;
        final Iterator<PlayerState> list = game.getPlayers().stream()
            .map(Player::getPlayerState)
            .filter(ps -> ps.getTimeLeft() > 0)
            .collect(Collectors.toList())
            .iterator();

        // TODO: find better way to dodge ConcurrentModificationException
        while (list.hasNext()) {
            final PlayerState ps = list.next();
            hasChanged = true;
            final int timeLeft = ps.getTimeLeft() - 1;
            ps.setTimeLeft(timeLeft);

            if (timeLeft == 0) {
                pick(game, ps.getPlayer(), ps.getAutoPickId());
            }
        }

        return hasChanged;
    }

    private void passPack(Game game, Player nextPlayer) {
        if (nextPlayer.isBot()) {
            pickAsBot(game, nextPlayer);
        } else {
            final PlayerState nextPlayerState = nextPlayer.getPlayerState();
            final boolean hasOnePack = nextPlayerState.getWaitingPacks().size() == 1;

            if (hasOnePack) {
                final int pick = nextPlayerState.getPick() + 1;
                nextPlayerState.setPick(pick);

                if (game.isUseTimer()) {
                    final int timeLeft = TimeProducer.calc(game.getTimer(), pick);
                    nextPlayerState.setTimeLeft(timeLeft);
                }
                customerService.send(nextPlayer.getUserId(), PackMessage.of(nextPlayerState.getWaitingPack().getCards()));
            }
        }
    }

    @Override
    public Player addPlayer(Game game, Player player) {
        player.setSeat(game.getPlayers().size());
        game.getPlayers().add(player);
        gameRepository.save(game);
        return game.getPlayers().stream()
            .filter(p -> p.getUserId().equals(player.getUserId()))
            .findFirst()
            .orElse(null);
    }
}
