import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // 여기에 코드를 작성해주세요.

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Integer N = Integer.parseInt(br.readLine());

        StringBuilder sb = new StringBuilder();
        
        if(N < 0) sb.append(N).append("\nminus");
        else sb.append(N);

        System.out.println(sb.toString());
    }
}