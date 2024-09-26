import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

public class Egalisation_Histogramme_RGB_Q4 implements PlugInFilter {
    public int setup(String arg, ImagePlus imp) {
        return DOES_RGB; 
    }

    public void run(ImageProcessor ip) {
        int w = ip.getWidth();
        int h = ip.getHeight();

        // Initialisation des histogrammes pour les composantes R, G et B
        int[] histoRed = new int[256];
        int[] histoGreen = new int[256];
        int[] histoBlue = new int[256];

        // Calcul des histogrammes pour chaque composante RGB
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int[] rgb = new int[3];
                ip.getPixel(i, j, rgb);
                histoRed[rgb[0]]++;
                histoGreen[rgb[1]]++;
                histoBlue[rgb[2]]++;
            }
        }

        // Intensité de l'image en niveaux de gris
        int[] intensite = new int[256];
        for (int i = 0; i < 256; i++) {
            intensite[i] = (int)(0.3*histoRed[i] + 0.59 * histoGreen[i] + 0.11 * histoBlue[i]); // 0.3 * R + 0.59 * G + 0.11 * B
        }

       // Calcul de l'histogramme cumulé
        int[] histoCumule = new int[256];
        histoCumule[0] = intensite[0];
        for (int i = 1; i < 256; i++) {
            histoCumule[i] = histoCumule[i - 1] + intensite[i];
        }

        // On normalise pour obtenir une transformation égalisée
        int nbPixels = w * h;
        int[] lut = new int[256];
        for (int i = 0; i < 256; i++) {
            // Application de la transformation égalisée à chaque composante R, G et B
            lut[i] = (int)(255.0*histoCumule[i]/nbPixels+0.5);
        }

        ImageProcessor ipOutput = ip.createProcessor(w, h);

        // On applique à chaque pixel la transfromation égalisée pour chaque composante R, G et B
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int[] rgb = new int[3];
                ip.getPixel(i, j, rgb);
                rgb[0] = lut[rgb[0]]; // Composante R
                rgb[1] = lut[rgb[1]]; // Composante G
                rgb[2] = lut[rgb[2]]; // Composante B
                ipOutput.putPixel(i, j, rgb);
            }
        }

        ImagePlus result = new ImagePlus("résultat", ipOutput);

        result.updateAndDraw();
        result.show();
    }
}