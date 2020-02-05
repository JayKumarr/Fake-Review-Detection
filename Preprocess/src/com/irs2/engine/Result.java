package com.irs2.engine;

import java.util.List;

public class Result {
	
	List<String> listDoc;
	List<Double> listWeight;
	
	
	
	public Result(List<String> listDoc, List<Double> listWeight) {
		super();
		this.listDoc = listDoc;
		this.listWeight = listWeight;
	}
	public List<String> getListDoc() {
		return listDoc;
	}
	public void setListDoc(List<String> listDoc) {
		this.listDoc = listDoc;
	}
	public List<Double> getListWeight() {
		return listWeight;
	}
	public void setListWeight(List<Double> listWeight) {
		this.listWeight = listWeight;
	}
	
	
	
}
