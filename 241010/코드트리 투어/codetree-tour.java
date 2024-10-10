// package S2024.상반기.오전.P002;

import java.io.*;
import java.util.*;

public class Main {
	
	static final int INF = Integer.MAX_VALUE;
	
	static int Q, N, M;
	static int[] dist;
	
	static ArrayList<Edge>[] arrList;

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
		ArrayList<Item> list = new ArrayList<>();
		
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
				
			}
			// 여행 상품 생성
			else if(op == 200)
			{
				// 사업 아이템을 추가한다.
				int id = Integer.parseInt(st.nextToken());
				int rev = Integer.parseInt(st.nextToken());
				int dest = Integer.parseInt(st.nextToken());
				
				list.add(new Item(id, rev, dest));
			}
			
			// 여행 상품 취소
			else if(op == 300)
			{
				int id = Integer.parseInt(st.nextToken());
				
				for(int i = 0; i < list.size(); i++)
				{
					if(list.get(i).id == id)
					{
						list.remove(i);
						break;
					}
				}
			}
			// 최적의 여행상품 판매
			else if(op == 400)
			{
				// 리스트에 있는 것들과 dist를 이용해 계산 (여기서 dist가 cost 임)
				boolean isAvailableExist = false;
				int max = Integer.MIN_VALUE;
				PriorityQueue<sItem> ansList = new PriorityQueue<>();
				
				for(int i = 0; i < list.size(); i++)
				{
					// dist가 INF 이거나 cost > rev 이면 판매 불가
					if(dist[list.get(i).dest] == INF || dist[list.get(i).dest] > list.get(i).revenue)
					{
						continue;
					}
					
					// 만약 rev - cost가 최대와 같으면 list에 집어 넣는다.
					if(list.get(i).revenue - dist[list.get(i).dest] == max)
					{
						ansList.offer(new sItem(list.get(i).id, i));
						isAvailableExist = true;
					}
					
					// 만약 rev - cost가 최대보다 크면 ansList를 초기화하고 max를 업데이트 한 후 ansList에 넣는다.
					if(list.get(i).revenue - dist[list.get(i).dest] > max)
					{
						max = list.get(i).revenue - dist[list.get(i).dest];
						ansList.clear();
						ansList.offer(new sItem(list.get(i).id, i));
						isAvailableExist = true;
					}
				}
				
				// 만약 판매 가능한 게 없으면 -1 출력
				if(!isAvailableExist) sb.append(-1).append("\n");
				else
				{
					// pq에서 하나 꺼냄
					sItem answer = ansList.poll();
					// id를 정답으로 입력
					sb.append(answer.id).append("\n");
					// 명단에서 제거
					list.remove(answer.idx);
				}
			}
			// 출발지 변경
			else
			{
				int start = Integer.parseInt(st.nextToken());
				dijkstra(start);
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
	int id, idx;
	
	public sItem(int id, int idx)
	{
		this.id = id;
		this.idx = idx;
	}

	@Override
	public int compareTo(sItem o) {
		// TODO Auto-generated method stub
		return Integer.compare(this.id, o.id);
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