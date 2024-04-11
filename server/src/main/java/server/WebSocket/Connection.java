package server.WebSocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String visitorName;
    public Session session;

    public Connection(String visitorName, Session session) {
        this.visitorName = visitorName;
        this.session = session;
    }

    public void send(String msg){
        try {
            session.getRemote().sendString(msg);
        } catch (IOException e) {
            System.out.println("Error sending WS message to entire session");
            throw new RuntimeException(e);
        }
    }
}