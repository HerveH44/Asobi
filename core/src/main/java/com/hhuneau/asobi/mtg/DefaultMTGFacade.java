package com.hhuneau.asobi.mtg;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.mtg.eventhandler.EventHandler;
import com.hhuneau.asobi.mtg.game.*;
import com.hhuneau.asobi.mtg.player.Player;
import com.hhuneau.asobi.mtg.player.PlayerService;
import com.hhuneau.asobi.mtg.player.PlayerState;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import com.hhuneau.asobi.websocket.events.game.GameEvent;
import com.hhuneau.asobi.websocket.messages.GameStateMessage;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.hhuneau.asobi.mtg.game.Status.CREATED;
import static com.hhuneau.asobi.mtg.game.Status.STARTED;

@Component
@Transactional
public class DefaultMTGFacade implements MTGFacade {

    private final GameService gameService;
    private final List<EventHandler> eventHandlers;
    private final PlayerService playerService;
    private final CustomerService customerService;

    public DefaultMTGFacade(GameService gameService, List<EventHandler> eventHandlers,
                            PlayerService playerService, CustomerService customerService) {
        this.gameService = gameService;
        this.eventHandlers = eventHandlers;
        this.playerService = playerService;
        this.customerService = customerService;
    }

    @Override
    public void disconnectUser(String sessionId) {
        playerService.disconnectPlayersWithSession(sessionId);
    }

    @Override
    @EventListener
    public AuthTokenDTO handle(CreateGameEvent evt) {
        return gameService.createGame(evt);
    }

    @Override
    @EventListener
    public void handle(GameEvent evt) {
        final Game game = gameService.getGame(evt.gameId)
            .orElseThrow(() -> new GameNotFoundException(String.format("gameId %s does not exist", evt.gameId)));

        eventHandlers.stream()
            .filter(eventHandler -> eventHandler.isInterested(game))
            .forEach(handler -> handler.handle(game, evt));
        broadcastState(evt.gameId);
    }

    @Override
    public void broadcastState(long gameId) {
        gameService.getGame(gameId)
            .ifPresent(this::broadcastGameState);
    }

    private void broadcastGameState(Game game) {
        game.getPlayers().forEach(player -> {
            final GameStateMessage gameStateMessage = GameStateMessage.of(GameStateDTO.of(game, player));
            customerService.send(player.getUserId(), gameStateMessage);
        });
    }

    //TODO: implement that with a fixed time on server side and client side decreasing it by himself
    @Scheduled(fixedRate = 1000)
    public void decreaseTimeLeft() {
        gameService.getGamesByStatus(STARTED).forEach(game -> {
            boolean toBroadcast = gameService.decreaseTimeLeft(game);
            if (toBroadcast) {
                broadcastGameState(game);
            }
        });
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void finishStalledGames() {
        //Stalled games are games created at least 15 minutes ago that didn't start
        gameService.getGamesByStatus(CREATED).stream()
            .filter(game -> {
                final LocalDateTime createdDate = game.getCreatedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                return createdDate.plusMinutes(15).isBefore(LocalDateTime.now());
            })
            .forEach(gameService::finishGame);
    }

    public static class GameStateDTO {
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
        public long self;
        public boolean isHost;
        public List<String> sets;
        public String packsInfo;
        public List<PartialPlayerStateDTO> playersStates;
        public List<GameMessageDTO> messages;

        public static GameStateDTO of(Game game, Player player) {
            final int index = game.getPlayers().indexOf(player);

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
            gameStateDTO.self = game.getPlayers().get(index).getSeat();
            gameStateDTO.sets = game.getSets().stream().map(MTGSet::getCode).collect(Collectors.toList());
            gameStateDTO.packsInfo = game.getPacksInfo();
            gameStateDTO.isHost = game.getHostId().equals(player.getUserId());
            gameStateDTO.didGameStart = game.getStatus().hasStarted();
            gameStateDTO.playersStates = game.getPlayers().stream()
                .map(PartialPlayerStateDTO::of)
                .collect(Collectors.toList());

            gameStateDTO.messages = game.getMessages().stream()
                .map(GameMessageDTO::of)
                .collect(Collectors.toList());

            return gameStateDTO;
        }
    }

    public static class PartialPlayerStateDTO {
        public String name;
        public boolean isBot;
        public int packs;
        public int time;
        public int seat;
        public String cockHash;
        public String mwsHash;

        public static PartialPlayerStateDTO of(Player player) {
            final PlayerState playerState = player.getPlayerState();
            final PartialPlayerStateDTO dto = new PartialPlayerStateDTO();
            dto.name = player.getName();
            dto.packs = playerState.getWaitingPacks().size();
            dto.time = playerState.getTimeLeft();
            dto.isBot = player.isBot() || player.getUserId().equals("");
            dto.seat = player.getSeat();
            dto.cockHash = playerState.getCockHash();
            dto.mwsHash = playerState.getMwsHash();
            return dto;
        }
    }

    private static class GameMessageDTO {
        public long id;
        public String name;
        public Date time;
        public String text;

        public static GameMessageDTO of(GameMessage gameMessage) {
            final GameMessageDTO message = new GameMessageDTO();
            message.id = gameMessage.getId();
            message.name = gameMessage.getName();
            message.time = gameMessage.getTime();
            message.text = gameMessage.getMessage();
            return message;
        }
    }
}
