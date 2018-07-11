package com.hhuneau.asobi.mtg.eventhandler.started;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameService;
import com.hhuneau.asobi.mtg.player.Player;
import com.hhuneau.asobi.mtg.player.PlayerState;
import com.hhuneau.asobi.mtg.pool.Booster;
import com.hhuneau.asobi.mtg.pool.Pack;
import com.hhuneau.asobi.mtg.pool.PoolService;
import com.hhuneau.asobi.mtg.sets.MTGCard;
import com.hhuneau.asobi.websocket.events.game.player.PickEvent;
import com.hhuneau.asobi.websocket.messages.PlayerStateMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.hhuneau.asobi.mtg.game.GameType.DRAFT;

@Component
@Transactional
public class DraftStartedEventHandler extends GameStartedEventHandler {

    final GameService gameService;
    final CustomerService customerService;
    final PoolService poolService;

    public DraftStartedEventHandler(GameService gameService, CustomerService customerService, PoolService poolService) {
        this.gameService = gameService;
        this.customerService = customerService;
        this.poolService = poolService;
    }

    @Override
    public void handle(Game game, PickEvent evt) {
        game.getPlayers().stream()
            .filter(player -> player.getPlayerId() == evt.playerId)
            .findFirst()
            .ifPresent(player -> {
                final PlayerState playerState = player.getPlayerState();
                final List<Pack> waitingPacks = playerState.getWaitingPacks();

                // TODO: ameliorer check que les packs n'existent pas
                if (waitingPacks.isEmpty()) {
                    return;
                }
                final Pack waitingPack = waitingPacks.remove(0);

                // Pick the card
                final Optional<MTGCard> possiblePick = waitingPack.getCards().stream()
                                                           .filter(card -> card.getId().equals(evt.cardIndex))
                                                           .findFirst();

                // TODO: ameliorer check que la carte existe pas
                if (!possiblePick.isPresent()) {
                    return;
                }
                waitingPack.getCards().remove(possiblePick.get());
                playerState.getPickedCards().add(possiblePick.get());

                // Pass the remaining pack
                if (!waitingPack.getCards().isEmpty()) {
                    final Player nextPlayer = gameService.getNextPlayer(game, player);
                    final PlayerState nextPlayerState = nextPlayer.getPlayerState();
                    nextPlayerState.getWaitingPacks().add(waitingPack);
                    customerService.send(nextPlayer.getUserId(), PlayerStateMessage.of(nextPlayerState));

                    // Alert player of the new pack
                    if (!nextPlayer.equals(player) && !waitingPacks.isEmpty()) {
                        customerService.send(player.getUserId(), PlayerStateMessage.of(playerState));
                    }
                }

                if (gameService.isRoundFinished(game)) {
                    // START A NEW ROUND
                    startNewRound(game);
                }
            });
    }

    // TODO: unify with gameCreated
    private void startNewRound(Game game) {
        AtomicBoolean isFinished = new AtomicBoolean(true);

        game.getPlayers().forEach(player -> {
            final List<Booster> pool = player.getPool();
            if (!pool.isEmpty()) {
                isFinished.set(false);
                //put the first booster as available
                final Pack firstPack = new Pack();
                final Booster booster = pool.remove(0);
                final List<MTGCard> poolCards = booster.getCards();
                firstPack.setCards(new ArrayList<>(poolCards));
                player.getPlayerState().getWaitingPacks().add(firstPack);
                customerService.send(player.getUserId(),
                    PlayerStateMessage.of(player.getPlayerState()));
                poolService.delete(booster);
            }
        });

        if (!isFinished.get()) {
            game.setRound(game.getRound() + 1);
            gameService.save(game);
        }
    }

    @Override
    public boolean isInterested(Game game) {
        return super.isInterested(game) && game.getGameType().equals(DRAFT);
    }
}
