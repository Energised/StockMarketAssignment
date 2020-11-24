/**
 * ClientHandler.java
 * @author Dan Woolsey
 *
 * Implementation of class that will run on a thread
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable
{

    private final Socket socket;

    private StockMarket stock_market;

    public ClientHandler(Socket socket, StockMarket stock_market)
    {
        this.socket = socket;
        this.stock_market = stock_market;
    }

    @Override
    public void run()
    {
        Trader t = new Trader();
        try
        {
            Scanner scanner = new Scanner(socket.getInputStream());
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // establish connection and start event loop
            try
            {
                stock_market.add_trader(t);
                System.out.println("Trader: " + t.get_id() + " has connected");
                writer.println("SUCCESS " + t.get_id());

                while(true)
                {
                    // handle client requests here
                }
            }
            catch(Exception e)
            {
                writer.println("ERROR: " + e.getMessage());
                socket.close();
            }
        }
        catch(Exception e)
        {
            // fails to set up scanner or writer
            e.printStackTrace();
        }
        finally
        {
            System.out.println("Trader " + t.get_id() + " has left");
        }
    }
}
