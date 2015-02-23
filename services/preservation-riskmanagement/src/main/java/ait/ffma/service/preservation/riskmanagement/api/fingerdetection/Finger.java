package ait.ffma.service.preservation.riskmanagement.api.fingerdetection;

import java.util.HashMap;
import java.util.Map;

import org.openimaj.image.FImage;
import org.openimaj.image.pixel.Pixel;

public class Finger {

	Map<Pixel, Pixel> plotMap = new HashMap<Pixel, Pixel>();
	public int getLastX() {
		return lastX;
	}
	public void setLastX(int lastX) {
		if (this.lastX > this.xId) {
			this.fingerWidthMax = this.lastX - this.xId;
		} else {
			this.fingerWidthMax = this.xId - this.lastX;
		}
		this.lastX = lastX;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getLastY() {
		return lastY;
	}
	public void setLastY(int lastY) {
		if (this.lastY > this.yId) {
			this.fingerHeight = this.lastY - this.yId;
		} else {
			this.fingerHeight = this.yId - this.lastY;
		}
		this.lastY = lastY;
	}
	int size = 0;
	int imageHeight = 0;
	int imageWidth = 0;
	int avgWidth = 0;
	int fingerHeight = 0;
	int fingerWidthMax = 0;
	int fingerWidthMin = 0;
	int xId = 0;
	int yId = 0;
	int lastX = 0;
	int lastY = 0;
	int minX = 0;
	int minY = 0;
	int maxX = 0;
	int maxY = 0;
	
	public int getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}
	public int getMinX() {
		return minX;
	}
	public void setMinX(int minX) {
		this.minX = minX;
	}
	public int getMinY() {
		return minY;
	}
	public void setMinY(int minY) {
		this.minY = minY;
	}
	public int getMaxX() {
		return maxX;
	}
	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}
	public int getMaxY() {
		return maxY;
	}
	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}
	public int getxId() {
		return xId;
	}
	public void setxId(int xId) {
		this.xId = xId;
		this.minX = xId;
		this.maxX = xId;
	}
	public int getyId() {
		return yId;
	}
	public void setyId(int yId) {
		this.yId = yId;
		this.minY = yId;
		this.maxY = yId;
	}
	public Map<Pixel, Pixel> getPlotMap() {
		return plotMap;
	}
	public void setPlotMap(Map<Pixel, Pixel> plotMap) {
		this.plotMap = plotMap;
	}
	public int getImageHight() {
		return imageHeight;
	}
	public void setImageHight(int imageHight) {
		this.imageHeight = imageHight;
	}
	public int getImageWidth() {
		return imageWidth;
	}
	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}
	public int getAvgWidth() {
		int sum = 0;
		if (getPlotMap().size() > 0) {
			for (Map.Entry<Pixel, Pixel> entry : getPlotMap().entrySet()) {
			    Pixel key = entry.getKey();
			    Pixel value = entry.getValue();
			    sum = sum + (value.x - key.x);
			}
			this.avgWidth = sum/getPlotMap().size();
		}
		return avgWidth;
	}
	public void setAvgWidth(int avgWidth) {
		this.avgWidth = avgWidth;
	}
	public int getFingerHeight() {
		return fingerHeight;
	}
	public void setFingerHeight(int fingerHeight) {
		this.fingerHeight = fingerHeight;
	}
	public int getFingerWidthMax() {
		return fingerWidthMax;
	}
	public void setFingerWidthMax(int fingerWidthMax) {
		this.fingerWidthMax = fingerWidthMax;
	}
	public int getFingerWidthMin() {
		return fingerWidthMin;
	}
	public void setFingerWidthMin(int fingerWidthMin) {
		this.fingerWidthMin = fingerWidthMin;
	}
	public void evalBounds(FImage image) {
		if (getPlotMap().size() > 0) {
			for (Map.Entry<Pixel, Pixel> entry : getPlotMap().entrySet()) {
			    try {
					Pixel key = entry.getKey();
				    Pixel value = entry.getValue();
				    float pixelKey = image.getPixel(key.x, key.y);
				    float pixelValue = image.getPixel(value.x, value.y);
				    if (pixelKey > 0.0 && pixelValue > 0.0) {
						if (key.x < this.minX) this.minX = key.x;
						if (value.x > this.maxX) this.maxX = value.x;
						if (key.y < this.minY) this.minY = key.y;
						if (value.y > this.maxY) this.maxY = value.y;
				    }
			    } catch (Exception e) {
			    	System.out.println("Error in evalBounds(): " + e);
			    }
			}
		}
	}

}
