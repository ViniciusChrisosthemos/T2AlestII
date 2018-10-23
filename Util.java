import java.io.*;

public class Util{
    public static int[][] readFile(File file)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
        
            String[] info = reader.readLine().split(" ");
            int h = Integer.parseInt(info[0]);
            int w = Integer.parseInt(info[1]);
            int line = 0;

            int[][] matriz = new int[h][w];
            String[] auxVector;

            while(reader.ready())
            {
                auxVector = reader.readLine().split(" ");
                matriz = putVectorInMatriz(matriz, formatvector(auxVector), line);
                line++;
            }

            reader.close();

            return matriz;

        }catch(IOException ex)
        {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    private static int[][] putVectorInMatriz(int[][] matriz, int[] vector, int line)
    {
        for(int i=0; i<vector.length; i++)
        {
            matriz[line][i] = vector[i];
        }

        return matriz;
    }

    public static void printMatriz(int[][] matriz)
    {
        for(int i=0; i<matriz.length; i++)
        {
            for(int j=0; j<matriz[0].length; j++)
            {
                System.out.print(matriz[i][j]+" ");
            }
            System.out.print("\n");
        }
    }

    private static int[] formatvector(String[] oldVector)
    {
        int[] newVector = new int[oldVector.length];
        char c;

        for(int i=0; i<oldVector.length; i++)
        {
            c = oldVector[i].charAt(0);
            switch(c)
            {
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                    newVector[i] = ((int) c) - ((int)'a') + 10;
                    break;
                default:
                    newVector[i] = (int) c - ((int) '0');
                    break;  
            }
        }

        return newVector;
    }

    public static void main(String[] args)
    {
        File f = new File("caso25a.txt");
        int[][] infos = Util.readFile(f);

        Grafo g = new Grafo(infos);
        g.printGaps();
    }

}