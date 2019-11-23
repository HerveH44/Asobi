package com.hhuneau.asobi.websocket;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.mtg.MTGFacade;
import com.hhuneau.asobi.mtg.game.AuthTokenDTO;
import com.hhuneau.asobi.mtg.lobby.ApplicationStatistics;
import com.hhuneau.asobi.mtg.lobby.ApplicationStatistics.LobbyStatsDTO;
import com.hhuneau.asobi.mtg.sets.DraftableSetsProvider;
import com.hhuneau.asobi.mtg.sets.SetDTO;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import com.hhuneau.asobi.websocket.events.game.GameEvent;
import com.hhuneau.asobi.websocket.messages.AuthTokenMessage;
import com.hhuneau.asobi.websocket.messages.Message;
import com.hhuneau.asobi.websocket.messages.SetsExportMessage;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.Map;

@Controller
public class StompController {

    private final ApplicationStatistics statistics;
    private final CustomerService customerService;
    private final DraftableSetsProvider draftableSetsProvider;
    private final MTGFacade mtgFacade;

    public StompController(ApplicationStatistics statistics, CustomerService customerService,
                           DraftableSetsProvider draftableSetsProvider, MTGFacade mtgFacade) {
        this.statistics = statistics;
        this.customerService = customerService;
        this.draftableSetsProvider = draftableSetsProvider;
        this.mtgFacade = mtgFacade;
    }

    @SubscribeMapping("/lobby")
    @SendTo("/topic/lobby")
    public LobbyStatsDTO lobbyStats() {
        return statistics.getStatistics();
    }

    @SubscribeMapping("/sets")
    public Message getSets() {
        final Map<String, List<SetDTO>> draftableSets = draftableSetsProvider.getDraftableSets();
        return SetsExportMessage.of(draftableSets);
    }

    @MessageMapping("/game/create")
    @SendToUser("/queue/reply")
    public Message createGame(CreateGameEvent event) {
        final AuthTokenDTO authTokenDTO = mtgFacade.handle(event);
        return AuthTokenMessage.of(authTokenDTO);
    }

    @MessageMapping("/game")
    public void joinGame(GameEvent event) {
        mtgFacade.handle(event);
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        mtgFacade.disconnectUser(event.getSessionId());
        customerService.broadcast("/topic/lobby", statistics.getStatistics());
    }

//    @MessageExceptionHandler
//    public ApplicationError handleException(MyException exception) {
//        return appError;
//    }
}
