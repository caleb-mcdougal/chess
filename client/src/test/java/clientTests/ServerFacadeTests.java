package clientTests;

import Exceptions.ResponseException;
import dataAccess.Exceptions.DataAccessException;
import model.Request.CreateGameRequest;
import model.Request.LoginRequest;
import model.Request.RegisterRequest;
import model.Response.ListGamesResponse;
import model.Response.LoginResponse;
import model.Response.RegisterResponse;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import clientUI.*;
import service.GameService;

import java.net.HttpURLConnection;
import java.util.Locale;


public class ServerFacadeTests {

    private static Server server;


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    //I need a before each clear DB, otherwise I am detecting the users registered in previous tests
    @BeforeEach
    public void clearDB() {
        GameService gs = new GameService();
        try {
            gs.clear();
        } catch (DataAccessException e) {
            System.out.println("Failure in pretest DB clear");
            Assertions.fail();
        }
    }

    @Test
    public void registerPositive() {
        var serverUrl = "http://localhost:8080";
        clientUI.ServerFacade facade = new ServerFacade(serverUrl);
        RegisterRequest request = new RegisterRequest("Caleb", "password", "email@email");
        RegisterResponse response = null;
        try {
            response = facade.register(request);
        } catch (ResponseException e) {
            System.out.println("Response exception in register positive");
            Assertions.fail();
        }
        Assertions.assertFalse(
                response.message() != null && response.message().toLowerCase(Locale.ROOT).contains("error"),
                "Response gave an error message");
    }

    @Test
    public void registerNegative() {
        var serverUrl = "http://localhost:8080";
        clientUI.ServerFacade facade = new ServerFacade(serverUrl);
        RegisterRequest request = new RegisterRequest("Caleb", "password", "email@email");
        try {
            facade.register(request);
        } catch (ResponseException e) {
            System.out.println("Response exception in register negative");
            Assertions.fail();
        }

        boolean errorCaught = false;

        try {
            facade.register(request);
        } catch (ResponseException e) {
            errorCaught = true;
        }

        Assertions.assertTrue(errorCaught);
    }

    @Test
    public void loginPositive() {
        var serverUrl = "http://localhost:8080";
        clientUI.ServerFacade facade = new ServerFacade(serverUrl);
        RegisterRequest request = new RegisterRequest("Caleb", "password", "email@email");
        RegisterResponse response = null;
        try {
            response = facade.register(request);
        } catch (ResponseException e) {
            System.out.println("Response exception in register positive");
            Assertions.fail();
        }
        Assertions.assertFalse(
                response.message() != null && response.message().toLowerCase(Locale.ROOT).contains("error"),
                "Response gave an error message");

        LoginRequest lRequest = new LoginRequest("Caleb", "password");
        LoginResponse lResponse = null;
        try {
            lResponse = facade.login(lRequest);
        } catch (ResponseException e) {
            System.out.println("Error thrown during login");
            Assertions.fail();
        }
        System.out.print(lResponse);
    }

    @Test
    public void loginNegative() {
        var serverUrl = "http://localhost:8080";
        clientUI.ServerFacade facade = new ServerFacade(serverUrl);
        RegisterRequest request = new RegisterRequest("Caleb", "password", "email@email");
        RegisterResponse response = null;
        try {
            response = facade.register(request);
        } catch (ResponseException e) {
            System.out.println("Response exception in register positive");
            Assertions.fail();
        }
        Assertions.assertFalse(
                response.message() != null && response.message().toLowerCase(Locale.ROOT).contains("error"),
                "Response gave an error message");

        boolean caughtBadUsername = false;
        boolean caughtBadPassword = false;

        LoginRequest lRequest = new LoginRequest("badUsername", "password");
        try {
            facade.login(lRequest);
        } catch (ResponseException e) {
            caughtBadUsername = true;
        }

        LoginRequest lRequest2 = new LoginRequest("Caleb", "badPassword");
        try {
            facade.login(lRequest2);
        } catch (ResponseException e) {
            caughtBadPassword = true;
        }

        Assertions.assertTrue(caughtBadUsername);
        Assertions.assertTrue(caughtBadPassword);
    }

    @Test
    public void createGamePositive() {
        var serverUrl = "http://localhost:8080";
        clientUI.ServerFacade facade = new ServerFacade(serverUrl);
        RegisterRequest request = new RegisterRequest("Caleb", "password", "email@email");
        RegisterResponse response = null;
        try {
            response = facade.register(request);
        } catch (ResponseException e) {
            System.out.println("Response exception in register positive");
            Assertions.fail();
        }
        Assertions.assertFalse(
                response.message() != null && response.message().toLowerCase(Locale.ROOT).contains("error"),
                "Response gave an error message");

        CreateGameRequest cgr = new CreateGameRequest("newGame");
        try {
            facade.create(cgr);
        } catch (ResponseException e) {
            System.out.println(e.getMessage());
            System.out.println("Create game failure");
            Assertions.fail();
        }
    }

//    @Test
//    public void createGameNegative() {
//        var serverUrl = "http://localhost:8080";
//        clientUI.ServerFacade facade = new ServerFacade(serverUrl);
//        RegisterRequest request = new RegisterRequest("Caleb", "password", "email@email");
//        RegisterResponse response = null;
//        try {
//            response = facade.register(request);
//        } catch (ResponseException e) {
//            System.out.println("Response exception in register positive");
//            Assertions.fail();
//        }
//        Assertions.assertFalse(
//                response.message() != null && response.message().toLowerCase(Locale.ROOT).contains("error"),
//                "Response gave an error message");
//
//        CreateGameRequest cgr = new CreateGameRequest("newGame");
//        try {
//            facade.create(cgr);
//        } catch (ResponseException e) {
//            System.out.println(e.getMessage());
//            System.out.println("Create game failure");
//            Assertions.fail();
//        }
//
//        boolean caughtNameRepeat = false;
//        try {
//            System.out.println("here");
//            facade.create(cgr);
//        } catch (ResponseException e) {
//            caughtNameRepeat = true;
//        }
//        Assertions.assertTrue(caughtNameRepeat);
//    }

    @Test
    public void createGameNoSignIn() {
        var serverUrl = "http://localhost:8080";
        clientUI.ServerFacade facade = new ServerFacade(serverUrl);
        CreateGameRequest cgr = new CreateGameRequest("newGame");
        boolean caughtNoSignIn = false;
        try {
            facade.create(cgr);
        } catch (ResponseException e) {
            caughtNoSignIn = true;
        }
        Assertions.assertTrue(caughtNoSignIn);
    }

    @Test
    public void listGamePositive() {
        var serverUrl = "http://localhost:8080";
        clientUI.ServerFacade facade = new ServerFacade(serverUrl);
        RegisterRequest request = new RegisterRequest("Caleb", "password", "email@email");
        RegisterResponse response = null;
        try {
            response = facade.register(request);
        } catch (ResponseException e) {
            System.out.println("Response exception in register positive");
            Assertions.fail();
        }
        Assertions.assertFalse(
                response.message() != null && response.message().toLowerCase(Locale.ROOT).contains("error"),
                "Response gave an error message");

        ListGamesResponse responseLG = null;
        try {
            responseLG = facade.list();
        } catch (ResponseException e) {
            System.out.println(e.getMessage());
            System.out.println("List game failure");
            Assertions.fail();
        }

        Assertions.assertEquals(responseLG.games().length, 0, "No games should exist");

        CreateGameRequest cgr = new CreateGameRequest("newGame");
        try {
            facade.create(cgr);
        } catch (ResponseException e) {
            System.out.println(e.getMessage());
            System.out.println("Create game failure");
            Assertions.fail();
        }

        try {
            responseLG = facade.list();
        } catch (ResponseException e) {
            System.out.println(e.getMessage());
            System.out.println("List game failure");
            Assertions.fail();
        }

        Assertions.assertEquals(responseLG.games().length, 1, "1 game should exist");
    }

    @Test
    public void listGameNegative() {
        var serverUrl = "http://localhost:8080";
        clientUI.ServerFacade facade = new ServerFacade(serverUrl);

        boolean caughtNoSignIn = false;

        try {
            facade.list();
        } catch (ResponseException e) {
            caughtNoSignIn = true;
        }

        Assertions.assertTrue(caughtNoSignIn, "Sign in should be required for listgame");

    }

}