package clientUI;

import Exceptions.ResponseException;
import webSocketMessages.serverMessages.*;
import com.google.gson.Gson;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketCommunicator extends Endpoint {
    private Session session;
    private Repl repl;

    public WebSocketCommunicator(String url, Repl repl) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.repl = repl;


            this.session.addMessageHandler(new MessageHandler.Whole<String>() {

                @Override
                public void onMessage(String message) {
                    repl.notify(message);
                }

            });
        }
        catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void sendUserCommand(UserGameCommand userGameCommand){
        String ugc = new Gson().toJson(userGameCommand);
        try {
            send(ugc);
        } catch (Exception e) {
            throw new RuntimeException("Error sending UserGameCommand");
        }
    }


    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);}
    public void onOpen(Session session, EndpointConfig endpointConfig) {}


}
