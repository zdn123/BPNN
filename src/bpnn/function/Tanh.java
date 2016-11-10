package bpnn.function;

/**
 * Created by zsh_o on 2016/10/1.
 */
public  class Tanh implements ActivateFunction {

    @Override
    public double calculate(double x) {
        return (Math.exp(x)-Math.exp(-1.0*x))/(Math.exp(x)+Math.exp(-1.0*x));
    }

    @Override
    public double derivativeCalculate(double x) {
        return 1-Math.pow(calculate(x),2.0);
    }
}
