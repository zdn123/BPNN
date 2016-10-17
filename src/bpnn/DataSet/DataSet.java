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

    public DataSet(int xn, int yn) {
        this.xn = xn;
        this.yn = yn;
        trainGroups =new ArrayList<>();
        testGroups=new ArrayList<>();
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

}
