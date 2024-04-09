package webSocketMessages.userCommands;

public class Leave extends UserGameCommand{

    private int gameID;
    private CommandType commandType;

    public Leave(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        commandType = CommandType.LEAVE;
    }

    public int getGameID() {
        return gameID;
    }
}