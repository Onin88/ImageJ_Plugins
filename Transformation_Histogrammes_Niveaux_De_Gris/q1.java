// Importation des paquets nécessaires. Le plugin n'est pas lui-même un paquet (pas de mot-clé package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor

public class q1 implements PlugInFilter {

    public int setup(String arg, ImagePlus imp) {
        return DOES_8G; // l'image doit être en niveaux de gris
    }

    public void run(ImageProcessor ip){
        // Récupération des dimensions de l'image
        int l = ip.getWidth();
        int h = ip.getHeight();

        // Converti l'iamge en un tableau de pixels
        byte[] pixels = (byte[]) ip.getPixels();

        // On calcule la moyenne des niveaux de gris de l'image
        int sum = 0;
        for(int i = 0; i < pixels.length; i++){
            sum += pixels[i] & 0xff; // Car sur 8 bits on fait masquage
        }
        float moy = sum / pixels.length;

        // On calcule la somme de 0 à H-1 et 0 à L-1 de (I(x,y) - moy)²
        double num = 0;
        for(int i = 0; i < pixels.length; i++){
            num += Math.pow((pixels[i] & 0xff) - moy, 2);
        }

        // Ecart type des variations des niveaux de gris
        double C = Math.sqrt(num / pixels.length);

        // Affichage des résultats
        IJ.log("Moyenne des niveaux de gris : " + moy);
        IJ.log("Ecart-type des variations des niveaux de gris : " + C);
    }
}