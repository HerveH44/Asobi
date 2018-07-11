package com.hhuneau.asobi.mtg.eventhandler.started;

import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameService;
import com.hhuneau.asobi.mtg.player.Player;
import com.hhuneau.asobi.mtg.player.PlayerState;
import com.hhuneau.asobi.mtg.pool.Pack;
import com.hhuneau.asobi.mtg.sets.MTGCard;
import com.hhuneau.asobi.websocket.events.game.player.PickEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.hhuneau.asobi.mtg.game.GameType.DRAFT;

@Component
@Transactional
public class DraftStartedEventHandler extends GameStartedEventHandler {

    private final GameService gameService;

    public DraftStartedEventHandler(GameService gameService) {
        this.gameService = gameService;
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
                }

                if (gameService.isRoundFinished(game)) {
                    // START A NEW ROUND
                    gameService.startNewRound(game);
                }
            });
    }

    @Override
    public boolean isInterested(Game game) {
        return super.isInterested(game) && game.getGameType().equals(DRAFT);
    }
}
