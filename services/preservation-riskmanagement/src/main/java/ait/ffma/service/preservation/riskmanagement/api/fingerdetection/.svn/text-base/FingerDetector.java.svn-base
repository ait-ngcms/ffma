package ait.ffma.service.preservation.riskmanagement.api.fingerdetection;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.pixel.Pixel;
import org.openimaj.image.processing.edges.CannyEdgeDetector2;
import org.openimaj.image.processing.resize.ResizeProcessor;
import org.openimaj.math.geometry.shape.Rectangle;


/**
 * OpenIMAJ uses Canny algorithm for finger detection on scans.
 *
 */
public class FingerDetector {
	
	public static int DEVIATION_X = 10;
	public static int MIN_FINGER_WIDTH = 32;
	public static float AVG_WIDTH_RATE = 1.5f;
	public static int TOLERANCE = 8; 
    public static int MIN_FINGER_POINTS = 30;
    public static int MAX_FINGER_SIZE = 50;
    public static int MIN_BORDER_DISTANCE = 150;
	private static List<Finger> fingerList = new ArrayList<Finger>();
	
	/**
	 * This list contains detected finger candidates of one scan.
	 */
	private static List<Finger> resFingerList = new ArrayList<Finger>();
	
    public static void main( String[] args ) {
    	// compare collections
    	try {
    		System.out.println("*********************************************");
    		System.out.println("            FIND FINGERS ON SCANS            ");
    		System.out.println("*********************************************\n");
    		long initTime = System.currentTimeMillis(); 
    		//String pathLeft = "samplesV2";     // 15 samples collection
    		//String pathLeft = "fingers2Db"; // 26 additional samples collection
    		//String pathLeft = "fingersDbOrigin"; // 160 samples collection
    		String pathLeft = "Z151694702jpg730"; // finger free test collection
    		File[] listOfFilesLeft = listFilesInDirectory(pathLeft);
    		
    		int fingerImageCount = 0;
			for( int i = 0; i < listOfFilesLeft.length; i++ ) {
				System.out.println("query collection image i: " + i);
				if (i < listOfFilesLeft.length) {
		    	    String fileNameLeft = pathLeft + "\\" + listOfFilesLeft[i].getName();

		    	    MBFImage query = ImageUtilities.readMBF(new File(fileNameLeft));
		    	    ResizeProcessor resize = new ResizeProcessor(400, 200);
		    		query.processInline(resize);
		    		MBFImage clone = query.clone ();
    				query.processInline(new CannyEdgeDetector2());
    				
    				FImage res = query.flatten();
    				// Extract gray scale image with edge detection
//    				DisplayUtilities.display(res);

    				analyse(res);
    				if (resFingerList.size() > 0) {
    					System.out.println(resFingerList.size() + " finger in current scan detected!\n");
    					fingerImageCount++;
					    Iterator<Finger> itr = resFingerList.iterator();
					    while (itr.hasNext()) {
						    Finger finger = itr.next();
						    finger.evalBounds(res);
//	    					System.out.println("minX: " + finger.getMinX() + ", minY: " + finger.getMinY() +
//	    							", maxX: " + finger.getMaxX() + ", maxY: " + finger.getMaxY());
	    					int offsetX = 0;
	    					int offsetY = 0;
	    					if (finger.getMinX() == finger.getMaxX()) {
	    						offsetX = 10;
	    					}
	    					if (finger.getMinY() == finger.getMaxY()) {
	    						offsetY = 10;
	    					}
	    					// draw green rectangle around the detected finger area
	    					clone.drawShape(new Rectangle (finger.getMinX(), finger.getMinY(), 
	    							offsetX + finger.getMaxX() - finger.getMinX(), 
	    							offsetY + finger.getMaxY() - finger.getMinY()), 4, RGBColour.GREEN);
//	    					clone.drawShape(new Rectangle (finger.getMinX(), finger.getMinY(), 
//	    							offsetX + finger.getMaxX() - finger.getMinX(), 
//	    							offsetY + finger.getMaxY() - finger.getMinY()), RGBColour.GREEN);
					   } // end while
					   // display image with green rectangles
	    			   DisplayUtilities.display(res); //tmp
        			   DisplayUtilities.display(clone);
    				} // end if finger list size
    				resFingerList.clear();
    				fingerList.clear();
    				// display resulting edges after analysis
//    				DisplayUtilities.display(res);
				} // offset if i
			} // for i  	
			
    		long endTime = System.currentTimeMillis(); 
    		long resTime = endTime - initTime;
    		System.out.println("calculation time: " + resTime);	
 		    System.out.println("finger list size: " + fingerImageCount + 
 		    		" of total " + listOfFilesLeft.length + " files\n");
    		System.out.println("**********************************************\n\n");
    	} catch (Exception e) {
			System.out.println("error: " + e);
		}

    }
    
	/**
	 * Constructor 
	 */
	public FingerDetector() {}
	
    /**
     * @param img - The document path e.g. "http://dl.dropbox.com/u/8705593/sinaface.jpg"
     * @return
     */
    public MBFImage analyse(String img) {
    	String resMsg = "No fingers detected.";
    	MBFImage resImg = null;
    	// compare collections
    	try {
   			MBFImage query = ImageUtilities.readMBF(new URL(img));
    		System.out.println("*********************************************");
    		System.out.println("            FIND FINGERS ON SCANS            ");
    		System.out.println("*********************************************\n");
    		long initTime = System.currentTimeMillis(); 
    	    ResizeProcessor resize = new ResizeProcessor(400, 200);
    		query.processInline(resize);
    		MBFImage clone = query.clone ();
   			resImg = clone;
			query.processInline(new CannyEdgeDetector2());
			
			FImage res = query.flatten();
			// Extract gray scale image with edge detection
//    				DisplayUtilities.display(res);

			analyse(res);
			if (resFingerList.size() > 0) {
				resMsg = resFingerList.size() + " finger in current scan detected!\n";
				System.out.println(resMsg);
			    Iterator<Finger> itr = resFingerList.iterator();
			    while (itr.hasNext()) {
				    Finger finger = itr.next();
				    finger.evalBounds(res);
//	    					System.out.println("minX: " + finger.getMinX() + ", minY: " + finger.getMinY() +
//	    							", maxX: " + finger.getMaxX() + ", maxY: " + finger.getMaxY());
					int offsetX = 0;
					int offsetY = 0;
					if (finger.getMinX() == finger.getMaxX()) {
						offsetX = 10;
					}
					if (finger.getMinY() == finger.getMaxY()) {
						offsetY = 10;
					}
					// draw green rectangle around the detected finger area
					clone.drawShape(new Rectangle (finger.getMinX(), finger.getMinY(), 
							offsetX + finger.getMaxX() - finger.getMinX(), 
							offsetY + finger.getMaxY() - finger.getMinY()), 4, RGBColour.GREEN);
			   } // end while
			   // display image with green rectangles
			   resImg = clone;
//			   DisplayUtilities.display(res); 
//			   DisplayUtilities.display(clone);
			} // end if finger list size
			resFingerList.clear();
			fingerList.clear();
			// display resulting edges after analysis
//    				DisplayUtilities.display(res);
			
    		long endTime = System.currentTimeMillis(); 
    		long resTime = endTime - initTime;
    		System.out.println("calculation time: " + resTime);	
    		System.out.println("**********************************************\n\n");
		} catch (MalformedURLException e) {
			System.out.println("document path error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("document path error: " + e.getMessage());
    	} catch (Exception e) {
			System.out.println("error: " + e);
		}
        return resImg;
    }
     
    /**
     * This method evaluates the next existing pixel on the right from the given 
     * position on the X axis.
     * @param image
     * @param xx
     * @param yy
     * @return the x coordinate of the next pixel
     */
    public static int evaluateNextRightPos (FImage image, int xx, int yy) {
		int res = 0;
    	for (int x=xx; x < image.getWidth (); x++) { // column
		   float pixel = image.getPixel(x, yy);
		   if (pixel > 0.0) {
			   res = x;
			   break;
		   }
    	}
    	return res;
    }

    /**
     * This method evaluates whether finger candidate matches to the given tolerance.
     * @param x
     * @param y
     * @return the finger object if that matches to the given tolerance
     */
    public static Finger isFingerExist(int x, int y) {
       Finger res = null;
	   Iterator<Finger> itr = fingerList.iterator();
	   while (itr.hasNext()) {
		   Finger finger = itr.next();
//		   System.out.println("finger list size: " + fingerList.size() + ", x: " + x + ", y: " + y + ", lastX: " + finger.getLastX() + ", lastY: " + finger.getLastY());
		   if (((finger.getLastX() >= x && finger.getLastX() - x < TOLERANCE) ||
				   (finger.getLastX() <= x && x - finger.getLastX() < TOLERANCE)) &&
			   ((finger.getLastY() >= y && finger.getLastY() - y < TOLERANCE) ||
				   (finger.getLastY() <= y && y - finger.getLastY() < TOLERANCE))) {
			   res = finger;
			   break;
		   }
	   }
	   return res;
	}
    
    /**
     * This method checks whether evaluated average finger width is inside of
     * the given interval.
     * @param res
     * @param finger
     */
    public static void checkFingerWidth(FImage res, Finger finger) {
    	int avgFingerWidth = finger.getAvgWidth();
		if (finger.getPlotMap().size() > 0) {
			for (Map.Entry<Pixel, Pixel> entry : finger.getPlotMap().entrySet()) {
			    Pixel key = entry.getKey();
			    Pixel value = entry.getValue();
			    if (avgFingerWidth*AVG_WIDTH_RATE < value.x - key.x) {
			    	res.setPixel(key.x, key.y, 0.0f);			    	
			    	res.setPixel(value.x, value.y, 0.0f);			    	
			    }
			}
		}    	
    }
    
    /**
     * This method checks whether finger candidate crosses a scan text border.
     * @param res
     * @param finger
     */
    public static void checkFingerOnBorder(FImage res, Finger finger) {
    	if (!(finger.getxId() < MIN_BORDER_DISTANCE || finger.getLastX() > res.getWidth() - MIN_BORDER_DISTANCE ||
    			finger.getyId() < MIN_BORDER_DISTANCE  || finger.getLastY() > res.getHeight() - MIN_BORDER_DISTANCE)) {
			if (finger.getPlotMap().size() > 0) {
				for (Map.Entry<Pixel, Pixel> entry : finger.getPlotMap().entrySet()) {
				    Pixel key = entry.getKey();
				    Pixel value = entry.getValue();
			    	res.setPixel(key.x, key.y, 0.0f);			    	
			    	res.setPixel(value.x, value.y, 0.0f);			    	
				}
			}
    	}
    }
    
    /**
     * This method adds a detected finger candidate to the finger candidate
     * list of the current scan if this candidate matches the requirements.
     * @param res
     */
    public static void flagFingers(FImage res) {
	   Iterator<Finger> itr = fingerList.iterator();
	   int fingersCount = 0;
	   while (itr.hasNext()) {
		   Finger finger = itr.next();
		   int plotCount = finger.getPlotMap().size();
	       if (plotCount >= MIN_FINGER_POINTS && finger.getFingerHeight() <= res.getHeight()*MAX_FINGER_SIZE && finger.getFingerWidthMax() <= res.getWidth()*MAX_FINGER_SIZE) {
//			   System.out.println("fingersCount: " + fingersCount + ", Finger x: " + finger.getxId() + 
//					   ", y: " + finger.getyId() + ". Plot count: " + plotCount);
			   fingersCount++;
			   if (finger.getImageHight()*MAX_FINGER_SIZE/100 > finger.getFingerHeight() &&
						   finger.getImageWidth()*MAX_FINGER_SIZE/100 > finger.getFingerWidthMax() &&
						   finger.getFingerWidthMax() > DEVIATION_X) {
				   checkFingerWidth(res, finger);
				   checkFingerOnBorder(res, finger);
				   resFingerList.add(finger);
			   }
		   }
	   }
    }

    /**
     * This method analyze a given scan image in order to detect finger candidates.
     * @param res The scan image to analyze 
     */
    public static void analyse(FImage res) {
    	// filter pixels
		for (int y=0; y < res.getHeight (); y++) { // row
			for (int x=0; x < res.getWidth (); x++) { // column
			   float pixel = res.getPixel(x, y);
			   if (pixel > 0.0) {
				   int nextX = evaluateNextRightPos(res, x + 1, y);
				   int diff = nextX - x;
				   if (diff >= MIN_FINGER_WIDTH) {
					   Finger f = null;
					   f = isFingerExist(x, y);
					   if (f == null) {
						   // create possible finger
						   f = new Finger();
						   f.setxId(x);
						   f.setyId(y);
						   fingerList.add(f);
					   }
					   f.getPlotMap().put(new Pixel(x, y), new Pixel(nextX, y));
					   f.setLastX(x);
					   f.setLastY(y);
					   f.setImageHight(res.getHeight());
					   f.setImageWidth(res.getWidth());
				   }
//				   System.out.println("x: " + x + ", y: " + y + ", pixel value: " + pixel + "\n");
			   }
			}
		}
//		System.out.println("finger list size: " + fingerList.size());
		flagFingers(res);
    }
    
    /**
     * This method lists all collection images.
     * @param path to the collection
     * @return
     */
    public static File[] listFilesInDirectory(String path) 
    {    
      String files;
      File folder = new File(path);
      File[] listOfFiles = folder.listFiles(); 
     
	  System.out.println("********* Collection *****************\n");
      for (int i = 0; i < listOfFiles.length; i++) 
      {     
          if (listOfFiles[i].isFile()) 
          {
    	     files = listOfFiles[i].getName();
    	     System.out.println(files);
          }
      }
	  System.out.println("\n");
      
      return listOfFiles;
    }
}
