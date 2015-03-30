package com.xrdev.musicast.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.xrdev.musicast.connection.SpotifyManager;
import com.xrdev.musicast.model.Token;

import org.joda.time.DateTime;

/**
 * Created by Guilherme on 03/08/2014.
 */
public class PrefsManager {

    private static final String PREFS_NAME = "MusicastPrefs";
    private static final String KEY_ACCESS_TOKEN = "accessToken";
    private static final String KEY_REFRESH_TOKEN = "refreshToken";
    private static final String KEY_CODE = "code";
    private static final String KEY_EXPIRATION_DATETIME = "expirationTime";
    
    public static void setCodeToPrefs(Context context, String code) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_CODE, code);
        editor.apply();
    }

    public static String getCodeFromPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(KEY_CODE, null);
    }


    public static void setTokenToPrefs(Context context, Token token) {
        String accessString = token.getAccessString();
        String refreshString = token.getRefreshString();
        DateTime expirationDt = token.getExpirationDt();

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(KEY_ACCESS_TOKEN, accessString);
        editor.putString(KEY_REFRESH_TOKEN, refreshString);
        editor.putString(KEY_EXPIRATION_DATETIME, expirationDt.toString());
        editor.apply();
    }

    public static void setAccessToken(Context context, String accessToken) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    public static Token getTokenFromPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String accessString = prefs.getString(KEY_ACCESS_TOKEN, null);
        String refreshString = prefs.getString(KEY_REFRESH_TOKEN, null);
        String expirationString = prefs.getString(KEY_EXPIRATION_DATETIME, null);

        return new Token(accessString, refreshString, expirationString);
    }

    public static String getAccessToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(KEY_ACCESS_TOKEN, null);
    }

    public static Token getValidToken(Context context) {
        Token currentToken = getTokenFromPrefs(context);

        if (currentToken.isValid()) {
            return currentToken;
        } else {
            Token refreshedToken = SpotifyManager.getRefreshedToken(context);
            if (refreshedToken == null) {
                return null;
            } else {
                setTokenToPrefs(context, refreshedToken);
                return refreshedToken;
            }
        }
    }

    public static void clearPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();

        editor.clear().apply();
    }
}
