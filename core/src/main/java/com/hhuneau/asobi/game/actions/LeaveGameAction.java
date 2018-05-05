package com.hhuneau.asobi.game.actions;

import com.hhuneau.asobi.customer.Customer;
import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.game.Game;
import com.hhuneau.asobi.game.GameService;
import com.hhuneau.asobi.game.player.PlayerService;
import com.hhuneau.asobi.websocket.events.game.player.LeaveGameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LeaveGameAction implements Action<LeaveGameEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeaveGameAction.class);
    private final PlayerService playerService;
    private final CustomerService customerService;
    private final GameService gameService;

    public LeaveGameAction(PlayerService playerService, CustomerService customerService, GameService gameService) {
        this.playerService = playerService;
        this.customerService = customerService;
        this.gameService = gameService;
    }

    @Override
    @EventListener
    public void accept(LeaveGameEvent evt) {
        final Optional<Customer> customer = customerService.find(evt.sessionId);
        if (!customer.isPresent()) {
            LOGGER.error("cannot find session with id {}", evt.sessionId);
            return;
        }
        if (!gameService.hasStarted(evt.gameId)) {
            final Optional<Game> game = gameService.getGame(evt.gameId);
            game.ifPresent(game1 -> {
                    if (game1.getPlayers().removeIf(player -> player.getPlayerId() == evt.playerId))
                        gameService.save(game1);
                }
            );
        }
    }
}
