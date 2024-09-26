// Importation des paquets n�cessaires. Le plugin n'est pas lui-m�me un paquet (pas de mot-cl� package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;						// pour classe GenericDialog et Newimage

public class Filtre_median implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {

		return DOES_8G;
	}

	public void run( ImageProcessor ip){
		int w = ip.getWidth();
		int h = ip.getHeight();
		
		ImageProcessor ipOutput = ip.createProcessor(w, h);
		
		//Masque 7x7
		int [][] masque = {{1 ,1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}};
		int tailleMasque = 3;
		int sumPoidsMasque = tailleMasque*tailleMasque;
		int commencement = tailleMasque/2;
		
        // Translater le masque en pixels
        int [] pixels = new int [tailleMasque*tailleMasque];
        
        // Trier les pixels voisins selon l'ordre croissant de leurs niveaux de gris
        for (int y = commencement; y < h - commencement - 1;  y++)
        {
            for ( int x = commencement; x < w - commencement - 1; x++) {
                int k = 0;
                for(int i = -commencement; i < commencement+1; i++)
                {
                    for(int j = -commencement; j < commencement+1; j++)
                    {
                        pixels[k] = ip.get((x+i), (y+j));
                        k++;
                    }
                }

                // Tri du tableau
                trier(pixels);
                // On applique la médiane
                ipOutput.set(x, y, mediane(pixels));
            }
        }

		ImagePlus result = new ImagePlus("Result", ipOutput);

		result.show();
		result.updateAndDraw();
	}

    // Méthode pour calculer la médiane d'un tableau d'entiers
    static int mediane (int a[]){
        int [] effectifs = new int [256]; // tableau des effectifs
        for (int i=0 ; i < a.length ; i++) {
            effectifs[a[i ]]++;
        }
        int somme = 0;
        for (int i=0 ; i <= 255 ; i++) {
            somme = somme + effectifs[i] ;
            if (2* somme >= a.length )
                return i;
        }
    return -1;
    }

    // Méthode qui trie un tableau d'entiers dans l'ordre croissant
    static void trier (int a[]) {
        for (int i=0 ; i < a.length ; i++) {
            int min = i;
            for (int j=i+1 ; j < a.length ; j++) {
                if (a[j] < a[min])
                    min = j;
            }
            int temp = a[i];
            a[i] = a[min];
            a[min] = temp;
        }
    }

}