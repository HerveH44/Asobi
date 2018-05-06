package com.hhuneau.asobi.mtg.eventhandler.created;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameService;
import com.hhuneau.asobi.mtg.pool.Booster;
import com.hhuneau.asobi.mtg.pool.PoolService;
import com.hhuneau.asobi.websocket.events.game.StartGameEvent;
import com.hhuneau.asobi.websocket.messages.PoolMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            customerService.send(player.getUserId(), PoolMessage.of(pool));
        });
        gameService.finishGame(game);
    }

    @Override
    public boolean isInterested(Game game) {
        return super.isInterested(game) && game.getGameType().equals(SEALED);
    }
}
