using System;

namespace TradingServerCS
{
    class Trader
    {

        private static int ID_GENERATOR = 1;

        private int trader_id;
        private Boolean stock_owned;

        public Trader()
        {
            this.trader_id = ID_GENERATOR;
            this.stock_owned = false;
            ID_GENERATOR++;
        }

        public int get_id()
        {
            return this.trader_id;
        }

        public Boolean get_stock_owned()
        {
            return this.stock_owned;
        }

        public void set_stock_owned(Boolean val)
        {
            this.stock_owned = val;
        }

        public override string ToString()
        {
            return this.get_id() + " " + this.get_stock_owned();
        }
    }
}