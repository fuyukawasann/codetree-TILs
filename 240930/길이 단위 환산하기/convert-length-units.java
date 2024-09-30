import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        double n = Double.parseDouble(br.readLine());

        n *= (double)30.48;

        System.out.printf("%.1f\n", n);
    }
}