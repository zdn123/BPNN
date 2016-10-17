package sample;

import bpnn.BPNN;
import bpnn.DataSet.DataGroup;
import bpnn.DataSet.DataSet;
import bpnn.function.ActivateFunction;
import bpnn.function.Sigmord;
import bpnn.function.Tanh;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;

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
    TabPane tabPane;
    @FXML
    AnchorPane errorLineChartPane;
    @FXML
    ComboBox activateChoose;
    @FXML
    TableView<TestData> compareTable;
    @FXML
    ComboBox testSelect;

    LineChart<Number,Number>lineChart;

    double[] X;

    @FXML
    public void initNet(){
        int maxIter=Integer.parseInt(maxIterText.getText());
        double minError=Double.parseDouble(minErrorText.getText());
        String netStructure=netStructText.getText();
        double speed=Double.parseDouble(speedText.getText());
        double momentum=Double.parseDouble(momentumText.getText());

        int funcN=activateChoose.getSelectionModel().getSelectedIndex();

        ActivateFunction activateFunction=null;
        switch (funcN){
            case 0:
                activateFunction=new Sigmord();
                break;
            case 1:
                activateFunction=new Tanh();
                break;
            default:
                activateFunction=new Tanh();
        }

        trainSetTextArea.setText("");
        predictSetTextArea.setText("");
        lineChart.getData().clear();

        Main.bpnn=new BPNN(maxIter,minError,netStructure,speed,momentum,activateFunction);

        compareTable.getColumns().clear();
        for(int i=0;i<Main.bpnn.xnumber;i++){
            TableColumn tc=new TableColumn("x"+i);
            int finalI = i;
            tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TestData,Double>, ObservableValue<Number>>() {
                @Override
                public ObservableValue<Number> call(TableColumn.CellDataFeatures<TestData,Double> param) {
                    return param.getValue().doubleProperties[finalI];
                }
            });
            compareTable.getColumns().add(tc);

        }
        for(int i=0;i<Main.bpnn.ynumber;i++){
            TableColumn tc=new TableColumn("dy"+i);
            int finalI = i;
            tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TestData,Double>, ObservableValue<Number>>() {
                @Override
                public ObservableValue<Number> call(TableColumn.CellDataFeatures<TestData,Double> param) {
                    return param.getValue().doubleProperties[2*finalI+Main.bpnn.xnumber];
                }
            });
            compareTable.getColumns().add(tc);
            TableColumn tc1=new TableColumn("dy"+i);
            int finalI1 = i;
            tc1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TestData,Double>, ObservableValue<Number>>() {
                @Override
                public ObservableValue<Number> call(TableColumn.CellDataFeatures<TestData,Double> param) {
                    return param.getValue().doubleProperties[2*finalI1+1+Main.bpnn.ynumber];
                }
            });
            compareTable.getColumns().add(tc1);
        }
    }


    @FXML
    public void chooseTrainSet(){
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("选择训练数据集");
        File file=fileChooser.showOpenDialog(Main.primaryStage);
        train(file);
    }
    @FXML
    public void chooseDefaultSet(){
        File file=new File("res/train.txt");
        //File file=new File("train.txt");//for artifact
        train(file);
        File file1=new File("res/predict.txt");
        //File file1=new File("predict.txt");//for artifact
        predict(file1);
    }
    void train(File file){
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
                dataSet.trainGroups.add(dataGroup);

                trainset+=line;
                trainset+="\n";
            }
            Main.bpnn.dataSet=dataSet;

            if(Main.bpnn.testrate>=dataSet.trainGroups.size()){
                Main.bpnn.testrate=0;
            }
            dataSet.sortTrainGroup();
            dataSet.generateTestGroups(Main.bpnn.testrate);
            dataSet.generateOne();//归一化

            trainSetTextArea.appendText(trainset);
            Main.bpnn.train(Main.bpnn.dataSet);

            if(Main.bpnn.testrate!=0){
                Main.bpnn.getpredictTest();
                DataSet ds=Main.bpnn.dataSet;
                compareTable.getItems().clear();
                for(int i=0;i<ds.testGroups.size();i++){
                    TestData td=new TestData(dataSet,i);
                    compareTable.getItems().add(td);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void choosePredictSet(){
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("选择训练数据集");
        //File file=fileChooser.showOpenDialog(Main.primaryStage);
        File file=new File("res/predict.txt");
        //File file=new File("predict.txt");//for artifact
        predict(file);
    }

    void predict(File file){
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
            predictSetTextArea.appendText("最终训练次数："+Main.bpnn.nmaxtrainnumber+"\n"+"最终误差："+Main.bpnn.trainError +"\n");
            //--

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void beginDraw(){
        tabPane.getSelectionModel().select(2);
        XYChart.Series series=new XYChart.Series();

        //series.getChart().set


//        //
//        Thread thread=new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for(int i=0;i<Main.bpnn.trainErrorlist.size();i++){
//                    series.getData().add(new XYChart.Data(i,Main.bpnn.trainErrorlist.get(i)));
//                    try {
//                        Thread.sleep(300);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        thread.start();
//        //
        for(int i = 0; i<Main.bpnn.trainErrorlist.size(); i++){
            series.getData().add(new XYChart.Data(i,Main.bpnn.trainErrorlist.get(i)));
        }
        lineChart.getData().add(series);

        if(Main.bpnn.testrate!=0){
            XYChart.Series testSeries=new XYChart.Series();
            for(int i=0;i<Main.bpnn.testErrorlist.size();i++){
                testSeries.getData().add(new XYChart.Data(i,Main.bpnn.testErrorlist.get(i)));
            }
            lineChart.getData().add(testSeries);
        }
    }

    @FXML
    void initialize(){
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "sigmord",
                        "tanh"
                );
        activateChoose.setItems(options);

        NumberAxis xAxis=new NumberAxis();
        NumberAxis yAxis=new NumberAxis();
        xAxis.setLabel("迭代次数");
        lineChart=new LineChart<Number, Number>(xAxis,yAxis);
        lineChart.setCreateSymbols(false);
        errorLineChartPane.getChildren().add(lineChart);

        ObservableList<String> options1 =
                FXCollections.observableArrayList(
                        "1/20",
                        "1/10",
                        "1/7",
                        "1/5",
                        "1/3"
                );
        testSelect.setItems(options1);
        testSelect.setOnAction((Event ev)->{
            int n=testSelect.getSelectionModel().getSelectedIndex();
            switch (n){
                case 0:
                    Main.bpnn.testrate=20;
                    break;
                case 1:
                    Main.bpnn.testrate=10;
                    break;
                case 2:
                    Main.bpnn.testrate=7;
                    break;
                case 3:
                    Main.bpnn.testrate=5;
                    break;
                case 4:
                    Main.bpnn.testrate=3;
                    break;
                default:
                    Main.bpnn.testrate=7;
            }
        });
        manager=new ScriptEngineManager();
        engine=manager.getEngineByName("nashorn");
        expressionText.setText("x0=uniform(1,10);\n" +
                "x1=uniform(2,8);\n" +
                "y0=x0+x1;\n" +
                "y1=x0-x1;");
    }
    class TestData{
        public SimpleDoubleProperty[] doubleProperties;

        public TestData(DataSet dataSet,int n) {
            this.doubleProperties= new SimpleDoubleProperty[dataSet.xn + 2 * dataSet.yn];
            for(int i=0;i<dataSet.xn;i++){
                doubleProperties[i]=new SimpleDoubleProperty();
                doubleProperties[i].set(dataSet.oldtestGroups.get(n).inputs[i]);
            }
            for(int i=0;i<dataSet.yn;i++){
                doubleProperties[dataSet.xn+2*i]=new SimpleDoubleProperty();
                doubleProperties[dataSet.xn+2*i].set(dataSet.oldtestGroups.get(n).outputs[i]);
            }
            for(int i=0;i<dataSet.yn;i++){
                doubleProperties[dataSet.xn+2*i+1]=new SimpleDoubleProperty();
                doubleProperties[dataSet.xn+2*i+1].set(dataSet.testPredicts.get(n)[i]);
            }
        }
    }

    @FXML
    TextField dataCount;
    @FXML
    TextField inputCount;
    @FXML
    TextField outputCount;
    @FXML
    TextField noise;
    @FXML
    TextArea expressionText;

    ScriptEngineManager manager;
    ScriptEngine engine;

    @FXML
    void generateDataSet(){
        int datacount= Integer.parseInt(dataCount.getText());
        int inputcount=Integer.parseInt(inputCount.getText());
        int outputcount=Integer.parseInt(outputCount.getText());
        double dnoise=Double.parseDouble(noise.getText());

        try {
            engine.eval("function uniform(min,max)\n" +
                    "{\n" +
                    "\treturn min+Math.random()*(max-min);\n" +
                    "}\n" +
                    "function randGaussian(m)\n" +
                    "{\n" +
                    "\tsum=0;\n" +
                    "\tfor(var i=0;i<m;i++){\n" +
                    "\t\tsum+=(Math.random()-1/2);\n" +
                    "\t}\n" +
                    "\treturn Math.sqrt(12/m)*sum;\n" +
                    "}\n" +
                    "function gaussian(m,a)\n" +
                    "{\n" +
                    "\treturn randGaussian(10000)*a+m;\n" +
                    "}");
//            double result=(double)engine.eval("gaussian(10,3)");
            DataSet dataSet=new DataSet(inputcount,outputcount);
            for(int i=0;i<datacount;i++){
                engine.eval(expressionText.getText());

                double[] inputs=new double[inputcount];
                for(int j=0;j<inputcount;j++){
                    double x= (double) engine.get("x"+j);
                    inputs[j]=x;
                }
                double[] outputs=new double[outputcount];
                for(int j=0;j<outputcount;j++){
                    double y=(double)engine.get("y"+j);
                    outputs[j]=y+Main.bpnn.random.nextDouble()*dnoise;
                }
                DataGroup dg=new DataGroup(inputcount,outputcount);
                dg.inputs=inputs;
                dg.outputs=outputs;
                dataSet.trainGroups.add(dg);
            }

            for(int i=0;i<dataSet.trainGroups.size();i++){
                System.out.print("X= ");
                for(int j=0;j<dataSet.trainGroups.get(i).inputs.length;j++){
                    System.out.print(dataSet.trainGroups.get(i).inputs[j]+"\t");
                }
                System.out.print("      Y= ")
                ;for(int j=0;j<dataSet.trainGroups.get(i).outputs.length;j++){
                    System.out.print(dataSet.trainGroups.get(i).outputs[j]+"\t");
                }
                System.out.println();
            }
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
