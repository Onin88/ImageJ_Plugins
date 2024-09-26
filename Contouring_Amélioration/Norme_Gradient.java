// Importation des paquets n�cessaires. Le plugin n'est pas lui-m�me un paquet (pas de mot-cl� package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;						// pour classe GenericDialog et Newimage

public class Norme_Gradient implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {

		return DOES_8G;
	}

	public void run( ImageProcessor ip){
		int w = ip.getWidth();
		int h = ip.getHeight();
		
		ImageProcessor ipOutput = ip.createProcessor(w, h);

		//Masque Sobel
		int [][] Sx = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
		// Estimation de la norme du gradient
		int [][] Sy = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};
		int tailleMasque = 3;
		int sumPoidsMasque = tailleMasque*tailleMasque;
		int commencement = tailleMasque/2;
		
		for (int y = commencement; y < h - commencement - 1;  y++)
		{
			for ( int x = commencement; x < w - commencement - 1; x++) {

				int sumx = 0;
				int sumy = 0;

				// On parcourt le masque 3x3
				for(int i = -commencement; i < commencement+1; i++)
				{
					for(int j = -commencement; j < commencement+1; j++)
					{
						// On applique le masque 1 sur les x et le masque 2 sur les y
						sumx += ip.getPixel(x+i, y+j)*Sx[i+commencement][j+commencement];
						sumy += ip.getPixel(x+i, y+j)*Sy[i+commencement][j+commencement];
					}
				}
				double valeur = Math.sqrt((sumx*sumx)+(sumy*sumy));

				if(valeur < 0){
					valeur = 0;
				}else{
					if(valeur > 255){
						valeur = 255;
					}
				}

                // Calculer les directions du gradient dans l'image
                //double theta = Math.atan2(sumy, sumx);
                // Arrondi de theta par multiples de 45 degré
                //theta = Math.round(theta * 4 / Math.PI) * Math.PI / 4;

				ipOutput.set(x, y, (int)valeur);
			}
		}

		ImagePlus result = new ImagePlus("Result", ipOutput);

		result.show();
		result.updateAndDraw();
	}
}