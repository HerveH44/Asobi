package com.hhuneau.asobi.mtg.eventhandler.started;

import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameService;
import com.hhuneau.asobi.mtg.player.Player;
import com.hhuneau.asobi.mtg.player.PlayerService;
import com.hhuneau.asobi.websocket.events.game.player.AutoPickEvent;
import com.hhuneau.asobi.websocket.events.game.player.PickEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.hhuneau.asobi.mtg.game.GameType.DRAFT;

@Component
@Transactional
public class DraftStartedEventHandler extends GameStartedEventHandler {

    private final GameService gameService;
    private final PlayerService playerService;

    public DraftStartedEventHandler(GameService gameService, PlayerService playerService) {
        this.gameService = gameService;
        this.playerService = playerService;
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
