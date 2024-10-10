// package S2024.상반기.오전.P001;

import java.io.*;
import java.util.*;

public class Main {
	
	static int K, M, idx;
	static int[][] map;
	static int[] wall, result;
	
	// 조이콘 - 상, 하, 좌, 우
	static int[] dr = {-1, 1, 0, 0}, dc = {0, 0, -1, 1};

	public static void main(String[] args) throws Exception {
		// 파일 입력
		// System.setIn(new FileInputStream("src/S2024/상반기/오전/P001/input.txt"));
		
		// 라이브러리
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		// 먼저 탐사 반복 횟수 K와 벽면에 적힌 유물 조각 개수 M을 받는다.
		StringTokenizer st = new StringTokenizer(br.readLine());
		K = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		// 유물 지도를 받는다.
		map = new int[5][5];
		for(int i = 0; i < 5; i++)
		{
			st = new StringTokenizer(br.readLine());
			for(int j = 0; j < 5; j++)
			{
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		// 벽면 유물을 받는다.
		wall = new int[M];
		st = new StringTokenizer(br.readLine());
		for(int i = 0; i < M; i++)
		{
			wall[i] = Integer.parseInt(st.nextToken());
		}
		idx = 0;
		
		// 턴을 반복한다.
		int turn = 0;
		result = new int[K];
		for(turn = 0; turn < K; turn++)
		{
			// 1. 탐사
			int max = Integer.MIN_VALUE;
			PriorityQueue<Yeumul> pq = new PriorityQueue<>();
			// 시계 방향 90 180 270 -> 360도면 원래 자리
			// 먼저 가능한 중심들에서 90 180 270도를 돌리면서 가능한 유물가치를 구한다.
			for(int i = 1; i <= 3; i++)
			{
				for(int j = 1; j <= 3; j++)
				{
					fYAnswer temp = findYeumul(i, j); // 0: value, 1: angle
					// max를 업데이트하되 만약 max와 같다면 현재 중심 좌표와 각도를 리스트에 넣는다.
					if(temp.answer[0] == max) {
						pq.offer(new Yeumul(i, j, temp.answer[1], temp.list));
					}
					// max보다 더 큰 max가 나온다면 리스트를 비우고, max를 업데이트 한 후 현재 중심 좌표와 각도를
					// 리스트에 넣는다.
					else if(temp.answer[0] > max)
					{
						pq.clear();
						max = temp.answer[0];
						pq.offer(new Yeumul(i, j, temp.answer[1], temp.list));
					}
				}
			}
			
			// pq에서 꺼낸 것이 최대 가치와 그 중심의 좌표, angle임
			// 만약 최대값이 0이라면 -> 챙길 수 있는 유물이 없음
			// break -> 탐색 종료임
			if(max == 0) break;
			
			// 그게 아니라면 진짜로 지도를 angle 만큼 회전 시키고 bfs로 유물 없애야 할 곳을 0으로 초기화 함
			Yeumul getY = pq.poll();
			cycle(getY.angle, getY.r, getY.c);
			// 결과를 반영함
			result[turn] += max;
			
			// 조각들을 주어진 조건에 따라 채워넣음
			// list를 정렬
			Collections.sort(getY.list);
			// 조각을 채워 넣음
			for(int i = 0; i < getY.list.size(); i++)
			{
				// 지금 좌표에 유물을 채워넣음
				map[getY.list.get(i).r][getY.list.get(i).c] = wall[idx++];
			}
			
			
			// 인접한 조각이 3개 있는(유물 생성 조건)지 확인하고 없을 때까지 반복
			while(true)
			{
				// bfs로 계산함
				bfsAnswer cur = bfs();
				// cur이 0이라면 더 이상 유물이 생성되지 않은 것이므로 탈출
				if(cur.piece == 0) break;
				
				// 그게 아니라면 결과를 일단 반영
				result[turn] += cur.piece;
				
				// 채워 넣기 전에 정렬
				Collections.sort(cur.list);
				
				// 빈 곳을 채워넣음
				for(int i = 0; i < cur.list.size(); i++)
				{
					map[cur.list.get(i).r][cur.list.get(i).c] = wall[idx++];
				}
			}
			
		}
		
		
		
		// 정답 출력
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < turn; i++)
		{
			sb.append(result[i]).append(" ");
		}
		sb.append("\n");
		
		bw.write(sb.toString());
		bw.flush();
		
		
		
		// 자원 반환
		br.close();
		bw.close();
		

	}
	
	static fYAnswer findYeumul(int r, int c)
	{
		int yeumulPiece = 0;
		int angle = 0;
		ArrayList<Coordinate> ans = new ArrayList<>();
		
		for(int i = 90; i <= 270; i += 90)
		{
			cycle(90, r, c);
			// bfs로 확인
			bfsAnswer temp = bfs();
			// temp가 yeumulPiece보다 크면 각도도 변경하고 값도 바꿈
			if(temp.piece > yeumulPiece)
			{
				angle = i;
				yeumulPiece = temp.piece;
				ans = temp.list;
			}
			// 만약 temp가 같으면 반영 안함 (선행된 각이 더 작고 이는 회전에서의 우선순위 반영)
		}
		// 원래 지도로 바꾸기 위해 한 번 더 회전
		cycle(90, r, c);
		
		return new fYAnswer(new int[] {yeumulPiece, angle}, ans);
		
	}
	
	static bfsAnswer bfs()
	{
		// 방문 배열
		boolean[][] visit = new boolean[5][5];
		int target = 0;
		int piece = 0;
		ArrayList<Coordinate> ans = new ArrayList<>();
		
		for(int i = 0; i < 5; i++)
		{
			for(int j = 0; j < 5; j++)
			{
				// 방문 안 한 곳이면 실행
				if(visit[i][j]) continue;
				
				// 지금 값을 타깃으로 함
				target = map[i][j];
				
				// 큐 선언
				ArrayDeque<Coordinate> q = new ArrayDeque<>();
				ArrayList<Coordinate> axis = new ArrayList<>();
				
				// 큐에 현재 값을 집어 넣음
				q.offer(new Coordinate(i, j));
				axis.add(new Coordinate(i, j));
				
				// 방문 체크
				visit[i][j] = true;
				
				while(!q.isEmpty())
				{
					// 1. 큐에서 꺼낸다
					Coordinate now = q.poll();
					// 2. 목적지 인가? - 확인 안 함
					// 3. 순회한다.
					for(int iter = 0; iter < 4; iter++)
					{
						int nr = now.r + dr[iter];
						int nc = now.c + dc[iter];
						// 4. 갈 수 있는가? - 지도 안에 있는가
						if(0 <= nr && nr < 5 && 0 <= nc && nc < 5)
						{
							// 지도의 값이 현재의 값과 일치하는 가 그리고 방문 안한 곳인가
							if(map[nr][nc] == target && !visit[nr][nc])
							{
								// 5. 체크인
								visit[nr][nc] = true;
								// 6. 큐에 넣는다.
								Coordinate next = new Coordinate(nr, nc);
								q.offer(next);
								axis.add(next);
							}
						}	
					}
				}
				
				// 지금 유물 조각이 3이상일 때만 piece에 반영하고 좌표도 옮김
				if(axis.size() >= 3)
				{
					piece += axis.size();
					
					for(int k = 0; k < axis.size(); k++)
					{
						ans.add(axis.get(k));
					}
				}
			}
		}
		
		// piece와 arraylist를 반환
		return new bfsAnswer(piece, ans);
	}
	
	static void cycle(int angle, int r, int c)
	{
		// 90도 회전일 때
		if(angle == 90)
		{
			// 가운데 X
			int temp = map[r-1][c+1];
			map[r-1][c+1] = map[r-1][c-1];
			int temp2 = map[r+1][c+1];
			map[r+1][c+1] = temp;
			temp = map[r+1][c-1];
			map[r+1][c-1] = temp2;
			map[r-1][c-1] = temp;
			
			// 가운데
			temp = map[r][c+1];
			map[r][c+1] = map[r-1][c];
			temp2 = map[r+1][c];
			map[r+1][c] = temp;
			temp = map[r][c-1];
			map[r][c-1] = temp2;
			map[r-1][c] = temp;
		}
		// 180도 회전일 때
		else if(angle == 180)
		{
			int temp = map[r+1][c+1];
			map[r+1][c+1] = map[r-1][c-1];
			map[r-1][c-1] = temp;
			
			temp = map[r+1][c];
			map[r+1][c] = map[r-1][c];
			map[r-1][c] = temp;
			
			temp = map[r+1][c-1];
			map[r+1][c-1] = map[r-1][c+1];
			map[r-1][c+1] = temp;
			
			temp = map[r][c - 1];
			map[r][c - 1] = map[r][c + 1];
			map[r][c + 1] = temp;
		}
		// 270도 회전일 때
		else if(angle == 270)
		{
			int temp = map[r+1][c-1];
			map[r+1][c-1] = map[r-1][c-1];
			int temp2 = map[r+1][c+1];
			map[r+1][c+1] = temp;
			temp = map[r-1][c+1];
			map[r-1][c+1] = temp2;
			map[r-1][c-1] = temp;
			
			temp = map[r][c-1];
			map[r][c-1] = map[r-1][c];
			temp2 = map[r+1][c];
			map[r+1][c] = temp;
			temp = map[r][c+1];
			map[r][c+1] = temp2;
			map[r-1][c] = temp;
		}
	}

}

class fYAnswer
{
	int[] answer;
	ArrayList<Coordinate> list;
	
	public fYAnswer(int[] answer, ArrayList<Coordinate> list)
	{
		this.answer = answer;
		this.list = list;
	}
}

class bfsAnswer
{
	int piece;
	ArrayList<Coordinate> list;
	
	public bfsAnswer(int piece, ArrayList<Coordinate> list)
	{
		this.piece = piece;
		this.list = list;
	}
}

class Coordinate implements Comparable<Coordinate>
{
	int r, c;
	
	public Coordinate(int r, int c)
	{
		this.r = r;
		this.c = c;
	}

	@Override
	public int compareTo(Coordinate o) {
		// TODO Auto-generated method stub
		// col 먼저 오름차순
		int dCol = Integer.compare(this.c, o.c);
		
		// 만약 같다면 row 내림차순
		if(dCol == 0) return Integer.compare(o.r, this.r);
		else return dCol;
	}
}

class Yeumul implements Comparable<Yeumul>
{
	int r, c, angle;
	ArrayList<Coordinate> list;
	
	
	public Yeumul(int r, int c, int angle, ArrayList<Coordinate> list)
	{
		this.r = r;
		this.c = c;
		this.angle = angle;
		this.list = list;
	}
	
	@Override
	public int compareTo(Yeumul o)
	{
		// 이미 1차 조건 - 획득 가치 최대화는 만족 상태
		// 회전 각도 오름차순
		int dAngle = Integer.compare(this.angle, o.angle);
		// 회전 각도 같은 거 여러개면 col 기준 오름차순
		if(dAngle == 0) 
		{
			// col 같은거 여러개면 row 작은 기준 오름차순
			int dCol = Integer.compare(this.c, o.c);
			if(dCol == 0)
			{
				return Integer.compare(this.r, o.r);
			}
			else return dCol;
		}
		else return dAngle;
	}
}