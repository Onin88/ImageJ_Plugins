// Importation des paquets n�cessaires. Le plugin n'est pas lui-m�me un paquet (pas de mot-cl� package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;						// pour classe GenericDialog et Newimage

public class Masque_Sobel implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {

		return DOES_8G;
	}

	public void run( ImageProcessor ip){
		int w = ip.getWidth();
		int h = ip.getHeight();
		
		ImageProcessor ipOutputX = ip.createProcessor(w, h);
		ImageProcessor ipOutputY = ip.createProcessor(w, h);

		//Masque Sobel
		int [][] Sx = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
		// Estimation de la norme du gradient
		int [][] Sy = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
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
				double valeurX = sumx;

				if(valeurX < 0){
					valeurX = 0;
				}else{
					if(valeurX > 255){
						valeurX = 255;
					}
				}

				double valeurY = sumy;

				if(valeurY < 0){
					valeurY = 0;
				}else{
					if(valeurY > 255){
						valeurY = 255;
					}
				}

				ipOutputX.set(x, y, (int)valeurX);
				ipOutputY.set(x, y, (int)valeurY);
			}
		}

		ImagePlus resultX = new ImagePlus("ipOutputX", ipOutputX);
		ImagePlus resultY = new ImagePlus("ipOutputY", ipOutputY);

		resultX.show();
		resultX.updateAndDraw();

		resultY.show();
		resultY.updateAndDraw();
	}
}