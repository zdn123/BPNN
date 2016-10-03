package bpnn;

/**
 * Created by zsh96 on 2016/10/1.
 */
public class InputLayer extends Layer{
    public InputLayer(int nodeNumber, int layerCount) {
        super(nodeNumber, layerCount);

        nodes=new InputNode[nodeNumber];
        for(int i=0;i<nodeNumber;i++){
            nodes[i]=new InputNode(layerCount);
        }
    }
}
