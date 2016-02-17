package server;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Vision {
	
	static double[] RGBUpper = {255, 255, 255}; //In BGR Order
	static double[] RGBLower = {156, 137, 138}; //In BGR Order
	
	public Vision() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public double[] getBoulderCoord(BufferedImage img) {
		byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		Mat image_final= new Mat(img.getWidth(), img.getHeight(), CvType.CV_8UC3);
		image_final.put(0, 0, pixels);
		Mat rgbFiltered = RGBThresh(img, new Scalar(RGBLower), new Scalar(RGBUpper));
		Mat eroded = new Mat();
		Imgproc.erode(rgbFiltered, eroded, new Mat(), new Point(-1, -1), 2);
		ArrayList<MatOfPoint> contours = findContours(eroded);
		MatOfPoint biggestContour = findBiggestContour(contours);
		Rect boundingBox = Imgproc.boundingRect(biggestContour);
		return new double[] {getCenterX(boundingBox), getCenterY(boundingBox)};
	}
	
	private ArrayList<MatOfPoint> findContours(Mat src) {
		Mat tmp = new Mat();
		src.copyTo(tmp);
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(tmp, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
		return contours;
	}
	
	private MatOfPoint findBiggestContour(ArrayList<MatOfPoint> contours) {
		int maxAreaIndex = 0;
		double maxArea = 0;
		for (int i = 0; i < contours.size(); i++) {
			double area = Imgproc.contourArea(contours.get(i));
			if (area > maxArea) {
				maxArea = area;
				maxAreaIndex = i;
			}
		}
		return contours.get(maxAreaIndex);
	}
	
	private int getCenterY(Rect r) {
		return r.y + (r.height / 2);
	}
	
	private int getCenterX(Rect r) {
		return r.x + (r.width / 2);
	}
	
	private Mat RGBThresh(Mat src, Scalar lowerb, Scalar upperb) {
		Mat dst = new Mat();
		Core.inRange(src, lowerb, upperb, dst);
		return dst;
	}

}
