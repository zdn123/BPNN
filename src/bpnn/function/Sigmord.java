package bpnn.function;

/**
 * Created by zsh_o on 2016/10/1.
 */
public class Sigmord implements ActivateFunction {
    @Override
    public double calculate(double x) {
        return 1/(1+Math.exp(-1.0*x));
    }

    @Override
    public double derivativeCalculate(double x) {
        return calculate(x)*(1-calculate(x));
    }
}
