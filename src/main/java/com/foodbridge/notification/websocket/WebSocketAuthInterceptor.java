package com.foodbridge.notification.websocket;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.foodbridge.auth.jwt.JwtService;
import com.foodbridge.security.service.CustomUserDetailsService;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public WebSocketAuthInterceptor(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(
                        message,
                        StompHeaderAccessor.class
                );

        if (accessor == null) {
            return message;
        }

        StompCommand command = accessor.getCommand();

        System.out.println("STOMP COMMAND: " + command);

        if (StompCommand.CONNECT.equals(command)) {

            String authHeader =
                    accessor.getFirstNativeHeader("Authorization");

            System.out.println(
                    "STOMP AUTH HEADER PRESENT: "
                    + (authHeader != null)
            );

            if (authHeader != null &&
                    authHeader.startsWith("Bearer ")) {

                String jwt = authHeader.substring(7);

                String username =
                        jwtService.extractUsername(jwt);

                System.out.println(
                        "STOMP JWT USERNAME: " + username
                );

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails)) {

                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    accessor.setUser(authentication);

                    System.out.println(
                            "WEBSOCKET USER AUTHENTICATED AS: "
                            + accessor.getUser().getName()
                    );
                }
            }
        }

        if (StompCommand.SUBSCRIBE.equals(command)) {

            System.out.println(
                    "SUBSCRIBE DESTINATION: "
                    + accessor.getDestination()
            );

            System.out.println(
                    "SUBSCRIBE USER: "
                    + (accessor.getUser() != null
                            ? accessor.getUser().getName()
                            : "NULL")
            );
        }

        return message;
    }
}