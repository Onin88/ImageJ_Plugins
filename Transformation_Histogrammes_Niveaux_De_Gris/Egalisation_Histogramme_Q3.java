// Importation des paquets n�cessaires. Le plugin n'est pas lui-m�me un paquet (pas de mot-cl� package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;						// pour classe GenericDialog et Newimage

public class Egalisation_Histogramme_Q3 implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {

		return DOES_8G;
	}

	public void run( ImageProcessor ip){
		int w = ip.getWidth();
		int h = ip.getHeight();
		
		ImageProcessor ipOutput = ip.createProcessor(w, h);

		int nbPixels = w*h;

		IJ.log("Nombre de pixels : "+nbPixels);
		
		int [] H = new int[256]; //nombre de pixels de l'image I de taille L*H qui ont la valeur n (0 <= n <= 255)

		int i, x, y;

		//Remplissage de H, on parcourt l'image et on incrémente la valeur de H correspondant à la valeur du pixel
		for (y = 0; y < h;  y++)
		{
			for (x = 0; x < w; x++) {
				if( ip.get(x, y) < 0 || ip.get(x, y) > 255){
					IJ.log("Erreur : pixel non compris entre 0 et 255");
					return;
				}
				else{
					H[ip.get(x, y)]++;
				}
			}
		}

		//Affichage des valeurs de H
		for(i = 0; i < 256; i++)
		{
			IJ.log("H["+i+"] = "+H[i]);
		}

		//Histogramme normalisé P
		float [] P = new float[256];
		//P(n) = H(n)/nbPixels c'est à dire le nombre de pixels de l'image I de taille L*H qui ont la valeur n divisé par le nombre total de pixels
		for (i = 0; i < 256; i++)
		{
			if(H[i] != 0){
				P[i] = (float) H[i]/nbPixels;
			}else{
				P[i] = 0;
			}
		}

		//Affichage des valeurs de P
		for(i = 0; i < 256; i++)
		{
			IJ.log("P["+i+"] = "+P[i]);
		}


		//Histogramme cumulé normalisé R*
		float [] R = new float[256];

		//R(n) = somme de P(i) pour k allant de 0 à n
		R[0] = P[0]; // la première valeur de R est égale à la première valeur de P
		// Ensuite on récupère la somme d'avant et on ajoute la valeur de P actuelle
		for(i = 1; i < 256; i++)
		{
			R[i] = R[i-1] + P[i];
		}

		// Afficher les valeurs de R
		for(i = 0; i < 256; i++)
		{
			IJ.log("R["+i+"] = "+R[i]);
		}

		//Egalisation de l'histogramme avec pour Transformation sur I : I'(x, y) = (256-1)*R(I(x, y))
		for (y = 0; y < h;  y++)
		{
			for (x = 0; x < w; x++) {
				ipOutput.set(x, y, (int)((256-1)*R[ip.get(x, y)]));
			}
		}

		ImagePlus result = new ImagePlus("Result", ipOutput);

		result.show();
		result.updateAndDraw();
	}
}