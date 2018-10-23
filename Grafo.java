import java.util.*;

public class Grafo
{

    private List<Node> edges;
    private List<Node> gaps;

    public Grafo(int[][] infos)
    {
        edges = new ArrayList<>(infos.length);
        gaps = new ArrayList<>(2);
        formatGraph(infos);
    }

    private void formatGraph(int[][] infos)
    {
        Node atual;
        Node proximo;
        Node aux;
        int elemento;
        Queue<Node> fila = new LinkedList<>();

        for(int i=0; i<infos.length; i++)
        {
            atual = new Node(i,0);
            for(int j=0; j<infos[0].length; j++)
            {
                elemento = infos[i][j];
                proximo = new Node(i,j+1);

                if((elemento & 1) != 1)
                {
                    if(j == 0)
                        gaps.add(atual);
                }
                if((elemento & 2) != 2)
                {
                    if(i == infos.length - 1)
                        gaps.add(atual);
                    else
                        fila.add(atual);
                }
                if((elemento & 4) != 4)
                {
                    if(j == infos[0].length - 1)
                    {
                        gaps.add(atual);
                    }
                    else
                    {
                        atual.addV(proximo);
                        proximo.addV(atual);
                    }
                }
                if((elemento & 8) != 8)
                {
                    if(i == 0)
                    {
                        gaps.add(atual);
                    }
                    else
                    {
                        aux = fila.remove();
                        atual.addV(aux);
                        aux.addV(atual);
                    }
                }

                edges.add(atual);
                atual = proximo;
            }
        }
    }

    public void printGraph()
    {
        System.out.println(edges);
    }

    public void printGaps()
    {
        System.out.println(gaps);
    }

    public class Node
    {
        List<Node> v;
        int x;
        int y;

        public Node(int x, int y)
        {
            v = new LinkedList<>();
            this.x = x;
            this.y = y;
        }

        public void addV(Node element)
        {
            v.add(element);
        }

        @Override
        public String toString()
        {
            return x + " " + y;
        }
    }
}
