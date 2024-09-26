// Importation des paquets n�cessaires. Le plugin n'est pas lui-m�me un paquet (pas de mot-cl� package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;						// pour classe GenericDialog et Newimage

public class Filtre_Moyenneur_5x5 implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {

		return DOES_8G;
	}

	public void run( ImageProcessor ip){
		int w = ip.getWidth();
		int h = ip.getHeight();
		
		ImageProcessor ipOutput = ip.createProcessor(w, h);
		
		//Masque 5x5
		int [][] masque = {{1 , 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}};
		int sumPoidsMasque = 25;
		int tailleMasque = 5;
		int commencement = tailleMasque/2 + 1; // prendre la division entière supérieure
		for (int y = commencement; y < h - commencement;  y++)
		{
			for ( int x = commencement; x < w - commencement ; x++) {

				int sum = 0;

				for(int i = -1; i < tailleMasque-1; i++)
				{
					for(int j = -1; j < tailleMasque-1; j++)
					{
						 sum += ip.get((x+i), (y+j)) * masque[i+1][j+1];
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