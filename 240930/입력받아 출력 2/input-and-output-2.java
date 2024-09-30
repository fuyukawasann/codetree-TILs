import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), "-");

        StringBuilder sb = new StringBuilder();

        sb.append(st.nextToken()).append(st.nextToken());

        System.out.println(sb.toString());
    }
}