package org.openidentityplatform.sampleservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Date;

public class WebSocketHandler extends TextWebSocketHandler {
    final private static Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        logger.info("got message {}", message);
        session.sendMessage(new TextMessage("current time: " + new Date()));
    }
    /*
    let socket = new WebSocket('ws://localhost:8080/ws-handler');
    socket.onmessage = function(event) {
        alert(`got message from server: ${event.data}`);
    };
    socket.send('Hello world');
     */
}
