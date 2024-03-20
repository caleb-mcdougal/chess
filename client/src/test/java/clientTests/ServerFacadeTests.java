package clientTests;

import Exceptions.ResponseException;
import model.Request.RegisterRequest;
import model.Response.RegisterResponse;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import clientUI.*;

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

        try {
            facade.register(request);
        } catch (ResponseException e) {
            Assertions.assertTrue(true);
        }

        System.out.println("didn't catch double register");
        Assertions.fail();
    }
}