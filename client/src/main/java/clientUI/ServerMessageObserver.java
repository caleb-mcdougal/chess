package clientUI;

import webSocketMessages.serverMessages.ServerMessage;

public interface ServerMessageObserver {
    public void notify (String messageString);
}
