#include "Class1.hpp"

//In C++ findet die Implementierung der Methoden normalerweise in der cpp-Datei statt

//Konstruktor
Class1::Class1(Class4* iClass4, Class5* iClass5)
{
	this->iClass4 = iClass4; //Aggregationen zu Class4
	this->iClass5 = iClass5;//Aggregationen zu Class5
}

int Class1::method1(int param1, int param2)
{
	int a = 0;
	int b = 1;
	if (a != b)
	{
		classInstance1.call1();	//Aufruf der Instanz-Methode
	}
	else
	{
		for (int i = 0; i != 100; i++)
		{
			Class2::call2();		//Aufruf der statischen Methode
		}
	}
	this->method2();

	return 1;
}

void Class1::method2()
{
	int i = 0;
	switch (i)
	{
	case 0:
	{
		int j = 10;
		while (j != 0)
		{
			method3();
			j--;
		}
		break;
	}
	case 1:
	{
		int j = 10;
		do
		{
			method3();
			j--;
		}
		while (j != 0);
		break;
	}
	default:
	{
		Class2 classInstance3 = new Class2();
		Class3 classInstance1 = new Class3();	//Wegen dieser Instanz besteht eine Komposition zu Class3
		classInstance1.call1();	//Sollte Class3 verwenden
		this->classInstance1.call1();	//Sollte Class2 verwenden
		classInstance2.call1();
		classInstance3.call1();
		classInstance4.callX();

		break;
	}
	}
}

void Class1::method3()
{
	int a = 0;
	int b = 1;
	int c = 1;
	if (a != b)
	{
		method3();	//Rekursiver selbstaufruf
	}

	if (b != c)
	{
		method4();	//Rekursiver wechselaufruf (Teil1)
	}
}

void Class1::method4()
{
	int d = 0;
	int e = 1;
	if (d != e)
	{
		method3();	//Rekursiver wechselaufruf (Teil2)
	}
}
