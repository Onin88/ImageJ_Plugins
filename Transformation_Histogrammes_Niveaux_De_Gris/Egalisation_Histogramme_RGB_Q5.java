// Importation des paquets nécessaires
import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import ij.gui.*;

public class Egalisation_Histogramme_RGB_Q5 implements PlugInFilter {

    public int setup(String arg, ImagePlus imp) {
        return DOES_RGB;
    }

    public void run(ImageProcessor ip) {
        int w = ip.getWidth();
        int h = ip.getHeight();

        ImageProcessor ipOutput = ip.createProcessor(w, h);

        int nbPixels = w * h;

        // Création des tableaux pour stocker les histogrammes de chaque canal
        int[][] H = new int[3][256]; // 3 canaux pour RGB

        // Remplissage des histogrammes
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int[] rgb = ip.getPixel(x, y, null);
                for (int c = 0; c < 3; c++) {
                    H[c][rgb[c]]++;
                }
            }
        }

        // Affichage des valeurs de H
        for (int i = 0; i < 256; i++) {
            IJ.log("H[" + i + "] = " + H[0][i] + " " + H[1][i] + " " + H[2][i]);
        }

        // Histogrammes normalisés pour chaque canal
        float[][] P = new float[3][256];
        for (int c = 0; c < 3; c++) {
            for (int i = 0; i < 256; i++) {
                P[c][i] = (float) H[c][i] / nbPixels;
            }
        }

        // Affichage des valeurs de P
        for (int i = 0; i < 256; i++) {
            IJ.log("P[" + i + "] = " + P[0][i] + " " + P[1][i] + " " + P[2][i]);
        }

        // Histogrammes cumulés normalisés pour chaque canal
        float[][] R = new float[3][256];
        for (int c = 0; c < 3; c++) {
            R[c][0] = P[c][0];
            for (int i = 1; i < 256; i++) {
                R[c][i] = R[c][i - 1] + P[c][i];
            }
        }

        // Affichage des valeurs de R
        for (int i = 0; i < 256; i++) {
            IJ.log("R[" + i + "] = " + R[0][i] + " " + R[1][i] + " " + R[2][i]);
        }

        // Egalisation de l'histogramme pour chaque canal
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int[] rgb = ip.getPixel(x, y, null);
                int[] rgbOutput = new int[3];
                for (int c = 0; c < 3; c++) {
                    rgbOutput[c] = (int) ((255) * R[c][rgb[c]]);
                }
                ipOutput.putPixel(x, y, rgbOutput);
            }
        }

        ImagePlus result = new ImagePlus("Result", ipOutput);

        result.show();
        result.updateAndDraw();
    }
}
