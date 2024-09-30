import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int sum = 0;

        while(st.hasMoreTokens()) {
            sum += Integer.parseInt(st.nextToken());
        }

        //  합을 출력
        System.out.println(sum);

        // 평균을 출력
        System.out.println(sum / 3);
    }
}