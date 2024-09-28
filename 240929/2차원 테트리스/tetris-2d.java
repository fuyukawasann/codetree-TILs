// package S2020.상반기.오전.P001;

import java.io.*;
import java.util.*;

public class Main {
	
	static boolean[][] map;

	public static void main(String[] args) throws Exception {
		// 파일 읽기
		// System.setIn(new FileInputStream("src/S2020/상반기/오전/P001/input.txt"));
		
		// 라이브러리
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		StringTokenizer st;
		
		// k를 받는다.
		int K = Integer.parseInt(br.readLine());
		
		// 테트리스 생성
		map = new boolean[10][10];
		
		
		// 쿼리를 실행한다.
		int result = 0;
		
		for(int k = 0; k < K; k++) {
			// 쿼리를 받는다.
			st = new StringTokenizer(br.readLine());
			int T = Integer.parseInt(st.nextToken());
			int X = Integer.parseInt(st.nextToken());
			int Y = Integer.parseInt(st.nextToken());
			
			// 노란색을 먼저 처리한다.
			// 1. 타일을 배치한다. - X를 4부터 9로 확인하고 최초로 true가 나오는 시점에서 이전 시점의 좌표를 true로 만들고 break한다.
			// 1-1. Type 1일 때
			if(T == 1) {
				boolean isFilled = false;
				for(int i = 5; i <= 9; i++) {
					if(map[i][Y]) {
						map[i - 1][Y] = true;
						isFilled = true;
						break;
					}
				}
				// 만약 다 돌았는데도 채워지지 않았다면 9번 row에 채워 넣음
				if(!isFilled) map[9][Y] = true;
			}
			// 1-2. Type 2일 때
			else if(T == 2) {
				boolean isFilled = false;
				for(int i = 5; i <= 9; i++) {
					if(map[i][Y] || map[i][Y + 1]) {
						map[i - 1][Y] = true;
						map[i - 1][Y + 1] = true;
						isFilled = true;
						break;
					}
				}
				// 만약 다 돌았는데도 채워지지 않았다면 9번 row에 채워 넣음
				if(!isFilled) {
					map[9][Y] = true;
					map[9][Y + 1] = true;
				}
			}
			// 1-3. Type 3일 때
			else {
				boolean isFilled = false;
				for(int i = 5; i <= 9; i++) {
					if(map[i][Y]) {
						map[i - 2][Y] = true;
						map[i - 1][Y] = true;
						isFilled = true;
						break;
					}
				}
				// 만약 다 돌았는데도 채워지지 않았다면 9번, 8번 row에 채워 넣음
				if(!isFilled) {
					map[9][Y] = true;
					map[8][Y] = true;
				}
			}
			// 2. 노란 부분들을 확인하며 꽉찬 행이 있다면 result를 증가하고 윗줄을 아랫줄로 내린다.
			for(int x = 9; x >= 4; x--) {
				// 2-1. 꽉 찼으면 result를 증가시킨다.
				while(map[x][0] && map[x][1] && map[x][2] && map[x][3]) {
					result++;
					// 2-1-1. map을 아래로 당긴다.
					for(int i = x - 1; i >= 3; i--) {
						for(int j = 0; j < 4; j++) {
							map[i + 1][j] = map[i][j];
						}
					}
				}
			}
			// 3. 연한 부분에 타일이 있다면 타일 없어질 때까지 미룸
			while(map[5][0] || map[5][1] || map[5][2] || map[5][3]) {
				for(int i = 8; i >= 3; i--) {
					for(int j = 0; j < 4; j++) {
						map[i + 1][j] = map[i][j];
					}
				}
			}
			
			// 빨간색을 처리한다.
			// 1. 타일을 배치한다.
			// 1-1. Type 1일 때
			if(T == 1) {
				boolean isFilled = false;
				for(int i = 5; i <= 9; i++) {
					if(map[X][i]) {
						map[X][i - 1] = true;
						isFilled = true;
						break;
					}
				}
				// 만약 다 돌았는데도 채워지지 않았다면 9번 row에 채워 넣음
				if(!isFilled) map[X][9] = true;
			}
			// 1-2. Type 2일 때
			else if(T == 2) {
				boolean isFilled = false;
				for(int i = 5; i <= 9; i++) {
					if(map[X][i]) {
						map[X][i - 2] = true;
						map[X][i - 1] = true;
						isFilled = true;
						break;
					}
				}
				// 만약 다 돌았는데도 채워지지 않았다면 9번, 8번 column에 채워 넣음
				if(!isFilled) {
					map[X][9] = true;
					map[X][8] = true;
				}
			}
			// 1-3. Type 3일 때
			else {
				boolean isFilled = false;
				for(int i = 5; i <= 9; i++) {
					if(map[X][i] || map[X + 1][i]) {
						map[X][i - 1] = true;
						map[X + 1][i - 1] = true;
						isFilled = true;
						break;
					}
				}
				// 만약 다 돌았는데도 채워지지 않았다면 9번 column에 채워 넣음
				if(!isFilled) {
					map[X][9] = true;
					map[X + 1][9] = true;
				}
			}
			// 2. 빨간 부분들을 확인하며 꽉찬 column이 있다면 result를 증가하고 왼쪽줄을 오른쪽줄로 옮긴다.
			for(int y = 9; y >= 4; y--) {
				// 2-1. 꽉 찼으면 result를 증가시킨다.
				while(map[0][y] && map[1][y] && map[2][y] && map[3][y]) {
					result++;
					// 2-1-1. map을 아래로 당긴다.
					for(int i = y - 1; i >= 3; i--) {
						for(int j = 0; j < 4; j++) {
							map[j][i + 1] = map[j][i];
						}
					}
				}
			}
			// 3. 연한 부분에 타일이 있다면 타일 없어질 때까지 미룸
			while(map[0][5] || map[1][5] || map[2][5] || map[3][5]) {
				for(int i = 8; i >= 3; i--) {
					for(int j = 0; j < 4; j++) {
						map[j][i + 1] = map[j][i];
					}
				}
			}
			
			// map 출력
//			System.out.println("Query #" + k);
//			System.out.println("result: " + result);
//			printMap();
		}
		
		// 결과를 입력한다.
		StringBuilder sb = new StringBuilder();
		// 점수 입력
		sb.append(result).append("\n");
		// 타일 수를 입력
		// 노란 쪽 + 빨간쪽 타일 수 입력
		int tile = 0;
		for(int i = 6; i < 10; i++) {
			for(int j = 0; j < 4; j++) {
				if(map[i][j]) tile++;
				if(map[j][i]) tile++;
			}
		}
		sb.append(tile).append("\n");
		
		// 결과 출력
		bw.write(sb.toString());
		bw.flush();
		
		
		// 자원 반환
		bw.close();
		br.close();
	}
	
	static void printMap() {
		// x 0 ~ 3
		
		StringBuilder printout = new StringBuilder();
		
		// x: 0 ~ 3
		for(int i = 0; i <= 3; i++) {
			for(int j = 0; j <= 9; j++) {
				if(map[i][j]) printout.append(1).append(" ");
				else printout.append(0).append(" ");
			}
			printout.append("\n");
		}
		
		// x: 4 ~ 9
		for(int i = 4; i <= 9; i++) {
			for(int j = 0; j <= 3; j++) {
				if(map[i][j]) printout.append(1).append(" ");
				else printout.append(0).append(" ");
			}
			printout.append("\n");
		}
		
		System.out.println(printout.toString());
	}

}