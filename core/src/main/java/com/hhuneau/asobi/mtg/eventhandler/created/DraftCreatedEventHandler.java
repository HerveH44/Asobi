package com.hhuneau.asobi.mtg.eventhandler.created;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameService;
import com.hhuneau.asobi.mtg.pool.PoolService;
import com.hhuneau.asobi.websocket.events.game.StartGameEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.hhuneau.asobi.mtg.game.GameType.DRAFT;

@Component
@Transactional
public class DraftCreatedEventHandler extends GameCreatedEventHandler {

    public DraftCreatedEventHandler(GameService gameService, CustomerService customerService, PoolService poolService) {
        super(gameService, customerService, poolService);
    }

    @Override
    public void handle(Game game, StartGameEvent evt) {
        super.handle(game, evt);
        gameService.startNewRound(game);
    }

    @Override
    public boolean isInterested(Game game) {

        return super.isInterested(game) && game.getGameType().equals(DRAFT);
    }
}
