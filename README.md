# 모의 담금질을 이용한 parameter estimation

## 201902949 이정수

-----------------------------------------------------

# Simulated Annealing(모의 담금질)

 Simulated Annealing(모의 담금질)은 가열된 금속(온도가 높은 액체)을 서서히 냉각 시키고(액체는 결정체가 됨) 이를 통해 금속을 더 강하게 하는 방식인 annealing을 모방한해 탐색 알고리즘이다.
 
 온도가 높은 액체는 분자가 자유롭고 온도가 낮아지면서 분자의 움직임이 줄어들고 결정체가 된다. 용융 상태에 분자가 자유로운 것처럼 처음 해를 탐색하는 과정에는 무작위로 특정한 패턴이 없지만 온도가 낮아지면서 분자 움직임이 줄어들고 결정체가 되는 것처럼 해 탐색 과정도 규칙적으로 이루어지게 된다.
 
 이런 방식의 해 탐색 알고리즘은 후보해에 이웃하는 이웃해를 정의해야 한다. 
 
 모의 담금질 기법으로 최솟값을 구할 때는 확률을 이용하기 때문에 온도(T)가 높은 초기에는 이웃해 중에서 현재보다 값이 더 큰 이웃해(위 방향)로 이동할 수 있다. 하지만 T가 낮아지면서 값이 더 작은 이웃해(아래 방향)으로 이동한다.
 
 지역 최적해(local optimum)로 이동이 됬을 때 이웃해들보다 값이 더 작지만 확률을 이용해서 이동하기 때문에 운이 좋게 값이 더 큰 이웃해(위 방향)로 이동하다가 전역최적해(global optimum)을 찾는다. 하지만 항상 전역 최적해를 찾는다는 보장은 없다.
 
 ![1](https://user-images.githubusercontent.com/81748368/121143018-8ac8a200-c877-11eb-8661-5e6c901f68ba.PNG)

------------------------------------------------------

# 알고리즘

 ## 모의 담금질 기법의 기본 알고리즘

1.     임의의 후보해 s를 선택  // 임의의 후보해 s 선택하여 탐색 시작
2.      초기 T를 정한다.       // T는 충분히 높은 값으로 실험을 통해 정해짐
3.     repeat                // repeat 루프는 종료 조건이 만족될때까지 수행
4.      for i = 1 to kT {  // kT는 T에서의 for-루프 반복 횟수. T가 작아질수록 kT 커짐
5.         s의 이웃해 중에서 랜덤하게 하나의 해 s'를 선택한다.   // s'를 랜덤으로 선택
6.         d = (s'의 값) - (s의 값)   // d는 s와 s'값의 차이
7.         if (d < 0)       // s보다 s'가 더 우수한 경우
8.              s ← s'    // s'가 s가 됨
9.         else              // s'가 s보다 우수하지 않은 경우
10.           q ← (0,1) 사이에서 랜덤하게 선택한 수  // 0부터 1사이에서 무작위로 선택한 q
11.           if ( q < p ) s ← s'    // p는 자유롭게 탐색할 확률. q가 p보다 작으면 s'가 s가 될 수도 있고 안 될수도 있음
         }
12.      T ←  αT    // α는 냉각율(cooling ratio)로 T에 곱해서 천천히 감소되도록 조절함. 0.8 ≤ α ≤ 0.99 

13.     until (종료 조건이 만족될 때까지)    // 종료조건: 더 이상 우수한 해를 찾지 못함, 미리 정해놓은 repeat 루프의 최대 반복 횟수의 초과 여부
14.     return s    // repeat 루프가 끝나면 현재 해인 s를 리턴


 ## 코드 구현
(코드는 수업시간에 구현한 코드입니다.)


public class Main {

    public static void main(String[] args) {

        // 000000
        // 010000

	    SimulatedAnnealing sa = new SimulatedAnnealing(1, 0.95, 100);
	    sa.solve(new Problem() {
            @Override
            public double fit(double x) {
                return 0.16*x*x*x*x -x*x + 0.37*x + 5;
            }

            @Override
            public boolean isNeighborBetter(double f0, double f1) {
                return f1 > f0;
            }
        }, 0, 31);

        System.out.println(sa.hist);
        //double x0=19; double f0=441;
    }
}



public class SimulatedAnnealing 
{
    private double t;   // 초기온도
    private double a;   // 냉각비율
    private int niter;  // 종료조건
    public ArrayList<Double> hist;

    public SimulatedAnnealing(double t, double a, int niter) 
{
        this.t = t;
        this.a = a;
        this.niter = niter;
        hist = new ArrayList<>();
    }

    public double solve(Problem p, double lower, double upper) 
{
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

					   
					 
					   
					   
