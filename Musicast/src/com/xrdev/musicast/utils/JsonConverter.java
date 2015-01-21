package com.xrdev.musicast.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.xrdev.musicast.model.JsonModel;
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
    public static String TYPE_PLAY_VIDEO = "playVideo";
    public static String TYPE_PAUSE_VIDEO = "pauseVideo";
    public static String TYPE_STOP_VIDEO = "stopVideo";
    public static String TYPE_GET_STATUS = "getStatus";

    public String makeJson(String type, Object obj){

        if (type.equals(TYPE_LOAD_VIDEO) && obj instanceof TrackItem) {

            jsonModel = new JsonModel();
            jsonModel.setType(type);
            jsonModel.setVideoId(((TrackItem) obj).getYoutubeId());

            jsonString = gson.toJson(jsonModel);

        } else {
            // Argumentos incorretos, apresentar mensagem de erro?
        }

        Log.d(TAG,"Mensagem enviada JSON: " + jsonString);
        return jsonString;
    }

}
