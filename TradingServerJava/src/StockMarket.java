/**
 * StockMarket.java
 * @author Dan Woolsey
 *
 * Implementation of StockMarket containing Trader instances and allowing trades
 * to take place
 */

import java.util.ArrayList;

public class StockMarket
{

    private ArrayList<Trader> traders = new ArrayList<Trader>();


    // TODO: do i replace this with a create_trader function like in
    // TODO: the bank server example? will think...
    public void add_trader(Trader t)
    {
        traders.add(t);
    }

    public ArrayList<Trader> get_traders()
    {
        return traders;
    }

    public void trade(int seller, int buyer)
    {
        synchronized(traders)
        {
            Trader trader_s, trader_b;
            for(Trader t : traders)
            {
                if(t.get_id() == seller)
                {
                    t.set_stock_owned(false);
                }
                if(t.get_id() == buyer)
                {
                    t.set_stock_owned(true);
                }
            }
        }
    }

    public String toString()
    {
        String result = "Market\nNumber of Traders = " + get_traders().size() + "\n";
        for(Trader t : get_traders())
        {
            result += t.toString();
        }
        return result;
    }

    public static void main(String[] args)
    {
        Trader t1 = new Trader();
        Trader t2 = new Trader();
        Trader t3 = new Trader();
        Trader t4 = new Trader();

        t1.set_stock_owned(true);

        StockMarket stockMarket = new StockMarket();
        stockMarket.add_trader(t1);
        stockMarket.add_trader(t2);
        stockMarket.add_trader(t3);
        stockMarket.add_trader(t4);

        System.out.println(stockMarket);

        stockMarket.trade(1,3);
        stockMarket.add_trader(new Trader());

        System.out.println(stockMarket);


    }
}
