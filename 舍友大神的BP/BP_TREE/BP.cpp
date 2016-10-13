#include <cstdio>
#include <cstdlib>
#include <cmath>
#include <cstring>
#include <time.h>
#define maxn 111
using namespace std;
class BP
{
  public:
      BP(){}
      BP(int d,int q,int l,double nl)
      {
          this->d=d;
          this->q=q;
          this->l=l;
          this->nl=nl;
          memset(X,0,sizeof(X));
          memset(Xg,0,sizeof(Xg));
          memset(V,0,sizeof(V));
          memset(W,0,sizeof(W));
          memset(b,0,sizeof(b));
          memset(Y,0,sizeof(Y));
           memset(Yg,0,sizeof(Yg));
          memset(Yl,0,sizeof(Yl));
          memset(sta,0,sizeof(sta));
          memset(gma,0,sizeof(gma));
          memset(BF,0,sizeof(BF));
          memset(YB,0,sizeof(YB));
          memset(G,0,sizeof(G));
          memset(Eq,0,sizeof(Eq));
          memset(detaW,0,sizeof(detaW));
          memset(detaV,0,sizeof(detaV));
          memset(detasta,0,sizeof(detasta));
          memset(detagma,0,sizeof(detagma));
      }
      void getXY()
      {
         double MaxX = -999.0;
         double MinX = 999.0;
         double MaxY = -999.0;
         double MinY = 999.0;
         for(int i=0; i<d; i++)
         {
           scanf("%lf",&Xg[i]);
           scanf("%lf",&Yg[i]);
           if(MaxX<Xg[i]) MaxX = Xg[i];
           if(MaxY<Yg[i]) MaxY = Yg[i];
           if(MinX>Xg[i]) MinX = Xg[i];
           if(MinY>Yg[i]) MinY = Yg[i];
           /*
           MaxX = max(MaxX,Xg[i]);
           MaxY = max(MaxY,Yg[i]);
           MinX = min(MinX,Xg[i]);
           MinY = min(MinY,Yg[i]);
           */
         }
         for(int i=0; i<d; i++)
         {
             X[i] = Xg[i]/(MaxX-MinX+1.0);
             Y[i] = Yg[i]/(MaxY-MinY+1.0);
         }
      }
      void printXY()
      {
          for(int i=0; i<d; i++)
            printf("%lf %lf\n",X[i],Y[i]);
          puts("");
      }
      void getInitVWSG()
      {
         srand((unsigned)time(NULL));
         for(int i=0;i<d;i++)
           for(int j=0;j<q;j++)
            V[i][j] = ((rand()%100)*1.000/100.0+0.35);
         for(int i=0; i<q; i++)
          for(int j=0; j<l; j++)
           W[i][j] = ((rand()%100)*1.000/100.0+0.35);
         for(int i=0; i<q; i++)
            gma[i] = ((rand()%100)*1.000/100.0+0.35);
         for(int i=0; i<l; i++)
            sta[i] = ((rand()%100)*1.000/100.0+0.35);
      }
      void printVMSG()
      {
         for(int i=0;i<d;i++,puts(""))
           for(int j=0;j<q;j++)
            printf("%lf ",V[i][j]);
         puts("");
         for(int i=0; i<q; i++,puts(""))
          for(int j=0; j<l; j++)
           printf("%lf ",W[i][j]);
         puts("");
         for(int i=0; i<q; i++)
            printf("%lf ",gma[i]);
         puts("");
         for(int i=0; i<l; i++)
            printf("%lf ",sta[i]);
         puts("");
      }
      void getB()
      {
         memset(b,0,sizeof(b));
         for(int i=0; i<q; i++)
          for(int j=0; j<d; j++)
           b[i] += X[j]*W[j][i];
      }
      double active(double x)
      {
          return (1.0/(1.0+1.0*exp(-1.0*x)));
      }
      void getBF()
      {
        memset(BF,0,sizeof(BF));
        for(int i=0; i<q; i++)
         BF[i] = active(b[i]-sta[i]);
      }
      void getYB()
      {
         memset(YB,0,sizeof(YB));
         for(int i=0; i<l; i++)
           for(int j=0; j<q; j++)
            YB[i] += BF[j]*W[j][i];
      }
      void getY()
      {
          memset(Yl,0,sizeof(Yl));
          for(int i=0; i<l; i++)
           Yl[i] = active(YB[i]-gma[i]);
      }
      void getGEDetaWVSG()
      {
         memset(G,0,sizeof(G));
         memset(Eq,0,sizeof(Eq));
         memset(detaW,0,sizeof(detaW));
         memset(detaV,0,sizeof(detaV));
         memset(detasta,0,sizeof(detasta));
         memset(detagma,0,sizeof(detagma));
         for(int i=0; i<l; i++)
         {
            G[i] = Yl[i]*(1-Yl[i])*(Y[i]-Yl[i]);
         }
         for(int i=0; i<q; i++)
         {
             double tp = 0.0;
             for(int j=0; j<l; j++)
              tp += W[i][j]*G[j];
             tp *= b[i]*(1.0-b[i]);
             Eq[i] = tp;
         }
         for(int i=0; i<q; i++)
          for(int j=0; j<l; j++)
            {
                detaW[i][j] = nl*G[j]*b[i];
                W[i][j] += detaW[i][j];
            }
         for(int i=0; i<l; i++)
            {
                detasta[i] = -1.0*nl*G[i];
                sta[i] += detasta[i];
            }
         for(int i=0; i<d; i++)
           for(int j=0; j<q;j++)
            {
                detaV[i][j] = nl*Eq[j]*X[i];
                V[i][j] += detaV[i][j];
            }
         for(int i=0; i<q; i++)
            {
                detagma[i] = -1.0*nl*Eq[i];
                gma[i] += detagma[i];
            }
      }
  void BPSolve()
  {
     getB();
     getBF();
     getYB();
     getY();
     getGEDetaWVSG();
  }
  double* getYr()
  {
      return Y;
  }
  double* getYlr()
  {
      return Yl;
  }
  double* getXr()
  {
      return X;
  }
int getD()
{
  return this->d;
}
int getL()
{
   return this->l;
}
bool Error()
  {
      double res = 0.0;
      for(int i=0; i<l; i++)
      {
          res += (Y[i]-Yl[i])*(Y[i]-Yl[i]);
      }
      res /= 2.0;
      if(res<=0.3) return true;
      return false;
  }
  private:
      double X[maxn];//归一化之后的输入
      double Xg[maxn];//原始输入
	  //
      double Y[maxn];//归一化之后的输出
      double Yg[maxn];//原始输出
	  double Yl[maxn];//预测输出
	  //
      double V[maxn][maxn];//隐含层权值
      double sta[maxn];//隐含层阈值
	  double b[maxn];//隐含层和值
	  double BF[maxn];//隐含层输出
	  double Eq[maxn];//隐含层均差
	  //
	  double W[maxn][maxn];//输出层权值
      double gma[maxn];//输出层阈值
      double YB[maxn];//输出层和值
      double G[maxn];//输出层均差
      //
      double detaW[maxn][maxn];//输出层权值增量
      double detaV[maxn][maxn];//隐含层权值增量
      double detasta[maxn];//隐含层阈值增量
      double detagma[maxn];//输出层阈值增量
      int d;//数据个数
	  int q;//隐含层数
	  int l;//输出层数
      double nl;//学习率

};
