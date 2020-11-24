/**
 * TradingServer.java
 * @author Dan Woolsey
 *
 * Implementation of a server accepting multiple clients to trade a single stock
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TradingServer
{

    private final static int PORT = 8888;

    private final static StockMarket stock_market = new StockMarket();

    public static void run_server()
    {
        ServerSocket server_socket = null;
        try
        {
            server_socket = new ServerSocket(PORT);
            System.out.println("Listening for incoming connections...");
            while(true)
            {
                Socket socket = server_socket.accept();
                new Thread(new ClientHandler(socket, stock_market)).start();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        // setup default traders
        Trader t1 = new Trader();
        Trader t2 = new Trader();
        Trader t3 = new Trader();
        Trader t4 = new Trader();

        t1.set_stock_owned(true);

        stock_market.add_trader(t1);
        stock_market.add_trader(t2);
        stock_market.add_trader(t3);
        stock_market.add_trader(t4);

        run_server();
    }
}
