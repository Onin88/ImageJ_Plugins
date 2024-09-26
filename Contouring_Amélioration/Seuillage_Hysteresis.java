import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import ij.gui.*;

public class Seuillage_Hysteresis implements PlugInFilter {

    
    private double Sh = 100; // Valeur de seuil haut par défaut
    private double Sb = 50; // Valeur de seuil bas par défaut

    public int setup(String arg, ImagePlus imp) {
        return DOES_8G;
    }

    public void run(ImageProcessor ip) {
        GenericDialog gd = new GenericDialog("Seuillage par Hystérésis");
        gd.addNumericField("Seuil Haut (Sh) :", Sh, 0);
        gd.addNumericField("Seuil Bas (Sb) :", Sb, 0);
        gd.showDialog();
        if (gd.wasCanceled()) return;
        
        Sh = gd.getNextNumber();
        Sb = gd.getNextNumber();

        int w = ip.getWidth();
        int h = ip.getHeight();
        
        ImageProcessor ipOutput = ip.createProcessor(w, h);

        // Masque Sobel
        int [][] Sx = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int [][] Sy = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};
        int tailleMasque = 3;
        int commencement = tailleMasque / 2;

        int[][] Gx = new int[w][h];
        int[][] Gy = new int[w][h];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int sumx = 0;
                int sumy = 0;

                // On parcourt le masque 3x3
                for(int i = -commencement; i < commencement + 1; i++) {
                    for(int j = -commencement; j < commencement + 1; j++) {
                        // On applique le masque 1 sur les x et le masque 2 sur les y
                        sumx += ip.getPixel(x + i, y + j) * Sx[i + commencement][j + commencement];
                        sumy += ip.getPixel(x + i, y + j) * Sy[i + commencement][j + commencement];
                    }
                }
                double valeur = Math.sqrt((sumx * sumx) + (sumy * sumy));

                Gx[x][y] = sumx;
                Gy[x][y] = sumy;

                if (valeur < 0) {
                    valeur = 0;
                } else if (valeur > 255) {
                    valeur = 255;
                }

                ipOutput.set(x, y, (int) valeur);
            }
        }

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                double valeur = ipOutput.getPixel(x, y);

                // Direction du gradient theta = arctan(Gx/Gy)
                double theta = Math.atan2(Gy[x][y], Gx[x][y]);
                theta = Math.toDegrees(theta);
                theta = Math.round(theta * 4 / Math.PI) * Math.PI / 4;

                /* Suppression des non-maxima : Si la norme du gradient en un pixel (x,y) est
                inférieure à la norme du gradient d’un de ses 2
                voisins le long de la direction du gradient, alors
                mettre la norme pour le pixel (x,y) à zéro */
                if (theta == 0 || theta == 180) {
                    if (valeur < ipOutput.getPixel(x - 1, y) || valeur < ipOutput.getPixel(x + 1, y)) {
                        valeur = 0;
                    }
                } else if (theta == 45 || theta == 225) {
                    if (valeur < ipOutput.getPixel(x - 1, y - 1) || valeur < ipOutput.getPixel(x + 1, y + 1)) {
                        valeur = 0;
                    }
                } else if (theta == 90 || theta == 270) {
                    if (valeur < ipOutput.getPixel(x, y - 1) || valeur < ipOutput.getPixel(x, y + 1)) {
                        valeur = 0;
                    }
                } else if (theta == 135 || theta == 315) {
                    if (valeur < ipOutput.getPixel(x + 1, y - 1) || valeur < ipOutput.getPixel(x - 1, y + 1)) {
                        valeur = 0;
                    }
                }
                ipOutput.set(x, y, (int) valeur);
            }
        }

        // Application des seuils Sh et Sb pour identifier les contours
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                double valeur = ipOutput.getPixel(x, y);

                if (valeur < Sb) {
                    ipOutput.set(x, y, 0); // Pixel non contour
                } else if (valeur > Sh) {
                    ipOutput.set(x, y, 255); // Pixel contour
                } else {
                    boolean connected = false;
                    // Vérification de la connexion avec un pixel de contour déjà accepté
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            if (x + i >= 0 && x + i < w && y + j >= 0 && y + j < h && ipOutput.get(x + i, y + j) == 255) {
                                connected = true;
                                break;
                            }
                        }
                    }
                    if (connected) {
                        ipOutput.set(x, y, 255);
                    } else {
                        ipOutput.set(x, y, 0);
                    }
                }
            }
        }

        ImagePlus result = new ImagePlus("Result", ipOutput);
        result.show();
        result.updateAndDraw();
    }
}
