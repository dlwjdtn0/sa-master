package com.company;

import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing2 {
    private double t;   // 초기온도
    private double a;   // 냉각비율
    private int niter;  // 종료조건
    public ArrayList<Double> hist;

    public SimulatedAnnealing2(double t, double a, int niter) {
        this.t = t;
        this.a = a;
        this.niter = niter;
        hist = new ArrayList<>();
    }

    public double solve(Problem2 p, double lower, double upper) {
        Random r = new Random();
        double x0 = r.nextDouble() * (upper - lower) + lower;    // 초기후보해
        double f0 = p.fit(x0);                                   // 초기후보해의 적합도
        hist.add(f0);

        for(int i=0; i<niter; i++) {    // REPEAT
            int kt = (int) Math.round(t * 20);
            for(int j=0; j<kt; j++) {
                double x1 = r.nextDouble() * (upper - lower) + lower;    // 이웃해
                double f1 = p.fit(x1);
                if(p.isNeighborBetter(f0, f1)) {    // 이웃해가 더 나음
                    x0 = x1;
                    f0 = f1;
                    hist.add(f0);
                } else {    // 기존해가 더 나음
                    double d = f1 - f0;
                    double p0 = Math.exp(-d/t);
                    if(r.nextDouble() < p0) {
                        x0 = x1;
                        f0 = f1;
                        hist.add(f0);
                    }
                }
            }
            t *= a;
        }
        return x0;
    }
}
