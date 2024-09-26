import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import ij.gui.*;

public class Norme_Gradient_Seuil implements PlugInFilter {
    private double seuil = 100; // Valeur de seuil par défaut

    public int setup(String arg, ImagePlus imp) {
        return DOES_8G;
    }

    public void run(ImageProcessor ip) {
        int w = ip.getWidth();
        int h = ip.getHeight();

        // Demande de la valeur du seuil
        GenericDialog gd = new GenericDialog("Seuil");
        gd.addNumericField("Seuil:", seuil, 0);
        gd.showDialog();
        if (gd.wasCanceled()) return;
        seuil = gd.getNextNumber();

        ImageProcessor ipOutput = ip.createProcessor(w, h);

        // Masque Sobel
        int[][] Sx = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] Sy = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};
        int tailleMasque = 3;
        int commencement = tailleMasque / 2;

        for (int y = commencement; y < h - commencement - 1; y++) {
            for (int x = commencement; x < w - commencement - 1; x++) {
                int sumx = 0;
                int sumy = 0;

                // Appliquer les masques Sx et Sy
                for (int i = -commencement; i < commencement + 1; i++) {
                    for (int j = -commencement; j < commencement + 1; j++) {
                        sumx += ip.getPixel(x + i, y + j) * Sx[i + commencement][j + commencement];
                        sumy += ip.getPixel(x + i, y + j) * Sy[i + commencement][j + commencement];
                    }
                }
                double valeur = Math.sqrt((sumx * sumx) + (sumy * sumy));

                // Seuillage de l'image
                if (valeur < seuil) {
                    ipOutput.set(x, y, 0);
                } else {
                    ipOutput.set(x, y, 255);
                }
            }
        }

        ImagePlus result = new ImagePlus("Résultat avec seuil", ipOutput);
        result.show();
        result.updateAndDraw();
    }
}
