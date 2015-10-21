package net.g2lab.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.io.DefaultFileFilter;

public class TIFFtoPNG {
	
	private static final Log LOG = LogFactory.getLog(Class.class.getName());
	
	public TIFFtoPNG() throws FileNotFoundException, IOException {
	}


	/** Converts all TIFF files from the given path to PNGs written to out path.
	 * @param path 
	 * @param outPath 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void convert(String path, String outPath) throws IOException, FileNotFoundException {
		String[] tiffs = new File(path).list(new DefaultFileFilter(".tif") {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".tif");
			}
		});
		LOG.debug(tiffs.length + " TIFF images to convert.");
		int n = 0;
		for (String tif : tiffs) {
			String filename = tif.split("\\.")[0];
			final BufferedImage image = ImageIO.read(new File(path + tif));
			String outFile = outPath +  filename + ".png";
			LOG.debug("...writing " + n++ + ": " + outFile);
			ImageIO.write(image, "png", new FileOutputStream(outFile));
		}
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String path = "C:/temp/icc/final/images/";
			String outPath = "C:/temp/icc/final/images-png/";
			new TIFFtoPNG().convert(path, outPath);
		} catch(Exception exc) {
			LOG.error(exc, exc);
		}

	}

}
