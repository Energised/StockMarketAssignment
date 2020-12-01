using System;
using System.IO;
using System.Net.Sockets;

namespace TradingClientCS
{
    class Client : IDisposable
    {
        const int port = 8888;

        private readonly StreamReader reader;
        private readonly StreamWriter writer;

        private String client_id;

        public Client()
        {
            // connect to the server
            TcpClient tcp_client = new TcpClient("localhost", port);
            NetworkStream stream = tcp_client.GetStream();
            reader = new StreamReader(stream);
            writer = new StreamWriter(stream);

            // recieve success message and trader_id
            string msg = reader.ReadLine();
            string[] msg_tokens = msg.Split(" ");
            if(msg_tokens[0].ToLower() != "success")
            {
                throw new Exception(msg);
            }
            string connect_msg = msg_tokens[0];
            client_id = msg_tokens[1];
        }

        public string[] refresh_market()
        {
            // ask server for data
            writer.WriteLine("REFRESH");
            writer.Flush();

            // first read = number of traders
            // then that many lines of trader info
            string next_line = reader.ReadLine();
            int size = int.Parse(next_line);

            string[] market = new string[size];
            for(int i=0; i<size; i++)
            {
                next_line = reader.ReadLine();
                market[i] = next_line;
            }
            return market;
        }

        public int get_stock_owner()
        {
            writer.WriteLine("CHECK");
            writer.Flush();
            // get int as response
            string next_line = reader.ReadLine();
            int stock_owner = int.Parse(next_line);
            return stock_owner;
        }

        public void trade(int seller, int buyer)
        {
            writer.WriteLine("TRADE " + seller + " " + buyer);
            writer.Flush();

            string response = reader.ReadLine();
            if(response.Trim().ToLower() != "success")
            {
                // failed trades are dealt with server-side
                // parameters that can cause failure are dealt with in Program
                // so just output the server response here
                // and no data will be modified server-side
                Console.WriteLine(response);
            }
        }

        public void leave()
        {
            writer.WriteLine("LEAVE");
            writer.Flush();
            System.Environment.Exit(0);
            // TODO: might have to use Environment.Exit but will test this
        }

        public string get_client_id()
        {
            return client_id;
        }

        public void Dispose()
        {
            reader.Close();
            writer.Close();
        }
    }
}