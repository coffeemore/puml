public class Vogel extends Tier implements kannFliegen
{
    public Vogel(String name, int alter)
    {
        super(name, alter);
    }

    @Override
    public void fliegen()
    {
        System.out.println(getName() + " fliegt weit oben.");
    }

    @Override
    public void bewegen()
    {
        super.bewegen();
    }

    @Override
    public void essen()
    {
        System.out.println(getName() + " pickt einen saftigen Wurm aus der Erde.");;
    }
}
