package com.mwo.spotify.source;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ListIterator;

import javax.annotation.PostConstruct;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import com.mwo.spotify.api.service.PlaylistService;
import com.mwo.spotify.rest.SpotifyServlet;
import com.wrapper.spotify.exceptions.WebApiException;


public class TheCurrent {
	
	@Autowired
	PlaylistService playlistService;
	
	@PostConstruct
	public void init() throws Exception {
	}
	
	public void getSongs() {
		System.out.println("task");
		Document doc;
		try {
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd"); 
			Date date = new Date();		
			
			//doc = Jsoup.connect("http://www.thecurrent.org/playlist/" + dt.format(date) + "?isajax=1").get();
			doc = Jsoup.connect("http://www.thecurrent.org/playlist/?isajax=1").get();
		} catch (IOException e) {
			System.out.println("Error during downloading playlist");
			e.printStackTrace();
			return;
		}
		
		Elements songs = doc.select(".song");
		
		ListIterator<Element> i = songs.listIterator();
		while(i.hasNext()){
			Element song = i.next();
			Element artistElement = song.select(".artist").first();
			Element titleElement = song.select(".title").first();
			
			String artist = artistElement != null ? artistElement.text() : null;
			String title = titleElement != null ? titleElement.text() : null;
			
			playlistService.add(artist, title);
		}
			
	}

}
