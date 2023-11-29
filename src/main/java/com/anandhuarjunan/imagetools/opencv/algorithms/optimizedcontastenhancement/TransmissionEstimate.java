package com.anandhuarjunan.imagetools.opencv.algorithms.optimizedcontastenhancement;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class TransmissionEstimate {

	public static Mat transEstimate(Mat img, int patchSz, double[] airlight, double lambda, double fTrans) {
		int rows = img.rows();
		int cols = img.cols();
		List<Mat> bgr = new ArrayList<Mat>();
		Core.split(img, bgr);
		int type = bgr.get(0).type();
		// calculate the transmission map
		Mat T = new Mat(rows, cols, type);
		for (int i = 0; i < rows; i += patchSz) {
			for (int j = 0; j < cols; j += patchSz) {
				int endRow = i + patchSz > rows ? rows : i + patchSz;
				int endCol = j + patchSz > cols ? cols : j + patchSz;
				Mat blkIm = img.submat(i, endRow, j, endCol);
				double Trans = BlkTransEstimate.blkEstimate(blkIm, airlight, lambda, fTrans);
				for (int m = i; m < endRow; m++) {
					for (int n = j; n < endCol; n++) {
						T.put(m, n, Trans);
					}
				}
			}
		}
		return T;
	}

	public static Mat transEstimateEachChannel(Mat img, int patchSz, double airlight, double lambda, double fTrans) {
		int rows = img.rows();
		int cols = img.cols();
		Mat T = new Mat(rows, cols, img.type());
		for (int i = 0; i < rows; i += patchSz) {
			for (int j = 0; j < cols; j += patchSz) {
				int endRow = i + patchSz > rows ? rows : i + patchSz;
				int endCol = j + patchSz > cols ? cols : j + patchSz;
				Mat blkIm = img.submat(i, endRow, j, endCol);
				double Trans = BlkTransEstimate.blkEstimateEachChannel(blkIm, airlight, lambda, fTrans);
				for (int m = i; m < endRow; m++) {
					for (int n = j; n < endCol; n++) {
						T.put(m, n, Trans);
					}
				}
			}
		}
		return T;
	}

}
