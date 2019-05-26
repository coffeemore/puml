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
private:
    Class2* classInstance1 = new Class2();	//Komposition zu Class2
    Class2* classInstance2 = new Class2();

    Class4* iClass4;
    Class5* iClass5;

public:
    //Konstruktor
    Class1(Class4* iClass4, Class5* iClass5);

    int method1(int param1, int param2);

    void method2();

    void method3();

    void method4();
};
