#include <stdlib.h>
#include <stdio.h>
#include <stdlib.h>

/* Para colocar o caminho usando pontos em vermelho: mudar o programa
 * para gerar comandos
 *
 * <circle cx="31.5" cy="0.5" r="0.2" stroke="red" fill="red" />
 * <circle cx="32.5" cy="0.5" r="0.2" stroke="red" fill="red" />
 * <circle cx="33.5" cy="0.5" r="0.2" stroke="red" fill="red" />
 *
 */

void svgline( int x1, int y1, int x2, int y2, FILE *saida) {
    fprintf(saida," <polyline points=\"%d,%d %d,%d\"/>\n", x1, y1, x2, y2);
}

void printPath1(int x, int y, FILE *saida)
{
  fprintf(saida, "<circle cx=\"%d.5\" cy=\"%d.5\" r=\"0.2\" stroke=\"red\" fill=\"red\" />", x - 1, y - 1);  
}

void printPath2(int x, int y, FILE *saida)
{
  fprintf(saida, "<rect x=\"%d.3\" y=\"%d.3\" width=\"0.4\" height=\"0.4\" style=\"fill:red;stroke:red;stroke-width:0.61;fill-opacity:0.1;stroke-opacity:0.9\" />" , x-1, y-1);
}

void processPath(FILE *path, FILE *saida)
{
  int x, y;
  
  while( 1 )
  {
    fscanf(path, "%d", &x);
    fscanf(path, "%d", &y);

    if( feof( path ) ) return;

    printPath2(x,y,saida);
  }
}

void process ( int w, int h, FILE *entrada, FILE *saida) {

  int N = 8;
  int L = 4;
  int S = 2;
  int O = 1;

  int l = 0;
  int c = 0;

  while ( 1 ) {

    char t = ( char ) getc( entrada );

    if ( feof( entrada ) ) return;

    switch ( t ) {
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9': t -= '0'; break;
        case 'a':
        case 'b':
        case 'c':
        case 'd':
        case 'e':
        case 'f': t = t - 'a' + 10; break;
        default: continue;
    }

    if ( ( t & N ) == N ) svgline( c, l , c + 1, l, saida);
    if ( ( t & S ) == S ) svgline( c, l + 1, c + 1, l + 1, saida);
    if ( ( t & O ) == O ) svgline( c , l , c , l + 1, saida);
    if ( ( t & L ) == L ) svgline( c + 1, l , c + 1, l + 1, saida);

    c++;
    if ( c == w ) {
        c = 0;
        l++;
    }

  }
}

int main ( ) {

  int w, h;
  int i;
  FILE *saida,*entrada;

  saida = fopen("teste.svg","w");
  entrada = fopen("caso5a.txt", "r");

  if(entrada == NULL || saida == NULL)
  {
    printf("Erro!");
    return 0;
  }

  fscanf(entrada, "%d", &w);
  fscanf(entrada, "%d", &h);

  fprintf (saida, "<?xml version=\"1.0\" standalone=\"no\"?>");
  fprintf (saida, "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"%dcm\" height=\"%dcm\" viewBox=\"-0.1 -0.1 %f %f\">", w, h, w+0.2, h+0.2 );
  fprintf (saida, "<g style=\"stroke-width:.1; stroke:black; stroke-linejoin:miter; stroke-linecap:butt; \">" );

  //Gerando o caminho:
  FILE *path = fopen("path.txt", "r");

  if(path == NULL)
  {
    printf("Erro!");
    return 0;
  } 

  processPath(path,saida);
  
  process( w, h, entrada, saida);

  // Finaliza
  fprintf (saida, "</g>" );
  fprintf (saida, "</svg>" );

  fclose(saida);
  fclose(entrada);
  return 0;
}
