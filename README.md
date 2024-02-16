# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```


Here is a link to the sequence diagram outlining design of the server:
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDAEooDmSAzmFMARDQVqhFHXyFiwUgBF+wAIIgQKLl0wATeQCNgXFDA3bMmdlAgBXbDADEaYFQCerDt178kg2wHcAFkjAxRFRSAFoAPnJKGigALhgAbQAFAHkyABUAXRgAegt9KAAdNABvfMp7AFsUABoYXDVvaA06lErgJAQAX0xhGJgIl04ePgEhaNF4qFceSgAKcqgq2vq9LiaoFpg2joQASkw2YfcxvtEByLkwRWVVLnj2FDAAVQKFguWDq5uVNQvDbTxMgAUQAMsC4OkYItljAAGbmSrQgqYb5KX5cAaDI5uUaecYiFTxNAWBAIQ4zE74s4qf5o25qeIgab8FCveYw4DVOoNdbNL7ydF3f5GeIASQAciCWFDOdzVo1mq12p0YJL0ilkbQcSMPIIaQZBvSMUyWYEFBYwL53hUuSgBdchX9BqK1VLgTKtUs7XVgJbfOkIABrdBujUwP1W1GChmY0LYyl4-UTIkR-2BkNoCnHJMEqjneORPqUeKRgPB9C9aKULGRYLoMDxABMAAYW8USmWM+geugNCYzJZrDZoNJHjBQRBOGgfP5Aph62Ei9W4olUhlsjl9Gp8R25SteRsND1i7AEzm9XnJjAEFOkGgbd75Yf+dncZeDXSYyaYI8Xm99wdH5hRdQFyDBCFZQ+O14URL1o0dWNayGd9ThTFB4kWN9dTQwlDUiHUqWTPD4hiJA4UcdkoCSNYj0fHlaNfQjc0-I1vzuU0UFZC0rUfT4EOA51IldSVpShLsKxndVNTLASnTjc9UOpdDS3TSTsKIq9aULKJqBLNMrW7LNT2QxdGxgVt21KCTM17NB+1McwrFsMwUFDSd2EsZgbD8AIgmQBt-lPeIEhkCD0mBTdty4Xd7DUzMqz0s8CMTD8VJvKcvOKGzK2YtK8K-RCfw0FAECeFAeN8bL4srY0QOEsCwvBCKDPLTNYIgJEsLq51FJw5SSIstsNJY9DkOCoaW0S-odLM5thr7AcnOHaYNAnNwYAAcTtTEfLnfyQmYQYJoSTbgSyXJ2DtarDPUky+s0g14mQHhtuqLgbra3LUtw-NtMudjGRvNw3tUT6jLkpDQKBCDIV-HbIYxZC8t+69LI7YAyXh96ehRga-sNY6V3mqzOyxq6cemmtZoCjASYx8mdrshzB2cmxsAsKBsDK+AzQMUHZz8hdaaO5ckpC9cLpyCmUHBySOxliU7RPFd-jx4iCc41lQbmRW7T2OWEvVrT8JgHr7hgZkuMCHW9eqA24tuhLzZFQFRI9KE0BQbwBek7HVERkCHtGwaZbFGQRvygnxuJ-3w6ps9BjmuOI8Wxyh1sRxStvbwYAAKQge8tpgmxtAQUAg2Fw6gtj5JnilmXDfQDtueALOoDgCBbygOow5kFWkrVn78evAArQu0B11v2877ve7tcOHZyrNjdYgGio438nio5Y5mXoD5NdmHwTh7rAYUlKL1R1N90j6-TfNh4nltheZAPqGGuPyD-Y6pEZfUF2OlV7pRlnfEe-0zbn0wtgLQNs7RzGnpQWe0B57VHDnUQCgchIAnFO6T0Mswyan-lgi+KF+oa0mJZGAYCKHaSJuLSaCdTIixJpgNOrNhxmDbvAMqeAIzYG5oQS8gt5xmRrgw0K4VIq5CMEw4OUdrwgF4VAOYRgDjAIKmxDeQMlFcRUWokhR8zZSIBIYoBw9aEYWoRo6OOkJpyKTiwxhi0gA
