package sample;

import bpnn.BPNN;
import bpnn.DataSet.DataGroup;
import bpnn.DataSet.DataSet;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.Scanner;

public class Controller {
    @FXML
    Button initButton;
    @FXML
    TextField netStructText;
    @FXML
    TextArea trainSetTextArea;
    @FXML
    TextArea predictSetTextArea;
    @FXML
    TextField maxIterText;
    @FXML
    TextField minErrorText;
    @FXML
    TextField speedText;
    @FXML
    TextField momentumText;
    @FXML
    TextArea stateText;
    @FXML
    TabPane tabPane;
    @FXML
    AnchorPane errorLineChartPane;

    double[] X;

    @FXML
    public void initNet(){
        int maxIter=Integer.parseInt(maxIterText.getText());
        double minError=Double.parseDouble(minErrorText.getText());
        String netStructure=netStructText.getText();
        double speed=Double.parseDouble(speedText.getText());
        double momentum=Double.parseDouble(momentumText.getText());
        Main.bpnn=new BPNN(maxIter,minError,netStructure,speed,momentum);

        stateText.appendText("\n成功初始化BP网络\n"+"请导入数据集\n");

    }


    @FXML
    public void chooseTrainSet(){
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("选择训练数据集");
        File file=fileChooser.showOpenDialog(Main.primaryStage);

        DataSet dataSet=new DataSet(Main.bpnn.xnumber,Main.bpnn.ynumber);
        String trainset="";
        try {
            BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            String line=null;
            while((line=reader.readLine())!=null){
                String[] ss=line.split("\t");
                DataGroup dataGroup=new DataGroup(dataSet.xn,dataSet.yn);
                for(int i=0;i<dataSet.xn;i++){
                    dataGroup.inputs[i]=Double.parseDouble(ss[i]);
                }
                for(int i=dataSet.xn;i<dataSet.xn+dataSet.yn;i++){
                    dataGroup.outputs[i-dataSet.xn]=Double.parseDouble(ss[i]);
                }
                dataSet.groups.add(dataGroup);

                trainset+=line;
                trainset+="\n";

                Main.bpnn.dataSet=dataSet;
            }
            trainSetTextArea.appendText(trainset);
            stateText.appendText("数据集加载成功\n"+"请开始训练网络\n");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            stateText.appendText("数据集加载异常："+e.getMessage()+"\n");

        } catch (IOException e) {
            e.printStackTrace();
            stateText.appendText("数据集加载异常："+e.getMessage()+"\n");

        }
    }

    @FXML
    void trainNet(){
        try {
            stateText.appendText("开始训练网络\n");
            Main.bpnn.train(Main.bpnn.dataSet);
            stateText.appendText("训练完成，可以进行预测\n"+"请导入预测数据\n");
        } catch (Exception e) {
            e.printStackTrace();
            stateText.appendText("数据训练异常："+e.getMessage()+"\n");
        }
    }

    @FXML
    void choosePredictSet(){
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("选择训练数据集");
        File file=fileChooser.showOpenDialog(Main.primaryStage);
        tabPane.getSelectionModel().select(1);
        try {
            BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            //-
            String line=null;
            int linenumber=0;
            while((line=reader.readLine())!=null){
                String[] ss=line.split("\t");
                double[] X=new double[Main.bpnn.xnumber];
                for(int i=0;i<Main.bpnn.xnumber;i++){
                    X[i]=Double.parseDouble(ss[i]);
                }
                predictSetTextArea.appendText("X["+linenumber+"]="+line+"\n");

                double[] Y=Main.bpnn.predict(X);
                String so="";
                for(int i=0;i<Y.length;i++){
                    so+=(Y[i]+"\t");
                }
                predictSetTextArea.appendText("Y["+linenumber+"]="+so+"\n");

                linenumber++;
            }
            predictSetTextArea.appendText("最终训练次数："+Main.bpnn.nmaxtrainnumber+"\n"+"最终误差："+Main.bpnn.error+"\n");
            //--

            stateText.appendText("成功预测"+"\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            stateText.appendText("预测异常："+e.getMessage()+"\n");
        } catch (IOException e) {
            e.printStackTrace();
            stateText.appendText("预测异常："+e.getMessage()+"\n");
        } catch (Exception e) {
            e.printStackTrace();
            stateText.appendText("预测异常："+e.getMessage()+"\n");
        }
    }

    @FXML
    void beginDraw(){
        tabPane.getSelectionModel().select(2);

        NumberAxis xAxis=new NumberAxis();
        NumberAxis yAxis=new NumberAxis();
        xAxis.setLabel("迭代次数");
        LineChart<Number,Number>lineChart=new LineChart<Number, Number>(xAxis,yAxis);
        XYChart.Series series=new XYChart.Series();

        for(int i=0;i<Main.bpnn.errorlist.size();i++){
            series.getData().add(new XYChart.Data(i,Main.bpnn.errorlist.get(i)));
        }

        errorLineChartPane.getChildren().add(lineChart);
        lineChart.getData().add(series);
    }

}
