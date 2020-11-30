/**
 * ClientProgram.java
 * @author Dan Woolsey
 *
 * Class to be executed by a client connecting to the StockMarket server
 */

import java.util.Scanner;

public class ClientProgram
{

    public static String line_break = "-----------------------------------------------";

    public static void menu_options()
    {
        System.out.println("(1) Get the current state of the market");
        System.out.println("(2) Check who currently owns the stock");
        System.out.println("(3) Make a trade (if you own the stock)");
        System.out.println("(4) Leave the market");
    }

    public static void print_market(String[] market)
    {
        System.out.println(line_break);
        System.out.println("CURRENT MARKET:");
        for(String trader : market)
        {
            System.out.println(trader);
        }
        System.out.println(line_break);
    }

    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        System.out.println("Logging you into the market...");
        try(Client client = new Client())
        {
            System.out.println("Complete!");
            System.out.println(line_break);
            System.out.println("Welcome to the Stock Market - Trader " + client.get_client_id());
            // pull current state of market from Client
            String[] market = client.refresh_market();
            // made its own function, save on redundant code
            print_market(market);
            // market state info displayed
            // now read in menu options choice
            while(true)
            {
                System.out.println("Welcome Trader " + client.get_client_id());
                System.out.println(line_break);
                menu_options();
                // wait for user input
                int choice = Integer.parseInt(in.nextLine());
                // switch/case
                switch(choice)
                {
                    case 1:
                        // update market instance and display
                        market = client.refresh_market();
                        print_market(market);
                        break;
                    case 2:
                        int stock_owner = client.get_stock_owner();
                        System.out.println(line_break);
                        System.out.println("ID of current stock owner: " + stock_owner);
                        System.out.println(line_break);
                        break;
                    case 3:
                        int seller = Integer.parseInt(client.get_client_id());
                        if(client.get_stock_owner() != seller)
                        {
                            System.out.println("ERROR: Seller doesn't own the stock");
                            continue;
                        }
                        System.out.println("Enter ID of trader buying the stock: ");
                        // TODO: check if buyer exists in market
                        // TODO: do in either this/StockMarket/ClientHandler
                        int buyer = Integer.parseInt(in.nextLine());
                        client.trade(seller, buyer);
                        break;
                    case 4:
                        client.leave();
                        break;
                    default:
                        System.out.println("Please enter a valid option");
                        break;
                }
            }


        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
