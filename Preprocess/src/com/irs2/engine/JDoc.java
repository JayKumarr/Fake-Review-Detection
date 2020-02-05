package com.irs2.engine;

public class JDoc {

	private String name;
	private int length =0;
	
	public JDoc(){}
	
	public JDoc(String name , int length){
		this.name = name;
		this.length = length;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	
	@Override
	public String toString() {
		return "["+this.name+"]->{"+length+"}";
	}
	
	public static JDoc getInstance(String str){
		JDoc doc = null;
		String[] strs = str.split("->");
		if(strs.length==2){
			doc = new JDoc();
			doc.setName(strs[0].replace("[", "").replace("]", ""));
			doc.setLength(Integer.parseInt(strs[1].replace("{", "").replace("}", "")));
		}
		return doc;
	}
}
