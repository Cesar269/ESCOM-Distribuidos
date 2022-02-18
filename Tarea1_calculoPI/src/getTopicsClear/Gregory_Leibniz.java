package getTopicsClear;

public class Gregory_Leibniz {

    /* Si "nodo" es impar: Σ(4.0/(8∗i+2∗(nodo−2)+3))
        Si "nodo" es par : −Σ(4.0/(8∗i+2∗(nodo−2)+3)) */

    public static float aproxPI(long limit){
        float PI=1;
        for (int i = 0; i < limit; i++) {
            float denominator = i*2 + 3;
            PI = (i%2==0)?(PI -= 1/denominator):(PI += 1/denominator);
        }
        return 4*PI;
    }

    public static void main(String[] args) {
        System.out.println(aproxPI(10000));
    }
}
