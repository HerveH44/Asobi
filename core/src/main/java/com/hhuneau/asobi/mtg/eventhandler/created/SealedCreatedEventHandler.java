package com.hhuneau.asobi.mtg.eventhandler.created;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameService;
import com.hhuneau.asobi.mtg.pool.Booster;
import com.hhuneau.asobi.mtg.pool.PoolService;
import com.hhuneau.asobi.websocket.events.game.host.StartGameEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.hhuneau.asobi.mtg.game.GameType.SEALED;

@Component
@Transactional
public class SealedCreatedEventHandler extends GameCreatedEventHandler {
    public SealedCreatedEventHandler(GameService gameService, CustomerService customerService, PoolService poolService) {
        super(gameService, customerService, poolService);
    }

    @Override
    public void handle(Game game, StartGameEvent evt) {
        super.handle(game, evt);
        game.getPlayers().forEach(player -> {
            final List<Booster> pool = player.getPool();
            player.getPlayerState().setPickedCards(
                pool.stream()
                    .flatMap(booster -> booster.getCards().stream())
                    .collect(Collectors.toList())
            );
        });
        gameService.finishGame(game);
    }

    @Override
    public boolean isInterested(Game game) {
        return super.isInterested(game) && game.getGameType().equals(SEALED);
    }
}
