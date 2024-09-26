import ij .*;
import ij. process .*;
import ij.gui .*;
import java .awt .*;
import ij. plugin . frame .*;

public class Diff_Im_Q6 extends PlugInFrame {
	public Diff_Im_Q6(){
		super ( "Soustraction entre images ");
	}

	public void run ( String arg) {
		String pluginDirectory = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		String imageAPath = pluginDirectory + "imA.pgm";
		String imageBPath = pluginDirectory + "imB.pgm";

		ImagePlus imgDiffA = new ImagePlus(imageAPath);
		ImageProcessor ipDiffA = imgDiffA.getProcessor ();
	
		ImagePlus imgDiffB = new ImagePlus (imageBPath);
		ImageProcessor ipDiffB = imgDiffB.getProcessor ();


		int w = ipDiffB.getWidth ();
		int h = ipDiffB.getHeight ();

		ImageProcessor ipRes = ipDiffB.createProcessor(w, h);
		
		// Soustraction des deux images
		for(int y = 0; y < h; y++)
		{
			for(int x = 0; x < w; x++)
			{
				int pixelA = ipDiffA.getPixel(x, y);
				int pixelB = ipDiffB.getPixel(x, y);

				ipRes.set(x, y, Math.max(pixelA - pixelB, 0));
			}
		}
		
		ImagePlus imgRes = new ImagePlus("Resultat", ipRes);

		imgRes.show ();
		imgRes.updateAndDraw ();
	}	
}