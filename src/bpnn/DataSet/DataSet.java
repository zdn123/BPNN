package bpnn.DataSet;

import sample.Main;

import java.util.ArrayList;

/**
 * Created by zsh96 on 2016/10/1.
 */
public class DataSet {
    public ArrayList<DataGroup> trainGroups;
    public ArrayList<DataGroup> testGroups;
    public int xn,yn;

    public double maxX[];
    public double minX[];
    public double maxY[];
    public double minY[];

    public  double omaxX,ominX;
    public double omaxY,ominY;

    public DataSet(int xn, int yn) {
        this.xn = xn;
        this.yn = yn;
        trainGroups =new ArrayList<>();
        testGroups=new ArrayList<>();

        maxX=new double[xn];
        minX=new double[xn];
        maxY=new double[yn];
        minY=new double[yn];
    }

    public void sortGroups(ArrayList<DataGroup> groups,int n){
        for(int i=0;i<groups.size();i++){
            for(int j=0;j<i;j++){
                if(groups.get(j).inputs[n]>groups.get(j+1).inputs[n]){
                    DataGroup temp=groups.get(j);
                    groups.set(j,groups.get(j+1));
                    groups.set(j+1,temp);
                }
            }
        }
    }
    public void sortTrainGroup(){
        for(int i=0;i<xn;i++){
            sortGroups(trainGroups,i);
        }
    }
    public void generateTestGroups(int testrate){
        if(testrate==0)
            return;
        int k=trainGroups.size()/testrate;
        int i= Main.bpnn.random.nextInt(k);
        int c=trainGroups.size();
        for(;i<c;i+=k){
            testGroups.add(trainGroups.get(c-i));
            trainGroups.remove(c-i);
        }
    }
    public void generateOne(double maxx,double minx,double maxy,double miny){
        this.omaxX=maxx;
        this.omaxY=maxy;
        this.ominX=minx;
        this.ominY=miny;

        findMaxMin();
        for(int i=0;i<trainGroups.size();i++){
            for(int j=0;j<xn;j++){
                trainGroups.get(i).inputs[j]=(trainGroups.get(i).inputs[j]-minX[j])/(maxX[j]-minX[j])*(omaxX-ominX)+ominX;
            }
            for(int j=0;j<yn;j++){
                trainGroups.get(i).outputs[j]=(trainGroups.get(i).outputs[j]-minY[j])/(maxY[j]-minY[j])*(omaxY-ominY)+ominY;
            }
        }
    }

    public void generateOne(){
        this.omaxX=1;
        this.omaxY=1;
        this.ominX=0;
        this.ominY=0;

        findMaxMin();
        for(int i=0;i<trainGroups.size();i++){
            for(int j=0;j<xn;j++){
                trainGroups.get(i).inputs[j]=(trainGroups.get(i).inputs[j]-minX[j])/(maxX[j]-minX[j])*(omaxX-ominX)+ominX;
            }
            for(int j=0;j<yn;j++){
                trainGroups.get(i).outputs[j]=(trainGroups.get(i).outputs[j]-minY[j])/(maxY[j]-minY[j])*(omaxY-ominY)+ominY;
            }
        }
    }

    public void degenerateOne(){
        for(int i=0;i<trainGroups.size();i++){
            for(int j=0;j<xn;j++){
                trainGroups.get(i).inputs[j]=(trainGroups.get(i).inputs[j]-ominX)/(omaxX-ominX)*(maxX[j]-minX[j])+minX[j];

            }
            for(int j=0;j<yn;j++){
                trainGroups.get(i).outputs[j]=(trainGroups.get(i).outputs[j]-ominY)/(omaxY-ominY)*(maxY[j]-minY[j])+minY[j];
            }
        }
    }

    void findMaxMin(){
        for(int i=0;i<trainGroups.size();i++){
            for(int j=0;j<xn;j++){
                if(maxX[j]<trainGroups.get(i).inputs[j]){
                    maxX[j]=trainGroups.get(i).inputs[j];
                }
                if(minY[j]>trainGroups.get(i).inputs[j]){
                    minY[j]=trainGroups.get(i).inputs[j];
                }
            }
            for(int j=0;j<yn;j++){
                if(maxY[j]<trainGroups.get(i).outputs[j]){
                    maxY[j]=trainGroups.get(i).outputs[j];
                }
                if(minY[j]>trainGroups.get(i).outputs[j]){
                    minY[j]=trainGroups.get(i).outputs[j];
                }
            }
        }
    }

}
