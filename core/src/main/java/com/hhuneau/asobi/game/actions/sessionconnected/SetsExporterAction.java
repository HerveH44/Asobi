package com.hhuneau.asobi.game.actions.sessionconnected;

import com.hhuneau.asobi.game.actions.Action;
import com.hhuneau.asobi.game.sets.MTGSetsService;
import com.hhuneau.asobi.game.sets.SetDTO;
import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.websocket.events.server.SessionConnectedEvent;
import com.hhuneau.asobi.websocket.messages.SetsExportMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class SetsExporterAction implements Action<SessionConnectedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetsExporterAction.class);
    private final MTGSetsService setsService;
    private final CustomerService customerService;

    public SetsExporterAction(MTGSetsService setsService, CustomerService customerService) {
        this.setsService = setsService;
        this.customerService = customerService;
    }

    @Override
    @EventListener
    public void accept(SessionConnectedEvent event) {
        final Map<String, List<SetDTO>> sets = setsService.getSets().stream()
            .map(SetDTO::of)
            .collect(Collectors.groupingBy(SetDTO::getType));
        customerService.send(event.sessionId, SetsExportMessage.of(sets));
    }

}
