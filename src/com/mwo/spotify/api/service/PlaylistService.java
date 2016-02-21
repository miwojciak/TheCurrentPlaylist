package com.mwo.spotify.api.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mwo.spotify.db.DB;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.AddTrackToPlaylistRequest;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.Track;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

public class PlaylistService {
	DB db;

	@Autowired
	AuthService authService;

	@Value( "${playlistId}" )
	String playlistId;

	public PlaylistService() {
		db = new DB();
	}

	@PostConstruct
	public void init() {
		System.out.format("PlaylistId: %s \n", playlistId);
	}

	public void add(String artist, String title) {
		if (!db.exist(artist, title)) {
			try {
				System.out.println("artist:\"" + artist + "\" title:\"" + title
						+ "\"");
				
				final TrackSearchRequest searchRequest = authService
						.getApi()
						.searchTracks(
								"artist:\"" + artist + "\" title:\"" + title + "\"")
						.build();

				final Page<Track> trackSearchResult = searchRequest.get();

				System.out.println("I got " + trackSearchResult.getTotal()
						+ " results!");

				if (trackSearchResult.getTotal() > 0) {
					List<String> tracksToAdd = Arrays.asList(trackSearchResult
							.getItems().get(0).getUri());

					final AddTrackToPlaylistRequest addTracksRequest = authService.getApi()
							.addTracksToPlaylist(authService.user.getId(),
									playlistId, tracksToAdd).build();

					addTracksRequest.get();
				}

				db.add(artist, title);
			} catch (IOException | WebApiException e) {
				e.printStackTrace();
			}
		}
	}
}
