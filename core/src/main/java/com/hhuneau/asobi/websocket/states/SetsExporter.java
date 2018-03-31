package com.hhuneau.asobi.websocket.states;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhuneau.asobi.messages.SetsExportMessage;
import com.hhuneau.asobi.sets.MTGSet;
import com.hhuneau.asobi.sets.MTGSetsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class SetsExporter implements OnConnectionEstablished {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetsExporter.class);
    private MTGSetsService setsService;
    private ObjectMapper mapper;

    public SetsExporter(MTGSetsService setsService, ObjectMapper mapper) {
        this.setsService = setsService;
        this.mapper = mapper;
    }

    @Override
    public void accept(WebSocketSession session) {
        try {
            final Map<String, List<SetDTO>> sets = setsService.getSets().stream()
                .map(SetDTO::of)
                .collect(Collectors.groupingBy(SetDTO::getType));
            final TextMessage message = new TextMessage(mapper.writeValueAsString(SetsExportMessage.of(sets)));
            session.sendMessage(message);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while JSONing sets", e);
        } catch (IOException e) {
            LOGGER.error("Error while handling exportation of sets", e);
        }
    }

    public static class SetDTO {
        public String code;
        public String name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String type;

        public static SetDTO of(MTGSet set) {
            SetDTO setDTO = new SetDTO();
            setDTO.code = set.getCode();
            setDTO.name = set.getName();
            setDTO.type = set.getType();
            return setDTO;
        }
    }
}
