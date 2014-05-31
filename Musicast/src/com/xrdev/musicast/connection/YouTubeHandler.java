package com.xrdev.musicast.connection;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.text.TextUtils;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.xrdev.musicast.model.VideoItem;


public class YouTubeHandler {
	private static final long NUMBER_OF_VIDEOS_RETURNED = 5;
	
	/** Global instance of the HTTP transport. */
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	/** Global instance of Youtube object to make all API requests. */
	private static YouTube youtube;

	
	
	public static ArrayList<VideoItem> searchVideos(String searchTerm) {
		ArrayList<VideoItem> videoItemList = new ArrayList<VideoItem>();
		
		try {
			// Construir o objeto que ser� a refer�ncia para todas as solicita��es � API.
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
		          public void initialize(HttpRequest request) throws IOException {}
		        }).setApplicationName("musicast").build();
			
			System.out.println("[YoutubeHandler]: Conex�o constru�da.");
			YouTube.Search.List search = youtube.search().list("id,snippet");
			
			String apiKey = "AIzaSyBTXTaiH-BYye70pKDGc_Bpf1dkqiPDLcw";
			
			search.setKey(apiKey);
			
			search.setQ(searchTerm);
			/*
			 * We are only searching for videos (not playlists or channels). If we were searching for
			 * more, we would add them as a string like this: "video,playlist,channel".
			 */
			search.setType("video");

			/*
			 * setFields: reduz as informa��es retornadas para apenas os campos necess�rios.
			 */
			// search.setFields("items(id/kind,id/videoId,snippet/title,snippet/description)"); --> Sem viewcount.
			
			search.setFields("items(id/kind,id/videoId)"); // Pega apenas o ID. Pegar o restante do objeto Video que ser� encontrado.
			
			// Campos poss�veis: https://developers.google.com/youtube/v3/docs/search
			search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
			
			System.out.println("[YouTubeHandler]: Iniciando busca.");
			
			SearchListResponse searchResponse = search.execute();
			
			List<SearchResult> searchResultList = searchResponse.getItems();
			
			List<String> videoIds = new ArrayList<String>();
			
			/**
			 * Pegar apenas os IDs dos v�deos encontrados.
			 */
			if (searchResultList != null) {
				 Iterator resultsIterator = searchResultList.iterator();
				 
				 while (resultsIterator.hasNext()) {
					 SearchResult searchResult = (SearchResult) resultsIterator.next();
					 ResourceId rId = searchResult.getId(); // Pegar o ID do resultado.
					 
					 // Verifica se o resultado � um video. Adiciona a lista de IDs.
					 if (rId.getKind().equals("youtube#video")) {
				    	 
						 videoIds.add(rId.getVideoId());
						 
						 System.out.println("[YouTubeHandler]: Video ID encontrado adicionado: " + rId.getVideoId());
						 /**
						 String videoId = rId.getVideoId();
						 String title = searchResult.getSnippet().getTitle();
						 String description = searchResult.getSnippet().getDescription();
						 VideoListResponse videoListResponse = youtube.videos().list("statistics").setId(videoId).execute();
						 
						 resultList.add(new VideoItem(videoId, title, description));
						 */
						 
					 } // end if
					 
				 } // end while
				 
				 /**
				  * Busca realizada, a lista videoId foi preenchida pelos IDs encontrados pela busca.
				  */
				 
				 /**
				  * Fazer uma busca usando Videos.list da API. Esta lista retorna objetos "Video", uma representa��o em Java
				  * do objeto JSON enviado pelo servidor.
				  * Por este objeto, obter t�tulo, descri��o e estat�sticas (como viewcount).
				  */
				 
				 /**
				  * Montar a Lista:
				  * youtube.videos() - chamada da API
				  * .list("id,snippet,statistics") - Informar quais atributos do JSON retornar - 
				  * https://developers.google.com/youtube/v3/docs/videos#resource
				  * .setId - A �nica forma de pesquisar os v�deos desta forma � por ID. Juntar tudo usando v�rgula como delimitador.
				  * .execute() - Autoexplicativo.
				  */
				 System.out.println("�ouTubeHandler]: Montando lista de Objetos Video com IDs encontrados");
				 VideoListResponse vlr = youtube.videos().list("id,snippet,statistics")
						 .setId(TextUtils.join(",", videoIds))
						 .setKey(apiKey)
						 .execute(); 
				 
				 for (Video video : vlr.getItems()) {
					 /**
					  * Para cada video, buscar as informa��es relevantes e instanciar um VideoItem.
					  * @TODO
					  * Isso deve ser alterado na vers�o final. Trabalhar diretamente com os videos e envi�-los ao Chromecast?
					  */
					 String videoId = video.getId();
					 String title = video.getSnippet().getTitle();
					 String description = video.getSnippet().getDescription();
					 BigInteger viewCount = video.getStatistics().getViewCount();
					 
					 // Adicionar o VideoItem ao Array.
					 videoItemList.add(new VideoItem(videoId, title, description, viewCount));
					 System.out.println("[YouTubeHandler] Video adicionado � lista para o View: " + title);
					 
				 }
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	      
		return videoItemList;
	}
	  
	  
}
