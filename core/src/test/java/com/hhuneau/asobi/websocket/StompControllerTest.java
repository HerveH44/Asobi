package com.hhuneau.asobi.websocket;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.concurrent.*;

import static java.util.Collections.singletonList;
import static java.util.concurrent.TimeUnit.SECONDS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class StompControllerTest {
    @Value("${local.server.port}")
    private int port;
    private String URL;
    private BlockingQueue<String> blockingQueue;
    private WebSocketStompClient stompClient;

    private static final String HELLO_ENDPOINT = "/app/hello";
    private static final String TOPIC_HELLO_ENDPOINT = "/topic/greetings";

    private CompletableFuture<String> completableFuture;

    @Before
    public void setup() {
        completableFuture = new CompletableFuture<>();
        blockingQueue = new LinkedBlockingDeque<>();
        stompClient = new WebSocketStompClient(new SockJsClient(
            singletonList(new WebSocketTransport(new StandardWebSocketClient()))));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        URL = "ws://localhost:" + port + "/stomp";
    }


    @Test
    public void testMe() throws InterruptedException, ExecutionException, TimeoutException {

        final StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        stompSession.subscribe(TOPIC_HELLO_ENDPOINT, new DefaultStompFrameHandler());
        stompSession.subscribe("/greetings", new DefaultStompFrameHandler());
        stompSession.subscribe("/topic", new DefaultStompFrameHandler());
        final String message = "testPayload";
        stompSession.send(HELLO_ENDPOINT, message.getBytes());
        stompSession.send(TOPIC_HELLO_ENDPOINT, "toto");
        final String got = completableFuture.get(5, SECONDS);

        Assert.assertEquals(message, got);
    }

//    private static final String WEBSOCKET_URI = "ws://localhost:8080/stomp";
//    private static final String WEBSOCKET_TOPIC = "/topic";
//

//
//
//    @Test
//    public void shouldReceiveAMessageFromTheServer() throws Exception {
//        StompSession session = stompClient
//            .connect(WEBSOCKET_URI, new StompSessionHandlerAdapter() {})
//            .get(1, SECONDS);
//        session.subscribe(WEBSOCKET_TOPIC, new DefaultStompFrameHandler());
//
//        String message = "MESSAGE TEST";
//        session.send(WEBSOCKET_TOPIC, message.getBytes());
//
//        Assert.assertEquals(message, blockingQueue.poll(1, SECONDS));
//    }
//
    class DefaultStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return String.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            completableFuture.complete((String) o);
        }
    }
}
