// package S2015.하반기.오전.P001;

import java.io.*;
import java.util.*;

public class Main {

	public static void main(String[] args) throws Exception {
		// 파일 입출력
		// System.setIn(new FileInputStream("src/S2015/하반기/오전/P001/input.txt"));
		
		// 라이브러리
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		// 식당 수
		int N = Integer.parseInt(br.readLine());
		
		// 정답 저장을 위한 변수 선언
		long result = 0;
		
		// 식당별 고객수
		StringTokenizer customer = new StringTokenizer(br.readLine());
		
		// 검사팀장과 팀원이 검사 가능한 최대 고객 수
		StringTokenizer check = new StringTokenizer(br.readLine());
		int[] checkAvailable = new int[2];
		checkAvailable[0] = Integer.parseInt(check.nextToken());
		checkAvailable[1] = Integer.parseInt(check.nextToken());
		
		// 식당 별 고객수를 돌며 몇 명이 필요한지 계산
		while(customer.hasMoreTokens()) {
			// 현재 고객 수를 받는다.
			int no_customer = Integer.parseInt(customer.nextToken());
			
			// 각 식당 별로 팀장은 1명 필요하므로 result를 1 올린다.
			result++;
			// 만약 팀장이 검사 가능한 max보다 남은 고객 수가 크다면 -> no_customer를 max만큼 뺀다.
			if(no_customer > checkAvailable[0]) {
				no_customer -= checkAvailable[0];
			}
			// 그렇지 않다면 no_customer는 0이 된다.
			else no_customer -= no_customer;
			
			// 만약 no_customer가 0이 아니라면 -> 팀원들이 검사해야 한다.
			if(no_customer != 0) {
				// 팀원은 no_customer를 팀원 max로 나누되 소수점을 살린뒤 올림을 한다.
				result += Math.ceil((double)no_customer / checkAvailable[1]);
			}
		}
		
		// 결과를 입력한다.
		StringBuilder sb = new StringBuilder();
		sb.append(result).append("\n");
		
		// 결과를 출력한다.
		bw.write(sb.toString());
		bw.flush();
		bw.close();
		br.close();

	}

}