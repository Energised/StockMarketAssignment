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

    private String client_id;

    // logs the client in, gets their id
    public Client() throws Exception
    {
        Socket socket = new Socket("localhost", PORT);

        reader = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);

        // now receive success message and trader id
        String msg = reader.nextLine();
        String[] msg_tokens = msg.split(" ");
        if(msg_tokens[0].compareToIgnoreCase("success") != 0)
        {
            throw new Exception(msg);
        }
        String connect_msg = msg_tokens[0];
        client_id = msg_tokens[1];
    }

    // return current state of the market
    public String[] refresh_market()
    {
        // ask server
        writer.println("REFRESH");

        // except back a number (string form)
        // then that many accounts as lines
        // their id, followed by true/false if they own the stock
        String next_line = reader.nextLine();
        int size = Integer.parseInt(next_line);

        // get account details
        String[] market = new String[size];
        for(int i=0; i<size; i++)
        {
            next_line = reader.nextLine();
            market[i] = next_line;
        }

        return market;
    }

    public int get_stock_owner()
    {
        writer.println("CHECK");
        // get int as a response
        String next_line = reader.nextLine();
        int stock_owner = Integer.parseInt(next_line);
        return stock_owner;
    }

    public void trade(int seller, int buyer) throws Exception
    {
        writer.println("TRADE " + seller + " " + buyer);
        // wait for successful response
        String response = reader.nextLine();
        if(response.trim().compareToIgnoreCase("success") != 0)
        {
            // failed trade is dealt with server-side
            // no need to throw exception and end the client
            //throw new Exception(response);
            System.out.println(response);
        }
        // TODO: write an update command to the server, getting all clients to refresh
        // TODO: their market variable via ClientProgram
    }

    public void leave() throws Exception
    {
        writer.println("LEAVE");
        System.exit(0);
    }


    // get method for client_id
    public String get_client_id()
    {
        return client_id;
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