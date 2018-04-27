package com.hhuneau.asobi.websocket.states;

import com.hhuneau.asobi.game.sets.MTGSet;
import com.hhuneau.asobi.game.sets.MTGSetsService;
import com.hhuneau.asobi.websocket.events.server.SessionConnectedEvent;
import com.hhuneau.asobi.websocket.events.server.SessionMessageEvent;
import com.hhuneau.asobi.websocket.messages.SetsExportMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class SetsExporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetsExporter.class);
    private final MTGSetsService setsService;

    public SetsExporter(MTGSetsService setsService) {
        this.setsService = setsService;
    }

    @EventListener
    public SessionMessageEvent onSessionConnected(SessionConnectedEvent event) {
        final Map<String, List<SetDTO>> sets = setsService.getSets().stream()
            .map(SetDTO::of)
            .collect(Collectors.groupingBy(SetDTO::getType));
        final SessionMessageEvent setsExporterEvent = new SessionMessageEvent();
        setsExporterEvent.sessionId = event.sessionId;
        setsExporterEvent.message = SetsExportMessage.of(sets);
        return setsExporterEvent;
    }

    @Getter
    @Setter
    public static class SetDTO {
        String code;
        String name;
        String type;

        static SetDTO of(MTGSet set) {
            SetDTO setDTO = new SetDTO();
            setDTO.code = set.getCode();
            setDTO.name = set.getName();
            setDTO.type = set.getType();
            return setDTO;
        }
    }
}
