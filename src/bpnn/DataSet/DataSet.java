package bpnn.DataSet;

import java.util.ArrayList;

/**
 * Created by zsh96 on 2016/10/1.
 */
public class DataSet {
    public ArrayList<DataGroup> groups;
    public int xn,yn;

    public DataSet(int xn, int yn) {
        this.xn = xn;
        this.yn = yn;
        groups=new ArrayList<>();
    }
}
