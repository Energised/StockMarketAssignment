using System;
using System.Collections;
using System.Collections.Generic;

namespace TradingServerCS
{
    class StockMarket
    {
        private IDictionary<int, Trader> traders_dict = new Dictionary<int, Trader>();

        public void add_trader(Trader t)
        {
            traders_dict.Add(t.get_id(), t);
        }

        public IDictionary<int, Trader> get_traders_dict()
        {
            return traders_dict;
        }

        public ArrayList get_market()
        {
            ArrayList market = new ArrayList();
            foreach(Trader t in get_traders_dict().Values)
            {
                market.Add(t.ToString());
            }
            return market;
        }

        public int get_stock_owner()
        {
            int stock_owner = 0;
            foreach(Trader t in get_traders_dict().Values)
            {
                if(t.get_stock_owned())
                {
                    stock_owner = t.get_id();
                }
            }
            return stock_owner;
        }

        public void trade(int seller, int buyer)
        {
            lock(traders_dict)
            {
                traders_dict[seller].set_stock_owned(false);
                traders_dict[buyer].set_stock_owned(true);
            }
        }

        public void leave()
        {
            throw new Exception("Cleaning up and exiting...");
        }

        public override string ToString()
        {
            string result = "Number of Traders = " + get_traders_dict().Count + "\n";
            foreach(Trader t in get_traders_dict().Values)
            {
                result += t.ToString();
                result += "\n";
            }
            return result;
        }
    }
}