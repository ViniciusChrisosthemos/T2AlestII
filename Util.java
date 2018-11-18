import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class Util {
	public static int[][] readFile(File file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));

			String[] info = reader.readLine().split(" ");
			int h = Integer.parseInt(info[0]);
			int w = Integer.parseInt(info[1]);
			int line = 0;

			int[][] matrix = new int[h][w];
			String[] auxVector;

			while (reader.ready()) {
				auxVector = reader.readLine().split(" ");
				matrix = putVectorInMatrix(matrix, formatvector(auxVector), line);
				line++;
			}

			reader.close();

			return matrix;

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	private static int[][] putVectorInMatrix(int[][] matrix, int[] vector, int line) {
		for (int i = 0; i < vector.length; i++)
			matrix[line][i] = vector[i];

		return matrix;
	}

	public static void printMatrix(int[][] matrix) {
		for (int[] element : matrix) {
			for (int j = 0; j < matrix[0].length; j++)
				System.out.print(element[j] + " ");
			System.out.print("\n");
		}
	}

	private static int[] formatvector(String[] oldVector) {
		int[] newVector = new int[oldVector.length];
		char c;

		for (int i = 0; i < oldVector.length; i++) {
			c = oldVector[i].charAt(0);
			switch (c) {
				case 'a':
				case 'b':
				case 'c':
				case 'd':
				case 'e':
				case 'f':
					newVector[i] = c - 'a' + 10;
					break;
				default:
					newVector[i] = c - '0';
					break;
			}
		}

		return newVector;
	}

	private static void process(int w, int h, File entrada, File saida) throws IOException {
		Scanner sc = new Scanner(entrada);
		FileWriter fw = new FileWriter(saida, true);
		BufferedWriter bw = new BufferedWriter(fw);
		int N = 8;
		int L = 4;
		int S = 2;
		int O = 1;

		int l = 0;
		int c = 0;
		sc.nextLine();
		while (sc.hasNext()) {
			char t = sc.next().charAt(0);
			switch (t) {
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					t -= '0';
					break;
				case 'a':
				case 'b':
				case 'c':
				case 'd':
				case 'e':
				case 'f':
					t = (char) (t - 'a' + 10);
					break;
				default:
					continue;
			}

			int x1, x2, y1, y2;
			if ((t & N) == N) {
				x1 = c;
				y1 = l;
				x2 = c + 1;
				y2 = l;
				bw.write(" <polyline points=\"" + x1 + "," + y1 + " " + x2 + "," + y2 + "\"/>\n");
			}
			if ((t & S) == S) {
				x1 = c;
				y1 = l + 1;
				x2 = c + 1;
				y2 = l + 1;
				bw.write(" <polyline points=\"" + x1 + "," + y1 + " " + x2 + "," + y2 + "\"/>\n");
			}
			if ((t & O) == O) {
				x1 = c;
				y1 = l;
				x2 = c;
				y2 = l + 1;
				bw.write(" <polyline points=\"" + x1 + "," + y1 + " " + x2 + "," + y2 + "\"/>\n");
			}
			if ((t & L) == L) {
				x1 = c + 1;
				y1 = l;
				x2 = c + 1;
				y2 = l + 1;
				bw.write(" <polyline points=\"" + x1 + "," + y1 + " " + x2 + "," + y2 + "\"/>\n");
			}

			c++;
			if (c == w) {
				c = 0;
				l++;
			}
		}
		sc.close();
		bw.flush();
		bw.close();
	}

	public static void generateSVG(int w, int h, File lab, LinkedList<String> path, String svg) {
		try {
			File saida = new File(svg);
			FileWriter fw = new FileWriter(saida);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("<?xml version=\"1.0\" standalone=\"no\"?>\n");
			bw.write("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" + w + "cm\" height=\"" + h
					+ "cm\" viewBox=\"-0.1 -0.1 " + (w + 0.2) + " " + (h + 0.2) + "\">\n");
			bw.write("<g style=\"stroke-width:.1; stroke:black; stroke-linejoin:miter; stroke-linecap:butt; \">\n");
			bw.flush();
			bw.close();
			process(w, h, lab, saida);
			fw = new FileWriter(saida, true);
			bw = new BufferedWriter(fw);
			StringBuilder sbPoints = new StringBuilder();
			Iterator<String> it = path.listIterator();
			while (it.hasNext()) {
				String node[] = it.next().split(",");
				int x = Integer.parseInt(node[0]);
				int y = Integer.parseInt(node[1]);
				sbPoints.append(x + ".5," + y + ".5 ");
			}
			bw.write("<polyline points='" + sbPoints.toString()
					+ "' style='fill-opacity:0;stroke:red;stroke-width:.2' />\n");
			bw.write("</g>\n");
			bw.write("</svg>\n");
			bw.flush();
			bw.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String casos[] = new String[] { "caso25a", "caso50a", "caso75a", "caso100a", "caso150a", "caso200a", "caso250a",
				"caso300a", "caso400a", "caso500a" };
		long agora;
		for (String caso : casos) {
			System.out.println("Caso: " + caso);
			agora = System.currentTimeMillis();
			File f = new File(caso + ".txt");
			int[][] infos = Util.readFile(f);
			Grafo g = new Grafo(infos);
			LinkedList<String> path = g.getShortestPathGaps();
			g.writePath(path, caso + "_caminho.txt");
			generateSVG(infos.length, infos[0].length, f, path, caso + ".svg");
			long tempo = System.currentTimeMillis() - agora;
			System.out.println("Tempo: " + tempo + "ms");
			System.out.println("-------------------");
		}
	}

}
