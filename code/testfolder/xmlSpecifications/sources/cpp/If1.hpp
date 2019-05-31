#include <iostream>

using namespace std;

//In C++ ist ein Interface eine Klasse die nur nicht implementierte, virtuelle methoden beinhaltet
class If1
{
public:
	//Virtuelle Methode. Das "= 0" am ende kennzeichnet die Methode als nicht implementiert
	virtual int method1(int param1, int param2) = 0;
};
