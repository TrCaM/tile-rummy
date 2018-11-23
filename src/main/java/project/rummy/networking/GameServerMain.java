package project.rummy.networking;

public class GameServerMain {

  public static void main(String[] args) throws InterruptedException {
    int port = 9999;
    WebSocketServer tileRummyServer = new WebSocketServer();
    tileRummyServer.start(port);
    System.out.println(String.format("Server is starting at port %s", port));

  }
}
