// package S2024.상반기.오전.P002;

import java.io.*;
import java.util.*;

public class Main {
	
	static final int INF = Integer.MAX_VALUE;
	
	static int Q, N, M;
	static boolean isAvailableExist;
	static int[] dist;
	
	
	static ArrayList<Edge>[] arrList;
	static Item[] list = new Item[30_000 + 1];
	static boolean[] isAvailable;
	static PriorityQueue<sItem> ansList = new PriorityQueue<>();
	static ArrayList<Integer> idSave = new ArrayList<>();

	public static void main(String[] args) throws Exception {
		// 파일 입력
		// System.setIn(new FileInputStream("src/S2024/상반기/오전/P002/input.txt"));
		
		// 라이브러리
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		StringTokenizer st;
		
		// 쿼리의 개수를 받는다.
		Q = Integer.parseInt(br.readLine());
		
		// 여행 상품 저장
		
		StringBuilder sb = new StringBuilder();
		for(int q = 1; q <= Q; q++)
		{
			st = new StringTokenizer(br.readLine());
			int op = Integer.parseInt(st.nextToken());
			
			// 랜드 건설
			if(op == 100)
			{
				// N과 M을 받는다.
				N = Integer.parseInt(st.nextToken());
				M = Integer.parseInt(st.nextToken());
				
				arrList = new ArrayList[N];
				dist = new int[N];
				
				// 배열 초기화
				for(int i = 0; i < N; i++)
				{
					arrList[i] = new ArrayList<>();
				}
				
				// 간선을 받는다.
				for(int m = 0; m < M; m++)
				{
					int u = Integer.parseInt(st.nextToken());
					int v = Integer.parseInt(st.nextToken());
					int w = Integer.parseInt(st.nextToken());
					
					// 양방향이니까 모두 저장
					arrList[u].add(new Edge(v, w));
					arrList[v].add(new Edge(u, w));
				}
				
				// 다익스트라를 실행한다.
				dijkstra(0); // 최초의 시작점은 0임
				isAvailableExist = false;
				isAvailable = new boolean[30_000 + 1];
			}
			// 여행 상품 생성
			else if(op == 200)
			{
				// 사업 아이템을 추가한다.
				int id = Integer.parseInt(st.nextToken());
				int rev = Integer.parseInt(st.nextToken());
				int dest = Integer.parseInt(st.nextToken());
				
				// 리스트에 추가
				list[id] = new Item(id, rev, dest);
				// idSave에 저장
				idSave.add(id);
				// pq에 추가
				// dist가 INF 이거나 cost > rev 이면 판매 불가
				if(dist[dest] == INF || dist[dest] > rev)
				{
					isAvailable[id] = true;
					continue;
				}
				
				// 그 외에는 ansList에 추가
				ansList.offer(new sItem(id, rev - dist[dest]));
				isAvailableExist = true;
				isAvailable[id] = true;
				
			}
			
			// 여행 상품 취소
			else if(op == 300)
			{
				int id = Integer.parseInt(st.nextToken());
				
				list[id] = null;
				isAvailable[id] = false;
				
			}
			// 최적의 여행상품 판매
			else if(op == 400)
			{
				// 리스트에 있는 것들과 dist를 이용해 계산 (여기서 dist가 cost 임)
				
				// 만약 판매 가능한 게 없으면 -1 출력
				if(!isAvailableExist) sb.append(-1).append("\n");
				else
				{
					boolean isExist = false;
					
					while(!isExist)
					{
						// pq에 없으면 탈출임;;
						if(ansList.isEmpty()) break;
						// pq에서 하나 꺼냄
						sItem answer = ansList.poll();
						// id에 실제로 값이 있는지 확인
						if(isAvailable[answer.id])
						{
							// 존재한다면 값으로 입력
							sb.append(answer.id).append("\n");
							// isExist를 참으로 바꿈
							isExist = true;
							// 명단에서 제거
							list[answer.id] = null;
							isAvailable[answer.id] = false;
						}
					}
					
					if(!isExist) sb.append(-1).append("\n");
				}
			}
			// 출발지 변경
			else
			{
				int start = Integer.parseInt(st.nextToken());
				dijkstra(start);
				
				// 다익스트라로 바꿨으니까 아이템도 다시 넣기
				isAvailableExist = false;
				boolean[] newIsAvailable = new boolean[30_000 + 1];
				// 일단 ansList를 비움
				ansList.clear();
				
				ArrayList<Integer> newIdSave = new ArrayList<>();
				
				for(int i = 0; i < idSave.size(); i++)
				{
					// 비어 있는지 확인
					if(!isAvailable[idSave.get(i)]) continue;
					
					// 그 외에는 newIdSave에 저장
					newIdSave.add(idSave.get(i));
					
					// 판매 불가하면 다음으로 건너뜀
					if(dist[list[idSave.get(i)].dest] == INF || dist[list[idSave.get(i)].dest] > list[idSave.get(i)].revenue)
					{
						continue;
					}
					
					// 그 외에는 ansList에 추가
					ansList.offer(new sItem(list[idSave.get(i)].id, list[idSave.get(i)].revenue - dist[list[idSave.get(i)].dest]));
					isAvailableExist = true;
					newIsAvailable[list[idSave.get(i)].id] = true;
					
				}
				
				// 복사
				isAvailable = newIsAvailable;
				idSave = newIdSave;
			}
				
		}
		
		// 정답 입력
		bw.write(sb.toString());
		
		// 정답 출력
		bw.flush();
		
		
		// 자원 반환
		bw.close();
		br.close();

	}
	
	
	static void dijkstra(int start)
	{
		// 우선순위 큐를 선언한다.
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		
		// dist를 INF로 채운다.
		Arrays.fill(dist, INF);
		
		// 시작점의 dist를 0으로 설정한다.
		dist[start] = 0;
		
		// pq에 시작점을 넣는다.
		pq.offer(new Edge(start, dist[start]));
		
		while(!pq.isEmpty())
		{
			// 큐에서 꺼내옴
			Edge now = pq.poll();
			
			// 이미 업데이트 된 거라면 건너뜀
			if(dist[now.to] < now.weight) continue;
			
			// 그게 아니라면 인접 리스트를 순회함
			for(Edge next : arrList[now.to])
			{
				// dist 업데이트가 필요하다면 업데이트
				if(dist[next.to] > now.weight + next.weight)
				{
					dist[next.to] = now.weight + next.weight;
					pq.offer(new Edge(next.to, dist[next.to]));
				}
			}
		}
		
	}

}

class sItem implements Comparable<sItem>
{
	int id, revCost;
	
	public sItem(int id, int revCost)
	{
		this.id = id;
		this.revCost = revCost;
	}

	@Override
	public int compareTo(sItem o) {
		// TODO Auto-generated method stub
		// 먼저 revCost를 내림차순
		int dRC = Integer.compare(o.revCost, this.revCost);
		
		// 동일한게 있다면 id가 작은 것을 출력
		if(dRC == 0) return Integer.compare(this.id, o.id);
		else return dRC;
	}
}

class Item
{
	int id, revenue, dest;
	
	public Item(int id, int revenue, int dest)
	{
		this.id = id;
		this.revenue = revenue;
		this.dest = dest;
	}
}

class Edge implements Comparable<Edge>
{
	int to, weight;
	
	public Edge(int to, int weight)
	{
		this.to = to;
		this.weight = weight;
	}

	@Override
	public int compareTo(Edge o) {
		// TODO Auto-generated method stub
		return Integer.compare(this.weight, o.weight);
	}
}