package clientUI;

import Exceptions.ResponseException;

import java.util.Arrays;
import java.util.Scanner;

import model.Request.*;

import static ui.EscapeSequences.*;

public class Repl {

    private final ServerFacade server;
    private boolean signedIn;

    public Repl(String serverURL) {
        server = new ServerFacade(serverURL);
        signedIn = false;
    }


    public void run() {
        if (!signedIn) {
            System.out.print(this.preloginMenu());
        }
        else {
            System.out.print(this.preloginMenu());
        }

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit") && !signedIn) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = this.eval(line);
                System.out.print(BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }

//        while (!result.equals("quit") && signedIn) {
//            printPrompt();
//            String line = scanner.nextLine();
//
//            try {
//                result = this.eval(line);
//                System.out.print(BLUE + result);
//            } catch (Throwable e) {
//                var msg = e.toString();
//                System.out.print(msg);
//            }
//        }

        System.out.println();
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
//                case "quit" -> listPets();
                default -> preloginMenu();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            signedIn = true;
            LoginRequest request = new LoginRequest(params[0], params[1]);
            server.login(request);
            return String.format("You signed in as %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
    }

    public String register (String... params) throws ResponseException {
        if (params.length == 3) {
            signedIn = true;
            RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
            server.register(request);
            return String.format("You registered as %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }


    public String preloginMenu() {
        return """
                - register <USERNAME> <PASSWORD> <EMAIL>
                - login <USERNAME> <PASSWORD>
                - quit
                - help
        """;
    }

    public String postloginMenu() {
        return """
                - create <NAME>
                - list
                - join <ID>
                - logout
                - quit
                - help
        """;
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }
}
