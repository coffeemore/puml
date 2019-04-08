public abstract class Tier extends Lebewesen
{
    public Tier(String name, int alter)
    {
        super(name, alter);
    }
    @Override
    public void bewegen()
    {
        System.out.println(getName() + " bewegt sich.");
    }
    @Override
    public void essen()
    {
        System.out.println(getName() + " frisst.");
    }
}
