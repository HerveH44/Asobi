package com.hhuneau.asobi.mtg.eventhandler.created;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.mtg.MTGFacade;
import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameService;
import com.hhuneau.asobi.mtg.player.Player;
import com.hhuneau.asobi.mtg.player.PlayerService;
import com.hhuneau.asobi.mtg.player.PlayerState;
import com.hhuneau.asobi.mtg.pool.Booster;
import com.hhuneau.asobi.mtg.pool.Pack;
import com.hhuneau.asobi.mtg.pool.PoolService;
import com.hhuneau.asobi.mtg.sets.MTGCard;
import com.hhuneau.asobi.websocket.events.game.StartGameEvent;
import com.hhuneau.asobi.websocket.events.game.player.PickEvent;
import com.hhuneau.asobi.websocket.messages.PlayerStateMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.hhuneau.asobi.mtg.game.GameType.DRAFT;

@Component
@Transactional
public class DraftCreatedEventHandler extends GameCreatedEventHandler {

    private final MTGFacade facade;

    public DraftCreatedEventHandler(GameService gameService, CustomerService customerService, PoolService poolService, PlayerService playerService, MTGFacade facade) {
        super(gameService, customerService, poolService);
        this.facade = facade;
    }

    @Override
    public void handle(Game game, StartGameEvent evt) {
        super.handle(game, evt);
        startNewRound(game);
    }

    private void startNewRound(Game game) {
        AtomicBoolean isFinished = new AtomicBoolean(true);

        game.getPlayers().forEach(player -> {
            final List<Booster> pool = player.getPool();
            if (!pool.isEmpty()) {
                isFinished.set(false);
                //put the first booster as available
                final Pack firstPack = new Pack();
                firstPack.setCards(new ArrayList<>(pool.remove(0).getCards()));
                player.getPlayerState().getWaitingPacks().add(firstPack);
                customerService.send(player.getUserId(),
                    PlayerStateMessage.of(player.getPlayerState()));
            }
        });

        if (!isFinished.get()) {
            game.setRound(game.getRound() + 1);
            facade.broadcastState(game.getGameId());
        }
    }

    @Override
    public void handle(Game game, PickEvent evt) {
        game.getPlayers().stream()
            .filter(player -> player.getPlayerId() == evt.playerId)
            .findFirst()
            .ifPresent(player -> {
                final PlayerState playerState = player.getPlayerState();
                final Pack waitingPack = playerState.getWaitingPacks().remove(0);

                // Pick the card
                final MTGCard pickedCard = waitingPack.getCards().remove(evt.cardIndex);
                playerState.getPickedCards().add(pickedCard);

                // Alert player of the new pack
                if (!playerState.getWaitingPacks().isEmpty()) {
                    customerService.send(player.getUserId(), PlayerStateMessage.of(playerState));
                }

                // Pass the remaining pack
                final Player nextPlayer = gameService.getNextPlayer(game, player);
                final PlayerState nextPlayerState = nextPlayer.getPlayerState();
                nextPlayerState.getWaitingPacks().add(waitingPack);
                customerService.send(nextPlayer.getUserId(), PlayerStateMessage.of(nextPlayerState));

                if (gameService.isRoundFinished(game)) {
                    // START A NEW ROUND
                    startNewRound(game);
                }

                // Broadcast gameState
                facade.broadcastState(game.getGameId());
            });
    }

    @Override
    public boolean isInterested(Game game) {
        return super.isInterested(game) && game.getGameType().equals(DRAFT);
    }
}
