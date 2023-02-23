package com.lj.model.message;

import lombok.Data;

import javax.websocket.Session;

@Data
public class MyWebSocket {
    private Session session;
}
