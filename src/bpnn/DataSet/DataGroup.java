package bpnn.DataSet;

/**
 * Created by zsh96 on 2016/10/1.
 */
public class DataGroup {
    public double[] inputs;
    public double[] outputs;
    int xn,yn;

    public DataGroup(int xn, int yn) {
        this.xn = xn;
        this.yn = yn;
        inputs=new double[xn];
        outputs=new double[yn];
    }
}
