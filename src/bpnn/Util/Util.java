package bpnn.Util;

import bpnn.DataSet.DataSet;

/**
 * Created by zsh_o on 2016/10/1.
 */
public class Util {
    public static double dot(double[] X,double[] Y) throws Exception {
        int m=X.length;
        int n=Y.length;
        if(m!=n)
            throw new Exception("向量格式不一致");
        double sum=0;
        for(int i=0;i<m;i++)
            sum+=X[i]*Y[i];
        return sum;
    }

    public static DataSet convertDataSet(String text){
        return null;
    }
}
