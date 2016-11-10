package bpnn.DataSet;

/**
 * Created by zsh_o on 2016/10/1.
 */
public class DataGroup implements Cloneable{
    public double[] inputs;
    public double[] outputs;

    int xn,yn;

    public DataGroup(int xn, int yn) {
        this.xn = xn;
        this.yn = yn;
        inputs=new double[xn];
        outputs=new double[yn];
    }

    @Override
    protected DataGroup clone() throws CloneNotSupportedException {
        DataGroup dg=new DataGroup(xn,yn);
        dg.inputs=inputs.clone();
        dg.outputs=outputs.clone();

        return dg;
    }
}
