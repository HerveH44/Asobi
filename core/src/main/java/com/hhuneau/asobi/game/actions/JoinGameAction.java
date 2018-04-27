package com.hhuneau.asobi.game.actions;

import com.hhuneau.asobi.game.Game;
import com.hhuneau.asobi.game.GameService;
import com.hhuneau.asobi.game.player.Player;
import com.hhuneau.asobi.game.player.PlayerService;
import com.hhuneau.asobi.websocket.Customer;
import com.hhuneau.asobi.websocket.CustomerService;
import com.hhuneau.asobi.websocket.events.JoinGameEvent;
import com.hhuneau.asobi.websocket.messages.ErrorMessage;
import com.hhuneau.asobi.websocket.messages.PlayerIdMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JoinGameAction implements Action<JoinGameEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoinGameAction.class);
    private final GameService gameService;
    private final PlayerService playerService;
    private final CustomerService customerService;

    public JoinGameAction(GameService gameService, PlayerService playerService, CustomerService customerService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.customerService = customerService;
    }

    @Override
    @EventListener
    public void accept(JoinGameEvent evt) {
        final Optional<Customer> customer = customerService.find(evt.sessionId);
        if (!customer.isPresent()) {
            LOGGER.error("cannot find session with id {}", evt.sessionId);
            return;
        }
        final Optional<Game> game = gameService.getGame(evt.gameId);
        //TODO: Gérer les exceptions (game introuvable ou commencée/finie)
        if (!game.isPresent()) {
            final ErrorMessage errorMessage = ErrorMessage.of(String.format("Game %s not found", evt.gameId));
            customer.get().send(errorMessage);
            return;
        }

        //Get registeredPlayer from userId if exists. If not create a new registeredPlayer
        final Optional<Player> registeredPlayer = playerService.getPlayerWithId(evt.gameId, evt.playerId);
        registeredPlayer.ifPresent(player -> {
            player.setUserId(evt.sessionId);
            playerService.save(player);
            final PlayerIdMessage message = PlayerIdMessage.of(player.getPlayerId());
            customer.get().send(message);
        });

        if (!registeredPlayer.isPresent()) {
            if (game.get().getStatus().hasStarted()) {
                final ErrorMessage errorMessage = ErrorMessage.of(String.format("Game %s already started", evt.gameId));
                customer.get().send(errorMessage);
                return;
            }

            final Player player = playerService.save(Player.of(evt.sessionId, evt.name, game.get()));
            final PlayerIdMessage message = PlayerIdMessage.of(player.getPlayerId());
            customer.get().send(message);
        }
    }
}
