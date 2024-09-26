// Importation des paquets n�cessaires. Le plugin n'est pas lui-m�me un paquet (pas de mot-cl� package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;						// pour classe GenericDialog et Newimage

public class Filtre_Moyenneur_7x7 implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {

		return DOES_8G;
	}

	public void run( ImageProcessor ip){
		int w = ip.getWidth();
		int h = ip.getHeight();
		
		ImageProcessor ipOutput = ip.createProcessor(w, h);
		
		//Masque 7x7
		int [][] masque = {{1 ,1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}};
		int tailleMasque = 7;
		int sumPoidsMasque = tailleMasque*tailleMasque;
		int commencement = tailleMasque/2;
		for (int y = commencement; y < h - commencement - 1;  y++)
		{
			for ( int x = commencement; x < w - commencement - 1; x++) {

				int sum = 0;

				// On parcourt le masque 7x7
				for(int i = -commencement; i < commencement+1; i++)
				{
					for(int j = -commencement; j < commencement+1; j++)
					{
						// On applique à des coordonnées allant de x - 3 à x + 3 et y - 3 à y + 3 car le masque est de taille 7x7
						 sum += ip.get((x+i), (y+j)) * masque[i+commencement][j+commencement];
					}
				}
				
				ipOutput.set(x, y, (int)(sum/sumPoidsMasque));
			}
		}

		ImagePlus result = new ImagePlus("Result", ipOutput);

		result.show();
		result.updateAndDraw();
	}
}