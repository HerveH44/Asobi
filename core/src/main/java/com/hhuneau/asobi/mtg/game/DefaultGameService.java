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
import com.hhuneau.asobi.mtg.sets.card.MTGCardService;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import com.hhuneau.asobi.websocket.events.game.host.StartGameEvent;
import com.hhuneau.asobi.websocket.events.game.player.MessageEvent;
import com.hhuneau.asobi.websocket.events.game.player.PlayerNameEvent;
import com.hhuneau.asobi.websocket.messages.ErrorMessage;
import com.hhuneau.asobi.websocket.messages.PackMessage;
import com.hhuneau.asobi.websocket.messages.PickMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.hhuneau.asobi.mtg.game.GameMode.CUBE;
import static com.hhuneau.asobi.mtg.game.Status.FINISHED;
import static com.hhuneau.asobi.mtg.game.Status.STARTED;
import static java.util.Collections.emptyList;

@Service
@Transactional
public class DefaultGameService implements GameService {

    private final GameRepository gameRepository;
    private final MTGSetsService setService;
    private final PoolService poolService;
    private final CustomerService customerService;
    private final MTGCardService cardService;

    public DefaultGameService(GameRepository gameRepository, MTGSetsService setService, PoolService poolService, CustomerService customerService, MTGCardService cardService) {
        this.gameRepository = gameRepository;
        this.setService = setService;
        this.poolService = poolService;
        this.customerService = customerService;
        this.cardService = cardService;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Game> getGame(long gameId) {
        return gameRepository.findById(gameId);
    }

    @Override
    public AuthTokenDTO createGame(CreateGameEvent evt) {
        final String authToken = UUID.randomUUID().toString();
        if (evt.gameMode.equals(CUBE)) {
            final List<String> cardNames = Arrays.stream(evt.cubeList.split("\n")).collect(Collectors.toList());
            final List<MTGCard> cardList = new ArrayList<>();
            final List<String> notFoundCardNames = new ArrayList<>();
            cardNames.forEach(cardName -> cardService.getCard(cardName).ifPresentOrElse(cardList::add, () -> notFoundCardNames.add(cardName)));

            if (!notFoundCardNames.isEmpty()) {
                throw new IllegalStateException(String.format("Following cards were not found in the database : %s", String.join(", ", notFoundCardNames)));
            }

            // If we can't divide the cards to enough packs
            if (cardList.size() < (evt.seats * evt.packsNumber * 15)) {
                throw new IllegalStateException(String.format("Not enough cards to create cube for %s players and %s pack", evt.seats, evt.packsNumber));
            }
            final Game game = Game.of(evt, emptyList(), authToken, cardList);
            final Game savedGame = gameRepository.save(game);
            return AuthTokenDTO.of(savedGame.getGameId(), authToken);
        } else {
            final List<MTGSet> sets = setService.getSets(evt.sets);
            final Game game = Game.of(evt, sets, authToken, emptyList());
            final Game savedGame = gameRepository.save(game);
            return AuthTokenDTO.of(savedGame.getGameId(), authToken);
        }
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
        if (game.getRound() == game.getPacksNumber()) {
            finishGame(game);
            return;
        }

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
                firstPack.setPickNumber(1);

                if (game.isUseTimer()) {
                    playerState.setTimeLeft(TimeProducer.calc(game.getTimer(), firstPack.getPickNumber()));
                }

                playerState.setPack(game.getRound());
                poolService.delete(booster);

                final String userId = player.getUserId();
                if (userId != null && !userId.equals("")) {
                    customerService.send(userId, PackMessage.of(firstPack));
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
            pack.setPickNumber(pack.getPickNumber() + 1);
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
        waitingPack.setPickNumber(waitingPack.getPickNumber() + 1);
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

        if (playerState.hasWaitingPack()) {
            if (playerState.getWaitingPack().getCards().size() == 1) {
                pick(game, player, playerState.getWaitingPack().getCards().get(0).getId());
                return;
            }
            if (sessionId != null && !sessionId.equals("")) {
                customerService.send(sessionId, PackMessage.of(playerState.getWaitingPack()));
            }
        }
        if (game.isUseTimer()) {
            final int timeLeft = !playerState.hasWaitingPack() ? 0 : TimeProducer.calc(game.getTimer(), playerState.getWaitingPack().getPickNumber());
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

    @Override
    public void kick(Game game, long kick) {
        game.getPlayers().stream()
            .filter(player -> player.getSeat() == kick)
            .findFirst()
            .ifPresent(player -> customerService.send(player.getUserId(), ErrorMessage.of("You have been kicked from game " + game.getGameId()))
            );
        game.getPlayers().removeIf(p -> p.getSeat() == kick);
    }

    @Override
    public void swap(Game game, List<Long> swap) {
        if (swap.size() < 2) {
            return;
        }

        // Calculate max occupied seats
        // from all the current players, we keep the max Seat and we add 1
        final int gameSeats = 1 + game.getPlayers().stream().reduce(0, (maxSeat, player) -> Math.max(maxSeat, player.getSeat()), Math::max);
        final int firstSeat = Math.toIntExact(swap.get(0));
        final int secondSeat = Math.toIntExact((swap.get(1) % gameSeats + gameSeats) % gameSeats);

        game.getPlayers().stream()
            .filter(player -> player.getSeat() == firstSeat || player.getSeat() == secondSeat)
            .forEach(player -> player.setSeat(player.getSeat() == firstSeat ? secondSeat : firstSeat));
    }

    @Override
    public void addMessage(Game game, MessageEvent evt) {
        game.getPlayers().stream()
            .filter(player -> player.getPlayerId() == evt.playerId)
            .findFirst()
            .ifPresent(player -> game.getMessages().add(GameMessage.of(game, player.getName(), new Date(), evt.message)));
    }

    @Override
    public void setPlayerName(Game game, PlayerNameEvent evt) {
        game.getPlayers().stream()
            .filter(player -> player.getPlayerId() == evt.playerId)
            .findFirst()
            .ifPresent(player -> player.setName(evt.name));
    }

    @Override
    public List<Game> getGamesByStatus(Status status) {
        return gameRepository.findAllByStatus(status);
    }

    @Override
    public void delete(Game game) {
        gameRepository.delete(game);
    }

    private void passPack(Game game, Player nextPlayer) {
        if (nextPlayer.isBot()) {
            pickAsBot(game, nextPlayer);
        } else {
            final PlayerState nextPlayerState = nextPlayer.getPlayerState();
            final boolean hasOnePack = nextPlayerState.getWaitingPacks().size() == 1;

            if (hasOnePack) {

                final Pack waitingPack = nextPlayerState.getWaitingPack();
                if (waitingPack.getCards().size() == 1) {
                    pick(game, nextPlayer, waitingPack.getCards().get(0).getId());
                    return;
                }

                if (game.isUseTimer()) {
                    final int timeLeft = TimeProducer.calc(game.getTimer(), waitingPack.getPickNumber());
                    nextPlayerState.setTimeLeft(timeLeft);
                }
                customerService.send(nextPlayer.getUserId(), PackMessage.of(waitingPack));
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
