/**
 * Client.java
 * @author Dan Woolsey
 *
 * Implementation of Client that will connect to the StockMarket server
 */

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements AutoCloseable
{

    private final int PORT = 8888;

    private final Scanner reader;
    private final PrintWriter writer;

    public Client() throws Exception
    {
        Socket socket = new Socket("localhost", PORT);

        reader = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);

        // now receive success message and trader id
        String msg = reader.nextLine();
        String[] msg_tokens = msg.split(" ");
        System.out.println(msg_tokens[0]);
        System.out.println(msg_tokens[1]);

    }

    @Override
    public void close()
    {
        reader.close();
        writer.close();
    }

    public static void main(String[] args) throws Exception
    {
        Client c = new Client();
    }
}