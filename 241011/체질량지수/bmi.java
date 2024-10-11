import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = new StringTokenizer(br.readLine());

        int h = Integer.parseInt(st.nextToken());
        int w = Integer.parseInt(st.nextToken());

        int b = (int) Math.floor(10_000 * w / (double)(h * h));

        StringBuilder sb = new StringBuilder();

        if(b >= 25) sb.append(b).append("\nObesity\n");
        else sb.append(b).append("\n");

        bw.write(sb.toString());
        bw.flush();

    }
}