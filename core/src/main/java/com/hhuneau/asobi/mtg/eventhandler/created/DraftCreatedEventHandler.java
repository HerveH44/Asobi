package com.hhuneau.asobi.mtg.eventhandler.created;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameService;
import com.hhuneau.asobi.mtg.player.PlayerService;
import com.hhuneau.asobi.mtg.pool.Booster;
import com.hhuneau.asobi.mtg.pool.Pack;
import com.hhuneau.asobi.mtg.pool.PoolService;
import com.hhuneau.asobi.websocket.events.game.StartGameEvent;
import com.hhuneau.asobi.websocket.messages.PlayerStateMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.hhuneau.asobi.mtg.game.GameType.DRAFT;

@Component
@Transactional
public class DraftCreatedEventHandler extends GameCreatedEventHandler {

    public DraftCreatedEventHandler(GameService gameService, CustomerService customerService, PoolService poolService, PlayerService playerService) {
        super(gameService, customerService, poolService);
    }

    @Override
    public void handle(Game game, StartGameEvent evt) {
        super.handle(game, evt);
        game.getPlayers().forEach(player -> {
            final List<Booster> pool = player.getPool();
            if (!pool.isEmpty()) {
                //put the first booster as available
                final Pack firstPack = new Pack();
                firstPack.setCards(new ArrayList<>(pool.get(0).getCards()));
                player.getRemainingPacks().add(firstPack);
                customerService.send(player.getUserId(), PlayerStateMessage.of(firstPack));
            }
        });
    }

    @Override
    public boolean isInterested(Game game) {
        return super.isInterested(game) && game.getGameType().equals(DRAFT);
    }
}
