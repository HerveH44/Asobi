package com.hhuneau.asobi.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhuneau.asobi.game.player.Player;
import com.hhuneau.asobi.game.pool.Booster;
import com.hhuneau.asobi.game.pool.PoolService;
import com.hhuneau.asobi.websocket.messages.PoolMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static com.hhuneau.asobi.game.GameType.SEALED;

@Component
public class SealedEngine implements GameEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(SealedEngine.class);
    private final PlayerSessionService service;
    private final DefaultGameService gameService;
    private final ObjectMapper mapper;
    private final PoolService poolService;

    public SealedEngine(PlayerSessionService service, DefaultGameService gameService, ObjectMapper mapper, PoolService poolService) {
        this.service = service;
        this.gameService = gameService;
        this.mapper = mapper;
        this.poolService = poolService;
    }

    @Override
    @Transactional
    public void start(Long gameId) {
        gameService.getGame(gameId).ifPresent(game -> {
                final Set<Player> players = game.getPlayers();
                poolService.createPools(players, game);
                gameService.startGame(game);
                game.getPlayers().forEach(player -> {
                    //getPool
                    final List<Booster> pool = player.getPool();

                    //sendPool via session
                    final WebSocketSession session = service.getSession(player);
                    if (session.isOpen()) {
                        try {
                            session.sendMessage(
                                new TextMessage(mapper.writeValueAsString(PoolMessage.of(pool))));
                        } catch (IOException e) {
                            LOGGER.error(String.format("Can't parse booster %s", pool));
                        }
                    }
                });
            }
        );
    }

    @Override
    public boolean isInterested(GameType gameType) {
        return gameType == SEALED;
    }
}
