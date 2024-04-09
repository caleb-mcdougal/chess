package webSocketMessages.serverMessages;

public class ServerMessageError extends ServerMessage{

    private String errorMessage;
    public ServerMessageError(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
    }


    public String getErrorMessage() {
        return this.errorMessage;
    }

}
