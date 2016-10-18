package test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by zsh96 on 2016/10/15.
 */
public class JsTest {

    public static void main(String[] args){
        ScriptEngineManager manager=new ScriptEngineManager();
        ScriptEngine engine=manager.getEngineByName("nashorn");
        engine.put("a",4);
        engine.put("b",2);
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
            double result=(double)engine.eval("gaussian(10,3)");
            System.out.println(result);
            String[] s="\t  32  \t".split("\t");
            System.out.println(s);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
