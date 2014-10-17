package com.rocui.docs.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextSheet {
	private List<HashMap<String,String>> row = new ArrayList<HashMap<String,String>>();

	public TextSheet addRow(HashMap<String,String> row) { 
		this.row.add(row);
		return this;
	}
	
	public TextSheet addRow(String row){
		HashMap<String,String> map=new HashMap<String,String>();
		map.put(Long.toString(System.currentTimeMillis()), row);
		this.row.add(map);
		return this;
	}
	
	public TextSheet addRow(List<HashMap<String,String>> rows){
		for(HashMap<String,String> map:rows){
			this.row.add(map);
		}
		return this;
	}

	public List<HashMap<String,String>> getRows(){
		return this.row;
	}
}