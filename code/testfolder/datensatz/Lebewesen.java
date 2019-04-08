public abstract class Lebewesen
{
    private String name;
    private int alter;

    public Lebewesen(String name, int alter)
    {
        this.name = name;
        this.alter = alter;
    }

    public abstract void bewegen();
    public abstract void essen();

    public String getName()
    {
        return name;
    }

    public int getAlter()
    {
        return alter;
    }
}
