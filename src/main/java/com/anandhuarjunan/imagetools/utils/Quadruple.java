package com.anandhuarjunan.imagetools.utils;

public class Quadruple<A,B,C,D> {

	private  A first = null;
	private  B second= null;
	private  C third= null;
	private  D fourth= null;
	
	public Quadruple(A first, B second, C third, D fourth) {
		super();
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
	}

	public A getFirst() {
		return first;
	}

	public void setFirst(A first) {
		this.first = first;
	}

	public B getSecond() {
		return second;
	}

	public void setSecond(B second) {
		this.second = second;
	}

	public C getThird() {
		return third;
	}

	public void setThird(C third) {
		this.third = third;
	}

	public D getFourth() {
		return fourth;
	}

	public void setFourth(D fourth) {
		this.fourth = fourth;
	}
	

	
	
	
}
