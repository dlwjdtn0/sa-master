package com.company;

public class Main2 {

    public static void main(String[] args) {

        // 000000
        // 010000

        SimulatedAnnealing2 sa = new SimulatedAnnealing2(1, 0.95,100 );
        sa.solve(new Problem2() {
            @Override
            public double fit(double x) { return 3*x*x -18*x +20;
            }

            @Override
            public boolean isNeighborBetter(double f0, double f1) {
                return f1 > f0;
            }
        }, 0, 31);

        System.out.println(sa.hist);
        // x=19, f(x)=441



    }
}
