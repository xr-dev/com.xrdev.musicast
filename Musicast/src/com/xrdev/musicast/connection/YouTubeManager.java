package com.xrdev.musicast.connection;

import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.cast.MediaInfo;
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
import com.xrdev.musicast.Application;
import com.xrdev.musicast.model.QueueList;
import com.xrdev.musicast.model.TrackItem;
import com.xrdev.musicast.model.VideoItem;

import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class YouTubeManager {
	private static final long NUMBER_OF_VIDEOS_RETURNED = 1;
	
	/** Global instance of the HTTP transport. */
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	/** Global instance of Youtube object to make all API requests. */
	private static YouTube youtube;

	//public static ArrayList<VideoItem> searchVideos(String searchTerm) {
	public static VideoItem searchVideo(String searchTerm){
			//ArrayList<VideoItem> videoItemList = new ArrayList<VideoItem>();
		VideoItem resultVideo = null;

		try {
			// Construir o objeto que será a referência para todas as solicitações à API.
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
		          public void initialize(HttpRequest request) throws IOException {}
		        }).setApplicationName("musicast").build();
			
			System.out.println("[YoutubeHandler]: Conexão construída.");
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
			 * setFields: reduz as informações retornadas para apenas os campos necessários.
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
			 * Pegar apenas os IDs dos vídeos encontrados.
			 */
			if (searchResultList != null) {
				 Iterator resultsIterator = searchResultList.iterator();
				 
				 while (resultsIterator.hasNext()) {
					 SearchResult searchResult = (SearchResult) resultsIterator.next();
					 ResourceId rId = searchResult.getId(); // Pegar o ID do resultado.
					 
					 // Verifica se o resultado é um video. Adiciona a lista de IDs.
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
				  * Fazer uma busca usando Videos.list da API. Esta lista retorna objetos "Video", uma representaçao em Java
				  * do objeto JSON enviado pelo servidor.
				  * Por este objeto, é possível obter título, descrição, estatísticas (como viewcount) e contentDetails (como duração).
				  */
				 
				 /**
				  * Montar a Lista:
				  * youtube.videos() - chamada da API
				  * .list("id,snippet,statistics,contentDetails") - Informar quais atributos do JSON retornar -
				  * https://developers.google.com/youtube/v3/docs/videos#resource
				  * .setId - A única forma de pesquisar os vídeos desta forma é por ID. Juntar tudo usando vírgula como delimitador.
				  * .execute() - Autoexplicativo.
				  */
				 System.out.println("[YouTubeHandler]: Montando lista de Objetos Video com IDs encontrados");
				 // VideoListResponse vlr = youtube.videos().list("id,snippet,statistics,contentDetails") // Adicionado ,contentDetails - testar.
                 VideoListResponse vlr = youtube.videos().list("id,contentDetails")
                        .setId(TextUtils.join(",", videoIds))
                        .setKey(apiKey)
                        .execute();
				 
				 for (Video video : vlr.getItems()) {
					 /**
					  * Para cada video, buscar as informações relevantes e instanciar um VideoItem.
					  * @TODO
					  * Isso deve ser alterado na versão final. Trabalhar diretamente com os videos e enviá-los ao Chromecast?
					  */
					 String videoId = video.getId();
					 //String title = video.getSnippet().getTitle();
					 //String description = video.getSnippet().getDescription();
					 //BigInteger viewCount = video.getStatistics().getViewCount();
                     String durationString = video.getContentDetails().getDuration();


                     // Converter a duração do formato String enviado pela API para segundos.
                     PeriodFormatter formatter = ISOPeriodFormat.standard();
                     Period p = formatter.parsePeriod(durationString);
                     Seconds s = p.toStandardSeconds();

                     int durationInt = s.getSeconds();

                     // Adicionar o VideoItem ao Array.
					 // videoItemList.add(new VideoItem(videoId, title, description, viewCount, durationInt));

                     resultVideo = new VideoItem(videoId, durationInt);

                     System.out.println("[YouTubeHandler] Video adicionado à lista para o View: " + videoId);
					 
				 }
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	      
		return resultVideo;
	}

    public static void associateYouTubeData(TrackItem item, QueueList queue){
        String artists;
        String trackName;

        // Recuperar dados de um item.

        if (item.getArtists() == null)
            artists = "";
        else
            artists = item.getArtists();

        if (item.getName() == null)
            trackName = "";
        else
            trackName = item.getName();

        // Buscar no Youtube por vídeos correspondentes.
        VideoItem video = searchVideo(artists + " - " + trackName);

        if (video == null) {
            item.setYoutubeId(TrackItem.VIDEO_NOT_FOUND);
            return;
        }
        // Procurar correlação usando a duração dos vídeos.
        // Configurar a tolerância na duração pelo if abaixo.
        if (video.getDurationInt() <= (item.getDuration() + 15)
                && video.getDurationInt() >= (item.getDuration() - 15)){
            item.setYoutubeId(video.getVideoId());
            queue.addValidTrack(item);
            item.setQueueIndex(queue.getValidTracks().size() - 1);
        } else {
            item.setYoutubeId(TrackItem.VIDEO_NOT_FOUND);
        }
    }

    public static ArrayList<TrackItem> associateYouTubeData(ArrayList<TrackItem> arrayList){
        String artists;
        String trackName;

        // Recuperar dados de cada item da playlist.

        for (TrackItem item : arrayList) {

            if (item.getArtists() == null)
                artists = "";
            else
                artists = item.getArtists();

            if (item.getName() == null)
                trackName = "";
            else
                trackName = item.getName();

            // Buscar no Youtube por vídeos correspondentes.
            VideoItem video = searchVideo(artists + " - " + trackName);

            // Procurar correlação usando a duração dos vídeos.
                // Configurar a tolerância na duração pelo if abaixo.
                if (video.getDurationInt() <= (item.getDuration() + 15)
                        && video.getDurationInt() >= (item.getDuration() - 15)){
                    item.setYoutubeId(video.getVideoId());
                } else {
                    item.setYoutubeId(TrackItem.VIDEO_NOT_FOUND);
                }
            }

        return arrayList;

    }
	  
	  
}
