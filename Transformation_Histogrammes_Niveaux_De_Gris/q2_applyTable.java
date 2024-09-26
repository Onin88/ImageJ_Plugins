import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

public class q2_applyTable implements PlugInFilter {

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

        // Initialisation de la LUT
        for(int ng = 0; ng < 256; ng++){
            LUT[ng] =(int) (255.0*(ng-min)/(double)(max-min));
        }

        // Application de la LUT
        ip.applyTable(LUT);
    }
}
