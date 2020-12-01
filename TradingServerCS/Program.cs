using System;
using System.Collections;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Threading;

namespace TradingServerCS
{
    class Program
    {

        private const int port = 8888;

        private static readonly StockMarket stock_market = new StockMarket();

        static void Main(string[] args)
        {
            //stock_market.add_trader(new Trader());
            //stock_market.add_trader(new Trader());

            run_server();
        }

        private static void run_server()
        {
            TcpListener listener = new TcpListener(IPAddress.Loopback, port);
            listener.Start();
            Console.WriteLine("Waiting for incoming connections...");
            while(true)
            {
                TcpClient tcp_client = listener.AcceptTcpClient();
                new Thread(handle_incoming_connection).Start(tcp_client);
            }
        }

        private static void handle_incoming_connection(object param)
        {
            TcpClient tcp_client = (TcpClient) param;
            using(Stream stream = tcp_client.GetStream())
            {
                StreamWriter writer = new StreamWriter(stream);
                StreamReader reader = new StreamReader(stream);
                // set up trader object for new connection
                Trader t = new Trader();
                // if no traders in market, give stock to new connection
                if(stock_market.get_traders_dict().Count == 0)
                {
                    t.set_stock_owned(true);
                }
                try
                {
                    stock_market.add_trader(t);
                    Console.WriteLine("Trader: " + t.get_id() + " has connected");
                    // check if ToString is called automatically on stock_market
                    Console.WriteLine(stock_market);
                    writer.WriteLine("SUCCESS " + t.get_id());
                    writer.Flush();

                    while(true)
                    {
                        string line = reader.ReadLine();
                        string[] sub_line = line.Split(" ");
                        switch(sub_line[0].ToLower())
                        {
                            case "refresh":
                                // return market information
                                ArrayList market = stock_market.get_market();
                                writer.WriteLine(market.Count);
                                foreach(String market_line in market)
                                {
                                    writer.WriteLine(market_line);
                                }
                                writer.Flush();
                                break;
                            case "check":
                                // return id of trader who owns the stock
                                int stock_owner = stock_market.get_stock_owner();
                                writer.WriteLine(stock_owner);
                                writer.Flush();
                                break;
                            case "trade":
                                // make a stock trade given a seller_id and buyer_id
                                int seller = int.Parse(sub_line[1]);
                                int buyer = int.Parse(sub_line[2]);
                                if(stock_market.get_traders_dict().ContainsKey(buyer) == false)
                                {
                                    writer.WriteLine("FAIL: Buyer isn't in the market");
                                    writer.Flush();
                                    break;
                                }
                                stock_market.trade(seller, buyer);
                                Console.WriteLine("Stock was traded from " + seller + " to " + buyer);
                                writer.WriteLine("SUCCESS");
                                writer.Flush();
                                break;
                            case "leave":
                                // call leave method in StockMarket class
                                stock_market.leave();
                                break;
                            
                        }
                    }
                }
                catch(Exception e)
                {
                    try
                    {
                        writer.WriteLine("ERROR: " + e.Message);
                        writer.Flush();
                        tcp_client.Close();
                    }
                    catch
                    {
                        Console.WriteLine("Failed to send error message");
                    }
                }
                finally
                {
                    stock_market.get_traders_dict().Remove(t.get_id());
                    Console.WriteLine("Trader removed from market before leaving");
                    // if they own the stock, give it to someone else
                    if(t.get_stock_owned())
                    {
                        Console.WriteLine("Leaving trader owns the stock");
                        // if there are still traders in the stock market
                        if(stock_market.get_traders_dict().Count > 0)
                        {
                            int i = 1;
                            // while there is not a trader with trader_id = 1
                            // increment i until we get to a trader with trader_id = i
                            while(!stock_market.get_traders_dict().ContainsKey(i))
                            {
                                i++;
                            }
                            // now we can reassign the stock
                            stock_market.get_traders_dict()[i].set_stock_owned(true);
                            Console.WriteLine("Stock was owned by " + t.get_id() + " now owned by " + i);
                        }
                    }
                    Console.WriteLine("Trader " + t.get_id() + " has left");
                    Console.WriteLine(stock_market);
                }
            }
        }

        static void tests()
        {
            Trader t1 = new Trader();
            Trader t2 = new Trader();

            t1.set_stock_owned(true);

            Console.WriteLine(t1);
            Console.WriteLine(t2);

            StockMarket stock_market = new StockMarket();

            stock_market.add_trader(t1);
            stock_market.add_trader(t2);

            ArrayList market = stock_market.get_market();
            Console.WriteLine(market[0]);
            Console.WriteLine(market[1]);

            Console.WriteLine(stock_market.ToString());
        }
    }
}
