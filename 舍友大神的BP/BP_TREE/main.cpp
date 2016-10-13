#include <iostream>
#include "BP.cpp"
#define read freopen("in.txt","r",stdin)
using namespace std;
void print(double* sr,int n)
{
    int len = n;
    for(int i=0; i< len; i++)
      printf("%lf ",sr[i]);
    puts("");
}
int main()
{
    read;
    BP bp(10,5,10,0.1);
    bp.getXY();
    bp.printXY();
    bp.getInitVWSG();
    //bp.printVMSG();
    puts("");
    int ci = 0;
    const int ds =600;
    do
    {
      bp.BPSolve();
      ci++;
    }while(ci<=ds);
    print(bp.getXr(),bp.getD());
    print(bp.getYr(),bp.getL());
    print(bp.getYlr(),bp.getL());
    return 0;
}
