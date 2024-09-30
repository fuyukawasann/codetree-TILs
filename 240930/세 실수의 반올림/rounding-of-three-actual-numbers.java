import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


        for(int i = 0; i < 3; i++) {
            System.out.printf("%.3f\n", Double.parseDouble(br.readLine()));
        }
    }
}