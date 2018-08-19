package com.hhuneau.asobi.mtg.eventhandler.started;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameService;
import com.hhuneau.asobi.mtg.player.PlayerService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.hhuneau.asobi.mtg.game.GameType.SEALED;


@Component
@Transactional
public class SealedStartedEventHandler extends GameStartedEventHandler {
    protected SealedStartedEventHandler(GameService gameService, CustomerService customerService, PlayerService playerService) {
        super(gameService, customerService, playerService);
    }

    @Override
    public boolean isInterested(Game game) {
        return super.isInterested(game) && game.getGameType().equals(SEALED);
    }
}
