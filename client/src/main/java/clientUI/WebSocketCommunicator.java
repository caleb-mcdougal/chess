//package clientUI;
//
//import webSocketMessages.serverMessages.*;
//import com.google.gson.Gson;
//import javax.websocket.*;
//import java.net.URI;
//
//public class WebSocketCommunicator extends Endpoint {
//    private Session session;
//
//    public WebSocketCommunicator() throws Exception {
//        URI uri = new URI("ws://localhost:8080/connect");
//        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//        this.session = container.connectToServer(this, uri);
//
//        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
//            public void onMessage(String message) {
//                System.out.println(message);
//            }
//        });
//    }
//
//    public void send(String msg) throws Exception {this.session.getBasicRemote().sendText(msg);}
//    public void onOpen(Session session, EndpointConfig endpointConfig) {}
//
//    public void onMessage(String message) {
//        try {
//            ServerMessage message =
//                    gson.fromJson(message, ServerMessage.class);
//            observer.notify(message);
//        } catch(Exception ex) {
//            observer.notify(new ErrorMessage(ex.getMessage()));
//        }
//    }
//
//}
