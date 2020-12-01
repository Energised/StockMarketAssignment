using System;

namespace TradingClientCS
{
    class Program
    {

        static string line_break = "-----------------------------------------------";

        static void menu_options()
        {
            Console.WriteLine("(1) Get the current state of the market");
            Console.WriteLine("(2) Check who currently owns the stock");
            Console.WriteLine("(3) Make a trade (if you own the stock)");
            Console.WriteLine("(4) Leave the market");
        }

        static void print_market(string[] market)
        {
            Console.WriteLine(line_break);
            Console.WriteLine("CURRENT MARKET:");
            foreach(string trader in market)
            {
                Console.WriteLine(trader);
            }
            Console.WriteLine(line_break);
        }

        static void Main(string[] args)
        {
            Console.WriteLine("Logging you into the market...");
            try
            {
                using(Client client = new Client())
                {
                    Console.WriteLine("Complete!");
                    Console.WriteLine(line_break);
                    Console.WriteLine("Welcome to the Stock Market - Trader " + client.get_client_id());
                    // pull current market state
                    string[] market = client.refresh_market();
                    print_market(market);

                    while(true)
                    {
                        Console.WriteLine("Welcome Trader " + client.get_client_id());
                        Console.WriteLine(line_break);
                        menu_options();
                        // get user input
                        int choice = int.Parse(Console.ReadLine());

                        switch(choice)
                        {
                            case 1:
                                // update market instance and display
                                market = client.refresh_market();
                                print_market(market);
                                break;
                            case 2:
                                // get current stock owner
                                int stock_owner = client.get_stock_owner();
                                Console.WriteLine(line_break);
                                Console.WriteLine("ID of current stock owner: " + stock_owner);
                                Console.WriteLine(line_break);
                                break;
                            case 3:
                                int seller = int.Parse(client.get_client_id());
                                if(client.get_stock_owner() != seller)
                                {
                                    Console.WriteLine("ERROR: Seller doesn't own the stock");
                                    continue;
                                }
                                Console.WriteLine("Enter ID of trader buying the stock: ");
                                int buyer = int.Parse(Console.ReadLine());
                                client.trade(seller, buyer);
                                break;
                            case 4:
                                client.leave();
                                break;
                            default:
                                Console.WriteLine("Please enter a valid option");
                                break;

                        }
                    }
                }
            }
            catch(Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }
    }
}