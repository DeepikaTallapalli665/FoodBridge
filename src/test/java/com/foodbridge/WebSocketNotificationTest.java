
package com.foodbridge;

import java.lang.reflect.Type;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class WebSocketNotificationTest {

    public static void main(String[] args) throws Exception {

        String token = System.getenv("FOODBRIDGE_JWT");

        if (token == null || token.isBlank()) {
            System.out.println(
                "Missing FOODBRIDGE_JWT environment variable."
            );
            return;
        }

        WebSocketStompClient client =
                new WebSocketStompClient(
                        new StandardWebSocketClient());

        client.setMessageConverter(
                new MappingJackson2MessageConverter());

        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("Authorization", "Bearer " + token);

        CountDownLatch notificationReceived = new CountDownLatch(1);

        StompSession session = client.connectAsync(
                "ws://localhost:8080/ws",
                new WebSocketHttpHeaders(),
                connectHeaders,
                new StompSessionHandlerAdapter() {

                    @Override
                    public void afterConnected(
                            StompSession session,
                            StompHeaders headers) {

                        System.out.println("WebSocket connected!");

                        session.subscribe(
                                "/user/queue/notifications",
                                new StompFrameHandler() {

                                    @Override
                                    public Type getPayloadType(
                                            StompHeaders headers) {
                                        return Object.class;
                                    }

                                    @Override
                                    public void handleFrame(
                                            StompHeaders headers,
                                            Object payload) {

                                    	if (payload instanceof byte[] bytes) {
                                    	    System.out.println(
                                    	        "LIVE NOTIFICATION: " + new String(bytes,
                                    	            java.nio.charset.StandardCharsets.UTF_8)
                                    	    );
                                    	} else {
                                    	    System.out.println("LIVE NOTIFICATION: " + payload);
                                    	}

                                        notificationReceived.countDown();
                                    }
                                });

                        System.out.println(
                            "Subscribed. Trigger a new notification now."
                        );
                    }

                    @Override
                    public void handleException(
                            StompSession session,
                            StompCommand command,
                            StompHeaders headers,
                            byte[] payload,
                            Throwable exception) {

                        System.err.println(
                            "WebSocket error: " + exception.getMessage()
                        );

                        exception.printStackTrace();
                    }

                    @Override
                    public void handleTransportError(
                            StompSession session,
                            Throwable exception) {

                        System.err.println(
                            "WebSocket transport error: "
                            + exception.getMessage()
                        );

                        exception.printStackTrace();
                    }
                }).get(10, TimeUnit.SECONDS);

        try {
            System.out.println(
                "Waiting for a live notification..."
            );

            notificationReceived.await();

        } finally {
            if (session.isConnected()) {
                session.disconnect();
            }

            client.stop();
        }
    }
}
