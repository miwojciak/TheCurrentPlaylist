package com.mwo.spotify.db;

import java.util.ArrayList;

public class DB {
	ArrayList<String> list;
	
	public DB(){
		list = new ArrayList<String>();		
	}
	
	public void add(String artist, String title){
		list.add(artist + ":" + title);
	}
	
	public boolean exist(String artist, String title){
		return list.contains(artist + ":" + title);
	}
	
}
