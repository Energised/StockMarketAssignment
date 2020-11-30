/**
 * ClientHandler.java
 * @author Dan Woolsey
 *
 * Implementation of class that will run on a thread
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
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
        if(stock_market.get_traders_map().size() == 0)
        {
            t.set_stock_owned(true);
        }
        try
        {
            Scanner scanner = new Scanner(socket.getInputStream());
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // establish connection and start event loop
            try
            {
                stock_market.add_trader(t);
                System.out.println("Trader: " + t.get_id() + " has connected");
                System.out.println(stock_market);
                writer.println("SUCCESS " + t.get_id());

                while(true)
                {
                    String line = scanner.nextLine();
                    String[] sub_line = line.split(" ");
                    switch(sub_line[0].toLowerCase())
                    {
                        case "refresh":
                            ArrayList<String> market = stock_market.get_market();
                            writer.println(market.size());
                            for(String market_line : market)
                            {
                                writer.println(market_line);
                            }
                            break;
                        case "check":
                            int stock_owner = stock_market.get_stock_owner();
                            writer.println(stock_owner);
                            break;
                        case "trade":
                            int seller = Integer.parseInt(sub_line[1]);
                            int buyer = Integer.parseInt(sub_line[2]);
                            if(stock_market.get_traders_map().get(buyer) == null)
                            {
                                writer.println("FAIL: Buyer isn't in the market");
                                break;
                            }
                            stock_market.trade(seller, buyer);
                            System.out.println("Stock was traded from " + seller + " to " + buyer);
                            writer.println("SUCCESS");
                            break;
                        case "leave":
                            stock_market.leave();
                            break;
                    }
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
            // remove user from the stock market
            stock_market.get_traders_map().remove(t.get_id());
            System.out.println("trader removed from market before leaving");
            // if leaving user owns the stock, give it to someone else
            if(t.get_stock_owned())
            {
                System.out.println("leaving trader owns the stock");
                // while loop, check for null entrys in the map
                // increment i until the first not-null trader is available
                if(stock_market.get_traders_map().size() > 0)
                {
                    int i = 1;
                    while(stock_market.get_traders_map().get(i) == null)
                    {
                        i++;
                    }
                    // give them the stock
                    stock_market.get_traders_map().get(i).set_stock_owned(true);
                    System.out.println("Stock was owned by " + t.get_id() + " now owned by " + i);
                }
                //System.out.println(stock_market.get_traders_map().size());
            }
            System.out.println("Trader " + t.get_id() + " has left");
            System.out.println(stock_market);
        }
    }
}
