package com.hhuneau.asobi.websocket.messages;

import com.hhuneau.asobi.mtg.pool.Booster;

import java.util.List;
import java.util.stream.Collectors;

public class PoolMessage extends Message {

    static public PoolMessage of(List<Booster> boosters) {
        final PoolMessage poolMessage = new PoolMessage();
        poolMessage.setType("POOL");
        poolMessage.setPayload(boosters.stream().map(Booster::getCards).collect(Collectors.toList()));
        return poolMessage;
    }
}
