package clientTests;

import Exceptions.ResponseException;
import dataAccess.Exceptions.DataAccessException;
import model.Request.RegisterRequest;
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
        RegisterRequest request = new RegisterRequest("Caleb2", "password", "email@eemail");
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
        RegisterRequest request = new RegisterRequest("Caleb", "password", "email@eemail");
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
}