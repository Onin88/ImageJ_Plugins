// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor

public class q2 implements PlugInFilter {

    public int setup(String arg, ImagePlus imp) {
        return DOES_8G; // l'image doit être en niveaux de gris
    }

    public void run(ImageProcessor ip){
        // Récupération des dimensions de l'image
        int l = ip.getWidth();
        int h = ip.getHeight();

        int[] LUT = new int[256];

        int min = 255;
        int max = 0;
        for (int y = 0; y < ip.getHeight(); y++) {
            for (int x = 0; x < ip.getWidth(); x++) {
                int pixelValue = ip.getPixel(x, y);
                if (pixelValue < min) min = pixelValue;
                if (pixelValue > max) max = pixelValue;
            }
        }

        //Initialisation de la LUT
        for(int ng = 0; ng < 256; ng++){
            LUT[ng] =(int) (255.0*(ng-min)/(double)(max-min));
        }
        //Calcul de la transformation
        for(int i = 0; i < l; i++){
            for(int j = 0; j < h; j++){
                int pixelValue = ip.getPixel(i, j);
                ip.putPixel(i, j, LUT[pixelValue]);
            }
        }
    }
}