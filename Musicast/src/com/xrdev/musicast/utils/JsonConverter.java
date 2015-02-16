package com.xrdev.musicast.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.xrdev.musicast.model.JsonModel;
import com.xrdev.musicast.model.LocalQueue;
import com.xrdev.musicast.model.TrackItem;

/**
 * Created by Guilherme on 12/01/2015.
 */
public class JsonConverter {
    private static String TAG = "JSON_CONVERTER";
    private Gson gson = new Gson();
    private String type;
    private String jsonString;
    private JsonModel jsonModel;
    public static String TYPE_LOAD_VIDEO = "loadVideo";
    public static String TYPE_LOAD_PLAYLIST = "loadPlaylist";
    public static String TYPE_PLAY_VIDEO_AT = "playVideoAt";
    public static String TYPE_PLAY_VIDEO = "playVideo";
    public static String TYPE_PAUSE_VIDEO = "pauseVideo";
    public static String TYPE_GET_STATUS = "getStatus";
    public static String TYPE_PLAY_PREVIOUS = "previousVideo";
    public static String TYPE_PLAY_NEXT = "nextVideo";
    public static String TYPE_SHOW_OVERLAY = "showOverlay";

    public String makeJson(String type, Object obj){

        if (type.equals(TYPE_LOAD_VIDEO) && obj instanceof TrackItem) {

            jsonModel = new JsonModel();
            jsonModel.setType(type);
            jsonModel.setVideoId(((TrackItem) obj).getYoutubeId());

            jsonString = gson.toJson(jsonModel);

        } else {
            // Argumentos incorretos, apresentar mensagem de erro?
        }

        writeLog();

        return jsonString;
    }

    public String makeLoadPlaylistJson(String type, LocalQueue queue, int index){

        jsonModel = new JsonModel();
        jsonModel.setType(type);
        jsonModel.setVideoIds(queue.getValidIds());
        jsonModel.setIndex(String.valueOf(index));
        jsonModel.setTracksMetadata(queue.getValidTracks());

        jsonString = gson.toJson(jsonModel);

        writeLog();

        return jsonString;
    }

    public String makeGenericTypeJson(String type) {
        jsonModel = new JsonModel();
        jsonModel.setType(type);
        jsonString = gson.toJson(jsonModel);

        return jsonString;
    }


    public void writeLog() {
        Log.d(TAG,"Mensagem enviada JSON: " + jsonString);
    }

}
