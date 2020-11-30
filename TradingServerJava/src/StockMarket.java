/**
 * StockMarket.java
 * @author Dan Woolsey
 *
 * Implementation of StockMarket containing Trader instances and allowing trades
 * to take place
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StockMarket
{
    // will remove traders Arraylist once Map is fully tested
    //private ArrayList<Trader> traders = new ArrayList<Trader>();
    private Map<Integer, Trader> traders_map = new HashMap<Integer, Trader>();


    // TODO: do i replace this with a create_trader function like in
    // TODO: the bank server example? will think...
    public void add_trader(Trader t)
    {
        //traders.add(t);
        traders_map.put(t.get_id(), t);
    }

    //public ArrayList<Trader> get_traders()
    //{
    //   return traders;
    //}

    public Map<Integer, Trader> get_traders_map()
    {
        return traders_map;
    }

    public ArrayList<String> get_market()
    {
        ArrayList<String> market = new ArrayList<String>();
        //ArrayList<Trader> traders = get_traders();
        for(Trader t : get_traders_map().values())
        {
            String line = t.toString();
            market.add(line);
        }
        return market;
    }

    public int get_stock_owner()
    {
        int stock_owner = 0;
        //ArrayList<Trader> traders = get_traders();
        for(Trader t : get_traders_map().values())
        {
            if(t.get_stock_owned() == true)
            {
                stock_owner = t.get_id();
            }
        }
        return stock_owner;
    }

    public void trade(int seller, int buyer) throws Exception
    {
        // using synchronized block, will try as method later
        // notes say synchronized methods are v slow iirc
        synchronized(traders_map)
        {
            get_traders_map().get(seller).set_stock_owned(false);
            get_traders_map().get(buyer).set_stock_owned(true);
            //for(Trader t : traders)
            //{
            //    if(t.get_id() == seller)
            //    {
            //        t.set_stock_owned(false);
            //    }
            //    if(t.get_id() == buyer)
            //    {
            //        t.set_stock_owned(true);
            //    }
            //}
        }
    }

    public void leave() throws Exception
    {
        throw new Exception("Cleaning up and exiting...");
    }

    public String toString()
    {
        String result = "Number of Traders = " + get_traders_map().size() + "\n";
        for(Trader t : get_traders_map().values())
        {
            result += t.toString();
            result += "\n";
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

        try
        {
            stockMarket.trade(1,3);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

        stockMarket.add_trader(new Trader());

        System.out.println(stockMarket);


    }
}
