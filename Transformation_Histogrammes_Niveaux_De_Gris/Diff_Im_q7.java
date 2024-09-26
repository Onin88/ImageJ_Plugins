import ij .*;
import ij. process .*;
import ij.gui .*;
import java .awt .*;
import ij. plugin . frame .*;

public class Diff_Im_q7 extends PlugInFrame {
	public Diff_Im_q7 (){
		super ( "Soustraction entre images ");
	}

	public void run ( String arg) {
		String pluginDirectory = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		String imageAPath = pluginDirectory + "diff1.jpg";
		String imageBPath = pluginDirectory + "diff2.jpg";

		ImagePlus imgDiffA = new ImagePlus(imageAPath);
		ImageProcessor ipDiffA = imgDiffA . getProcessor ();
	
		ImagePlus imgDiffB = new ImagePlus (imageBPath);
		ImageProcessor ipDiffB = imgDiffB . getProcessor ();

		int w = ipDiffB.getWidth ();
		int h = ipDiffB.getHeight ();

		ColorProcessor ipRes = new ColorProcessor(w, h);
		
		// Soustraction des deux images
		for(int y = 0; y < h; y++)
		{
			for(int x = 0; x < w; x++)
			{
				int pixelA = ipDiffA.getPixel(x, y);
                int pixelB = ipDiffB.getPixel(x, y);

                if(pixelA > pixelB){
					//on met le pixel de ipRes en bleu
					ipRes.set(x, y, new Color(0, 0, pixelA - pixelB).getRGB());
				}else if(pixelA < pixelB){
					//on met le pixel de ipRes en rouge
					ipRes.set(x, y, new Color(pixelB - pixelA, 0, 0).getRGB());
				}else {
					// Si le pixelA de base est blanc et le pixelB de base est noir, on met le pixel de ipRes en noir
					// Sinon, on met le pixel de ipRes en blanc
					if(pixelA == 255){
						ipRes.set(x, y, new Color(0, 0, 0).getRGB());
					}else{
						ipRes.set(x, y, new Color(255, 255, 255).getRGB());
					}
				}
			}
		}
		
		ImagePlus imgRes = new ImagePlus("Resultat", ipRes);

		imgRes.show ();
		imgRes.updateAndDraw ();
	}	
}