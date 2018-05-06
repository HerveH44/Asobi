package com.hhuneau.asobi.mtg.actions;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameRepository;
import com.hhuneau.asobi.mtg.game.GameService;
import com.hhuneau.asobi.mtg.player.PlayerRepository;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import com.hhuneau.asobi.websocket.events.game.player.JoinGameEvent;
import com.hhuneau.asobi.websocket.events.game.player.LeaveGameEvent;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.Arrays;

import static com.hhuneau.asobi.mtg.game.GameMode.NORMAL;
import static com.hhuneau.asobi.mtg.game.GameType.SEALED;
import static com.hhuneau.asobi.websocket.events.EventType.CREATE_GAME;
import static com.hhuneau.asobi.websocket.events.EventType.JOIN_GAME;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ITActionTest {


    @Autowired
    private CustomerService customerService;

    @Autowired
    private ApplicationEventPublisher publisher;
    private static final String custId = "1";

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;
    private FakeCustomer customer;

    @Before
    public void setUp() {
        customer = new FakeCustomer(custId);
        customerService.add(customer);
    }

    @Ignore
    @Test
    public void canDeleteOnePlayer() {

        final CreateGameEvent createGameEvent = getCreateGameEvent();
        publisher.publishEvent(createGameEvent);

        final Game game = gameRepository.findAll().get(0);
        final JoinGameEvent joinGameEvent = getJoinGameEvent(game);
        publisher.publishEvent(joinGameEvent);

        final LeaveGameEvent leaveGameEvent = getLeaveGameEvent(game);
        publisher.publishEvent(leaveGameEvent);

        Assert.isTrue(playerRepository.count() == 0, "playerRepository.count() must be null");

    }

    private LeaveGameEvent getLeaveGameEvent(Game game) {
        final LeaveGameEvent leaveGameEvent = new LeaveGameEvent();
        leaveGameEvent.sessionId = custId;
        leaveGameEvent.gameId = game.getGameId();
        leaveGameEvent.playerId =  customer.getPlayerId();
        return leaveGameEvent;
    }

    private JoinGameEvent getJoinGameEvent(Game game) {
        final JoinGameEvent joinGameEvent = new JoinGameEvent();
        joinGameEvent.sessionId = custId;
        joinGameEvent.gameId = game.getGameId();
        joinGameEvent.type = JOIN_GAME;
        return joinGameEvent;
    }

    private CreateGameEvent getCreateGameEvent() {
        final CreateGameEvent createGameEvent = new CreateGameEvent();
        createGameEvent.type = CREATE_GAME;
        createGameEvent.sets = Arrays.asList("XLN", "XLN", "XLN");
        createGameEvent.gameMode = NORMAL;
        createGameEvent.gameType = SEALED;
        createGameEvent.isPrivate = true;
        createGameEvent.seats = 8;
        createGameEvent.title = "testGame";
        createGameEvent.sessionId = custId;
        return createGameEvent;
    }

}
