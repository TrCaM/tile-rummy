package project.rummy.networks;

public class GameServerMain {
  public static void main(String[] args) throws InterruptedException {
    int port = 9999;
    TileRummyServer tileRummyServer = new TileRummyServer();
    tileRummyServer.start(port);
    System.out.println(String.format("Server is starting at port %s", port));
  }
}
