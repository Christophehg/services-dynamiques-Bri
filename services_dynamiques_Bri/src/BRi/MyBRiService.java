package BRi;

import java.net.Socket;

public class MyBRiService implements Service {
    private final Socket socket;

    public MyBRiService(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("MyBRiService is running...");
    }

    public static String toStringue() {
        return "MyBRiService is a valid BRi Service";
    }
}
