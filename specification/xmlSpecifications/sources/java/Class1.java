package xmlSpecifications;



public class Class1 extends Class3 implements If1, If2 
{
    private Class2 classInstance1 = new Class2();	//Komposition zu Class2
    protected Class2 classInstance2 = new Class2();	//Komposition zu Class2
    
    public Class4 iClass4;
    Class5 iClass5;
    
    private int var1;
    protected double var2;
    public short var3;
    byte var4;
    
    public Class1(Class4 iClass4, Class5 iClass5)
    {
	this.iClass4 = iClass4; //Agregation zu Class4
	this.iClass5 = iClass5; //Agregation zu Class5
    }
    
    private int method1(int param1, int param2)
    {
	int a = 0;
	int b = 1;
	if(a!=b)
	{
	    classInstance1.call1();	//Aufruf der Instanz-Methode
	}
	else
	{
	    for(int i=0; i!=100; i++)
	    {
		Class2.call2();		//Aufruf der statischen Methode
	    }
	}
	this.method2();
	
	return 1;
    }
    
    protected void method2()
    {
	int i=0;
	switch(i)
	{
		case 0:
		{
		    int j=10;
		    while(j!=0)
		    {
			method3();
			j--;
		    }
		    break;
		}
		case 1:
		{
		    int j=10;
		    do
		    {
			method3();
			j--;
		    }
		    while(j!=0);
		    break;
		}
		default:
		{
		    Class2 classInstance3 = new Class2();
		    Class3 classInstance1 = new Class3();	//Wegen dieser Instanz besteht eine Komposition zu Class3	
		    classInstance1.call1();	//Sollte Class3 verwenden
		    this.classInstance1.call1();	//Sollte Class2 verwenden
		    classInstance2.call1();
		    classInstance3.call1();
		    classInstance4.callX();
		    
		    break;
		}
	}
    }
    
    public void method3()
    {
	int a = 0;
	int b = 1;
	int c = 1;
	if(a!=b)
	{
	    method3();	//Rekursiver selbstaufruf
	}
	
	if(b!=c)
	{
	    method4();	//Rekursiver wechselaufruf (Teil1)
	}
    }
    
    void method4()
    {
	int d = 0;
	int e = 1;
	if(d!=e)
	{
	    method3();	//Rekursiver wechselaufruf (Teil2)
	}
    }
}
