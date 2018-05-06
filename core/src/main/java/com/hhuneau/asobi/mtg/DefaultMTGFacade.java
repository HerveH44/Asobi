package com.hhuneau.asobi.mtg;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.mtg.eventhandler.EventHandler;
import com.hhuneau.asobi.mtg.game.CreateGameDTO;
import com.hhuneau.asobi.mtg.game.GameService;
import com.hhuneau.asobi.mtg.player.Player;
import com.hhuneau.asobi.mtg.sets.MTGSetsService;
import com.hhuneau.asobi.mtg.sets.SetDTO;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import com.hhuneau.asobi.websocket.events.SessionConnectedEvent;
import com.hhuneau.asobi.websocket.events.game.GameEvent;
import com.hhuneau.asobi.websocket.messages.CreatedGameMessage;
import com.hhuneau.asobi.websocket.messages.ErrorMessage;
import com.hhuneau.asobi.websocket.messages.GameStateMessage;
import com.hhuneau.asobi.websocket.messages.SetsExportMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional
public class DefaultMTGFacade implements MTGFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMTGFacade.class);
    private final GameService gameService;
    private final CustomerService customerService;
    private final List<EventHandler> eventHandlers;
    private MTGSetsService setsService;

    public DefaultMTGFacade(GameService gameService, CustomerService customerService, List<EventHandler> eventHandlers, MTGSetsService setsService) {
        this.gameService = gameService;
        this.customerService = customerService;
        this.eventHandlers = eventHandlers;
        this.setsService = setsService;
    }

    @Override
    @EventListener
    public void handle(SessionConnectedEvent event) {
        final Map<String, List<SetDTO>> sets = setsService.getSets().stream()
            .map(SetDTO::of)
            .collect(Collectors.groupingBy(SetDTO::getType));
        customerService.send(event.sessionId, SetsExportMessage.of(sets));
    }

    @Override
    @EventListener
    public void handle(CreateGameEvent evt) {
        customerService.find(evt.sessionId)
            .ifPresentOrElse(
                customer -> {
                    final CreateGameDTO createGameDTO = gameService.createGame(evt);
                    customerService.send(evt.sessionId, CreatedGameMessage.of(createGameDTO));
                },
                () -> LOGGER.error("cannot find session with id {}", evt.sessionId)
            );
    }

    @Override
    @EventListener
    public void handle(GameEvent evt) {
        gameService.getGame(evt.gameId)
            .ifPresentOrElse(
                (game) -> eventHandlers.stream()
                    .filter(eventHandler -> eventHandler.isInterested(game))
                    .forEach(handler -> {
                        handler.handle(game, evt);
                        broadcastState(evt.gameId);
                    }),
                () -> customerService.send(evt.sessionId,
                    ErrorMessage.of(String.format("gameId %s does not exist", evt.gameId)))
            );
    }

    @Override
    public void broadcastState(long gameId) {
        gameService.getGame(gameId)
            .ifPresent(game -> {
                final List<PlayerStateDTO> gameState = game.getPlayers().stream()
                    .map(PlayerStateDTO::of)
                    .collect(Collectors.toList());
                game.getPlayers().forEach(player ->
                    customerService.send(player.getUserId(), GameStateMessage.of(gameState)));
            });
    }


    public static class PlayerStateDTO {
        public String name;
        public long waitingPack;

        public static PlayerStateDTO of(Player player) {
            final PlayerStateDTO dto = new PlayerStateDTO();
            dto.name = player.getName();
            dto.waitingPack = player.getPlayerState().getWaitingPacks() != null ? player.getPlayerState().getWaitingPacks().size() : 0;
            return dto;
        }
    }
}
