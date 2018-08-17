package com.hhuneau.asobi.mtg.eventhandler.started;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameService;
import com.hhuneau.asobi.mtg.player.Player;
import com.hhuneau.asobi.mtg.player.PlayerService;
import com.hhuneau.asobi.websocket.events.game.player.AutoPickEvent;
import com.hhuneau.asobi.websocket.events.game.player.DraftLogEvent;
import com.hhuneau.asobi.websocket.events.game.player.PickEvent;
import com.hhuneau.asobi.websocket.messages.DraftLogMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.hhuneau.asobi.mtg.game.GameType.DRAFT;

@Component
@Transactional
public class DraftStartedEventHandler extends GameStartedEventHandler {

    protected DraftStartedEventHandler(GameService gameService, CustomerService customerService, PlayerService playerService) {
        super(gameService, customerService, playerService);
    }

    @Override
    public void handle(Game game, PickEvent evt) {
        game.getPlayers().stream()
            .filter(player -> player.getPlayerId() == evt.playerId)
            .findFirst()
            .ifPresent(player -> gameService.pick(game, player, evt.cardIndex));
    }

    @Override
    public void handle(Game game, AutoPickEvent evt) {
        game.getPlayers().stream()
            .filter(player -> player.getPlayerId() == evt.playerId)
            .findFirst()
            .ifPresent(player -> autoPick(game, evt, player));
    }

    @Override
    public void handle(Game game, DraftLogEvent evt) {
        game.getPlayers().stream()
            .filter(player -> player.getPlayerId() == evt.playerId)
            .findFirst()
            .ifPresent(player -> customerService.send(player.getUserId(),
                DraftLogMessage.of(player.getPlayerState().getPicksLog())));
    }

    private void autoPick(Game game, AutoPickEvent evt, Player player) {
        final String autoPickId = player.getPlayerState().getAutoPickId();

        if (autoPickId != null && autoPickId.equals(evt.autoPickId)) {
            gameService.pick(game, player, autoPickId);
        } else {
            player.getPlayerState().setAutoPickId(evt.autoPickId);
            playerService.save(player);
        }
    }

    @Override
    public boolean isInterested(Game game) {
        return super.isInterested(game) && game.getGameType().equals(DRAFT);
    }
}
