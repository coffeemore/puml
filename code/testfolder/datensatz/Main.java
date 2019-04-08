public class Main
{
    public static void main(String[] args)
    {
        Mensch harry = new Mensch("Harry", 14);
        harry.bewegen();
        harry.essen();
        harry.arbeiten();

        Vogel eule = new Vogel("Hedwig", 3);
        eule.bewegen();
        eule.essen();
        eule.fliegen();
    }
}
