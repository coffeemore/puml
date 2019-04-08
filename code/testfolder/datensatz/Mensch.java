public class Mensch extends Lebewesen
{
    public Mensch(String name, int alter)
    {
        super(name, alter);
    }

    @Override
    public void bewegen()
    {
        System.out.println(getName() +  " fährt Segway.");
    }

    @Override
    public void essen()
    {
        System.out.println(getName() + " isst Fastfood.");
    }

    public void arbeiten()
    {
        System.out.println(getName() + " ist fleißig.");
    }
}
