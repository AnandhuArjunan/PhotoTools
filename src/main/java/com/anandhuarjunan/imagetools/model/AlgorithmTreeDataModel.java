package com.anandhuarjunan.imagetools.model;

public class AlgorithmTreeDataModel {

	private String algorithmName;
	private String algorithmId;
	private String algorithmGroupId;
	private String algorithmCodePath;
	private String algorithmCodeComplex;
	public String getAlgorithmName() {
		return algorithmName;
	}
	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}
	public String getAlgorithmId() {
		return algorithmId;
	}
	public void setAlgorithmId(String algorithmId) {
		this.algorithmId = algorithmId;
	}
	public String getAlgorithmCodePath() {
		return algorithmCodePath;
	}
	public void setAlgorithmCodePath(String algorithmCodePath) {
		this.algorithmCodePath = algorithmCodePath;
	}
	public String getAlgorithmCodeComplex() {
		return algorithmCodeComplex;
	}
	public void setAlgorithmCodeComplex(String algorithmCodeComplex) {
		this.algorithmCodeComplex = algorithmCodeComplex;
	}
	
	public String getAlgorithmGroupId() {
		return algorithmGroupId;
	}
	public void setAlgorithmGroupId(String algorithmGroupId) {
		this.algorithmGroupId = algorithmGroupId;
	}
	@Override
	public String toString() {
		return algorithmName;
	}
	
	
	
}
