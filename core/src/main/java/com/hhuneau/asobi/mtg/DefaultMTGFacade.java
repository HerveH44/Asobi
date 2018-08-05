package com.hhuneau.asobi.mtg;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.mtg.eventhandler.EventHandler;
import com.hhuneau.asobi.mtg.game.*;
import com.hhuneau.asobi.mtg.player.Player;
import com.hhuneau.asobi.mtg.player.PlayerService;
import com.hhuneau.asobi.mtg.sets.MTGSetsService;
import com.hhuneau.asobi.mtg.sets.SetDTO;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import com.hhuneau.asobi.websocket.events.SessionConnectedEvent;
import com.hhuneau.asobi.websocket.events.SessionDisconnectedEvent;
import com.hhuneau.asobi.websocket.events.game.GameEvent;
import com.hhuneau.asobi.websocket.messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    private final MTGSetsService setsService;
    private final PlayerService playerService;

    public DefaultMTGFacade(GameService gameService, CustomerService customerService, List<EventHandler> eventHandlers, MTGSetsService setsService, PlayerService playerService) {
        this.gameService = gameService;
        this.customerService = customerService;
        this.eventHandlers = eventHandlers;
        this.setsService = setsService;
        this.playerService = playerService;
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
    public void handle(SessionDisconnectedEvent event) {
        playerService.disconnectPlayersWithSession(event.sessionId);
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
        try {
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
        } catch (Exception e) {
            LOGGER.error("error while handling event {} {}", evt, e);
        }
    }

    @Override
    public void broadcastState(long gameId) {
        gameService.getGame(gameId)
            .ifPresent(this::broadcastGameState);
    }

    private void broadcastGameState(Game game) {
        final GameStateMessage gameStateMessage = GameStateMessage.of(GameStateDTO.of(game));
        game.getPlayers().forEach(player -> {
            customerService.send(player.getUserId(), gameStateMessage);
        });
    }

    @Scheduled(fixedRate = 1000)
    public void decreaseTimeLeft() {
        gameService.getAllCurrentGames().forEach(game -> {
            game.getPlayers().stream()
                .map(Player::getPlayerState)
                .filter(ps -> ps.getTimeLeft() > 0)
                .forEach(ps -> {
                    final int timeLeft = ps.getTimeLeft() - 1;
                    ps.setTimeLeft(timeLeft);

                    if (timeLeft == 0) {
                        gameService.pick(game, ps.getPlayer(), ps.getAutoPickId());
                    }
                });
            broadcastGameState(game);
            gameService.save(game);
        });
    }

    public static class GameStateDTO {
        public List<PartialPlayerStateDTO> playersStates;
        public long gameId;
        public String title;
        public long seats;
        public boolean isPrivate;
        public GameMode gameMode;
        public GameType gameType;
        public Status status;
        public Date createdDate;
        public int round;
        public boolean didGameStart;

        public static GameStateDTO of(Game game) {
            final GameStateDTO gameStateDTO = new GameStateDTO();
            gameStateDTO.createdDate = game.getCreatedDate();
            gameStateDTO.gameId = game.getGameId();
            gameStateDTO.gameMode = game.getGameMode();
            gameStateDTO.gameType = game.getGameType();
            gameStateDTO.isPrivate = game.isPrivate();
            gameStateDTO.round = game.getRound();
            gameStateDTO.seats = game.getSeats();
            gameStateDTO.status = game.getStatus();
            gameStateDTO.title = game.getTitle();
            gameStateDTO.didGameStart = game.getStatus().hasStarted();
            gameStateDTO.playersStates = game.getPlayers().stream()
                .map(PartialPlayerStateDTO::of)
                .collect(Collectors.toList());
            return gameStateDTO;
        }
    }

    public static class PartialPlayerStateDTO {
        public String name;
        public boolean isBot;
        public int packs;
        public int time;
        public String hash;

        public static PartialPlayerStateDTO of(Player player) {
            final PartialPlayerStateDTO dto = new PartialPlayerStateDTO();
            dto.name = player.getName();
            dto.packs = player.getPlayerState().getWaitingPacks().size();
            dto.time = player.getPlayerState().getTimeLeft();
            return dto;
        }
    }
}
