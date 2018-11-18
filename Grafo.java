import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Grafo {

	private List<Node> edges;
	private List<Node> gaps;

	public Grafo(int[][] infos) {
		edges = new ArrayList<>(infos.length);
		gaps = new ArrayList<>(2);
		formatGraph(infos);
	}

	private void formatGraph(int[][] infos) {
		Node atual;
		Node proximo;
		Node aux;
		int elemento;
		Queue<Node> fila = new LinkedList<>();

		for (int i = 0; i < infos.length; i++) {
			atual = new Node(i, 0);
			for (int j = 0; j < infos[0].length; j++) {
				elemento = infos[i][j];
				proximo = new Node(i, j + 1);

				if ((elemento & 1) != 1)
					if (j == 0)
						gaps.add(atual);
				if ((elemento & 2) != 2)
					if (i == infos.length - 1)
						gaps.add(atual);
					else
						fila.add(atual);
				if ((elemento & 4) != 4)
					if (j == infos[0].length - 1)
						gaps.add(atual);
					else {
						atual.addV(proximo);
						proximo.addV(atual);
					}
				if ((elemento & 8) != 8)
					if (i == 0)
						gaps.add(atual);
					else {
						aux = fila.remove();
						atual.addV(aux);
						aux.addV(atual);
					}

				edges.add(atual);
				atual = proximo;
			}
		}
	}

	public void printGraph() {
		System.out.println(edges);
	}

	public void printGaps() {
		System.out.println(gaps);
	}

	private LinkedList<String> shortestPath(Node from, Node to) {
		LinkedList<Node> queue = new LinkedList<>();
		LinkedList<String> path = new LinkedList<>();
		HashMap<Node, Node> prev = new HashMap<>();

		if (from == to)
			return path;
		queue.add(from);

		while (queue.size() != 0) {
			Node curr = queue.poll();
			Iterator<Node> i = curr.v.listIterator();

			while (i.hasNext()) {
				Node n = i.next();

				if (!prev.containsKey(n)) {
					prev.put(n, curr);

					if (n == to) {
						while (n != from) {
							path.addFirst(n.y + "," + n.x);
							n = prev.get(n);
						}
						path.addFirst(from.y + "," + n.x);
						return path;
					}

					queue.add(n);
				}
			}
		}
		return path;
	}

	public LinkedList<String> getShortestPathGaps() {
		return shortestPath(gaps.get(0), gaps.get(1));
	}

	public void writePath(LinkedList<String> path, String file) {
		System.out.println("Célula de Entrada: " + path.getFirst());
		System.out.println("Célula de Saída: " + path.getLast());
		System.out.println("Tamanho do caminho: " + path.size());
		try {
			FileWriter fw = new FileWriter(new File(file));
			BufferedWriter bw = new BufferedWriter(fw);
			path.forEach(p -> {
				try {
					bw.write(p + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class Node {
		List<Node> v;
		int x;
		int y;

		public Node(int x, int y) {
			v = new LinkedList<>();
			this.x = x;
			this.y = y;
		}

		public void addV(Node element) {
			v.add(element);
		}

		@Override
		public String toString() {
			return "(" + x + "," + y + ")";
		}
	}
}
