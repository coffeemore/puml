#include <iostream>
#include "Class2.hpp"
#include "Class3.hpp"
#include "Class4.hpp"
#include "Class5.hpp"
#include "If1.hpp"
#include "If2.hpp"

using namespace std;

//Es gibt in c++ keine Interfaces, daher werden diese durch die Mehrfachvererbung realisiert.
//Interfaces werden daran erkannt, dass die Klassen nur Virtuelle Methoden beinhalten.
class Class1 : public Class3, public If1, public If2
{

	/*ACHTUNG: C++ kennt keine paketsichtbarkeit, Instanzen, Variablen und Funktionen
	die sich in keinem definierten Abschnitt befinden werden "private"! classInstance1 und var1 sind also "private" */
    Class2* classInstance1 = new Class2();	//Komposition zu Class2
    int var1;

protected:
	Class2* classInstance2 = new Class2();
	double var2;

public:
	Class4* iClass4;
	short var3;

private:
    Class5* iClass5;
    uint8_t var4;


public:
    //Konstruktor
    Class1(Class4* iClass4, Class5* iClass5);

private:
    int method1(int param1, int param2);

protected:
    void method2();

public:
    void method3();

private:
    void method4();
};
