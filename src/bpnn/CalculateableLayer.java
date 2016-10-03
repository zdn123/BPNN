package bpnn;

/**
 * Created by zsh96 on 2016/10/1.
 */
public class CalculateableLayer extends Layer {
    public CalculateableLayer(int nodeNumber, int layerCount,int weightNumber) {
        super(nodeNumber, layerCount);

        nodes=new CalculateableNode[nodeNumber];
        for(int i=0;i<nodeNumber;i++){
            nodes[i]=new CalculateableNode(nodeNumber,weightNumber);
        }
    }
}
