package bpnn;

/**
 * Created by zsh96 on 2016/10/1.
 */
public abstract class Layer {
    public int nodeNumber;
    public int layerCount;
    public Node[] nodes;
    public Layer(int nodeNumber,int layerCount) {
        this.nodeNumber=nodeNumber;
        this.layerCount=layerCount;
    }

    public double[] getOutput(){
        double[] outputs =new double[nodeNumber];
        for(int i=0;i<nodeNumber;i++){
            outputs[i]=nodes[i].x;
        }

        return outputs;
    }
}
