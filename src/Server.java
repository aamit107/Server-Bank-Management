import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("Server waiting ...");

        while (true){
            Socket sc = serverSocket.accept();
            System.out.println("client connected.");

            Client client = new Client(sc);

            Thread t = new Thread(client);
            t.start();
        }
    }
}
