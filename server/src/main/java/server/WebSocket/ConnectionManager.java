package server.WebSocket;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;
//import webSocketMessages.Notification;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
//    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection>> connections = new ConcurrentHashMap<>();

    public void add(String visitorName, Session session, Integer gameID) {
        //Might need to add a check if the user is already in a game?
        System.out.println("in add");
        if(connections.containsKey(gameID)){ // If the game already has a map get it and the visitor with a new connection
            ConcurrentHashMap<String, Connection> gameMap = connections.get(gameID);
            var connection = new Connection(visitorName, session);
            gameMap.put(visitorName, connection);
        }
        else{
            var connection = new Connection(visitorName, session); // If there is not a map already, create a new one and with visitor and connection and add it to outer map
            ConcurrentHashMap<String, Connection> gameMap = new ConcurrentHashMap<>();
            gameMap.put(visitorName,connection);
            connections.put(gameID,gameMap);
        }
    }

    public void remove(String visitorName, Integer gameID) {
        System.out.println("in remove");
        ConcurrentHashMap<String, Connection> gameMap = connections.get(gameID);
        gameMap.remove(visitorName);
    }

    public void sendServerMessageAll(String excludeVisitorName, ServerMessage serverMessage, Integer gameID){
        System.out.println("in sendServerMessageAll");
        ConcurrentHashMap<String, Connection> gameMap = connections.get(gameID);
        var removeList = new ArrayList<Connection>();
        for (var c : gameMap.values()) {
            if (c.session.isOpen()) {
                if (!c.visitorName.equals(excludeVisitorName)) {
                    c.send(new Gson().toJson(serverMessage));
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            gameMap.remove(c.visitorName);
        }
    }

    public void sendMessageToRoot(String visitorName, ServerMessage serverMessage, Integer gameID){
        System.out.println("in sendMessageToRoot");
        ConcurrentHashMap<String, Connection> gameMap = connections.get(gameID);
        var removeList = new ArrayList<Connection>();
        for (var c : gameMap.values()) {
            if (c.session.isOpen()) {
                if (c.visitorName.equals(visitorName)) {
                    c.send(new Gson().toJson(serverMessage));
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            gameMap.remove(c.visitorName);
        }
    }
}