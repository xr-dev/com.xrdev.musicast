package com.xrdev.musicast.utils;

import android.content.Context;
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
    private Context mContext;
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
    public static String TYPE_CHANGE_MODE = "changeMode";

    public JsonConverter(Context context) {
        mContext = context;
    }

    public String makeJson(String type, Object obj){

        if (type.equals(TYPE_LOAD_VIDEO) && obj instanceof TrackItem) {

            jsonModel = new JsonModel();
            jsonModel.setType(type);
            jsonModel.setVideoId(((TrackItem) obj).getYoutubeId());
            jsonModel.setUUID(PrefsManager.getUUID(mContext));

            jsonString = gson.toJson(jsonModel);

        } else {
            // Argumentos incorretos, apresentar mensagem de erro?
        }

        writeLog();

        return jsonString;
    }

    public String makeLoadPlaylistJson(LocalQueue queue, int index){

        jsonModel = new JsonModel();
        jsonModel.setType(TYPE_LOAD_PLAYLIST);
        jsonModel.setVideoIds(queue.getValidIds());
        jsonModel.setIndex(String.valueOf(index));
        jsonModel.setTracksMetadata(queue.getValidTracks());
        jsonModel.setUUID(PrefsManager.getUUID(mContext));

        jsonString = gson.toJson(jsonModel);

        writeLog();

        return jsonString;
    }

    public String makeGenericTypeJson(String type) {
        jsonModel = new JsonModel();
        jsonModel.setType(type);
        jsonModel.setUUID(PrefsManager.getUUID(mContext));
        jsonString = gson.toJson(jsonModel);

        return jsonString;
    }

    public String makeModeJson(int mode) {
        jsonModel = new JsonModel();
        jsonModel.setType(TYPE_CHANGE_MODE);
        jsonModel.setMessage(String.valueOf(mode));
        jsonModel.setUUID(PrefsManager.getUUID(mContext));

        jsonString = gson.toJson(jsonModel);

        writeLog();

        return jsonString;
    }


    public void writeLog() {
        Log.d(TAG,"Mensagem enviada JSON: " + jsonString);
    }

}
