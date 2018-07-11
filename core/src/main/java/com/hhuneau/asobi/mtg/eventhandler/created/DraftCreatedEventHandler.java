package com.hhuneau.asobi.mtg.eventhandler.created;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameService;
import com.hhuneau.asobi.mtg.pool.Booster;
import com.hhuneau.asobi.mtg.pool.Pack;
import com.hhuneau.asobi.mtg.pool.PoolService;
import com.hhuneau.asobi.websocket.events.game.StartGameEvent;
import com.hhuneau.asobi.websocket.messages.PlayerStateMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
        startNewRound(game);
    }

    private void startNewRound(Game game) {
        AtomicBoolean isFinished = new AtomicBoolean(true);

        game.getPlayers().forEach(player -> {
            final List<Booster> pool = player.getPool();
            if (!pool.isEmpty()) {
                isFinished.set(false);
                //put the first booster as available
                final Pack firstPack = new Pack();
                final Booster booster = pool.remove(0);
                firstPack.setCards(new ArrayList<>(booster.getCards()));
                player.getPlayerState().getWaitingPacks().add(firstPack);
                customerService.send(player.getUserId(),
                    PlayerStateMessage.of(player.getPlayerState()));
                poolService.delete(booster);
            }
        });

        if (!isFinished.get()) {
            game.setRound(game.getRound() + 1);
            gameService.save(game);
        }
    }

    @Override
    public boolean isInterested(Game game) {

        return super.isInterested(game) && game.getGameType().equals(DRAFT);
    }
}
