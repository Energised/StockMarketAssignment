/**
 * Trader.java
 * @author Dan Woolsey
 *
 * Implementation of Trader object used in StockMarket
 */


public class Trader
{
    private static int ID_GENERATOR = 1;

    private int trader_id;
    private boolean stock_owned;

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

    public boolean get_stock_owned()
    {
        return this.stock_owned;
    }

    public void set_stock_owned(boolean val)
    {
        this.stock_owned = val;
    }

    public String toString()
    {
        return this.get_id() + " " + this.get_stock_owned();
    }

    public static void main(String[] args)
    {
        Trader t1 = new Trader();
        Trader t2 = new Trader();
        Trader t3 = new Trader();
        Trader t4 = new Trader();

        t4.set_stock_owned(true);

        System.out.println(t1);
        System.out.println(t2);
        System.out.println(t3);
        System.out.println(t4);
    }
}
