package com.hhuneau.asobi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhuneau.asobi.sets.MTGSet;
import com.hhuneau.asobi.sets.MTGSetsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(WebSocketHandler.class);
    private final MTGSetsService setsService;
    private final ObjectMapper mapper;


    public WebSocketHandler(MTGSetsService setsService, ObjectMapper mapper) {
        this.setsService = setsService;
        this.mapper = mapper;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        final Map<String, List<SetDTO>> sets = setsService.getSets().stream()
                .map(SetDTO::of)
                .collect(Collectors.groupingBy(SetDTO::getType));
        TextMessage message = new TextMessage(mapper.writeValueAsBytes(sets));
        session.sendMessage(message);
        LOGGER.info("Websocket connected " + session.toString());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOGGER.info("Websocket " + session.toString() + " emitted message " + message);
        super.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOGGER.info("Websocket disconnected " + session.toString());
    }

    private static class SetDTO {
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