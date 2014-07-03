/*
 * spotifywrapper.cpp
 *	Implementação das chamadas à API do Spotify.
 *  Created on: 10/06/2014
 *      Author: Guilherme
 */



#include <string>
#include "spotifywrapper.h"
#include "key.h"

using namespace std;

static sp_session *s_session;

//A message from the main Spotify thread
static void log_message(sp_session *session, const char *data) {
    //do what you like with the log
}

//A callback to indicate that we need to process events
static void notify_main_thread(sp_session *session) {
    int next_timeout = 0;
    do {
        //the value returned in next_timeout is when libspotify would like you to call sp_session_process_events again
        //so you should track this and keep it in a static variable somewhere.
        sp_session_process_events(session, &next_timeout);
    } while (next_timeout == 0);
}

//structure defined here https://developer.spotify.com/docs/libspotify/12.1.51/structsp__session__callbacks.html
static sp_session_callbacks callbacks = {
		NULL,        //Logged In callback
		NULL,        //Logged out callback
		NULL,        //Metadata changed callback
		NULL,        //Connection error callback
		NULL,        //Message to user callback
		&notify_main_thread,
		NULL,        //Music delivery callback
		NULL,        //Play token lost callback
		&log_message,
		NULL,        //End of track  callback
                NULL,        //Streaming error callback
                NULL,        //Userinfo updated
                NULL,        //Start playback
                NULL,        //Stop playback
                NULL,        //Audio buffer stats
                NULL,        //Offline status updated
                NULL,        //Offline error
                NULL,        //Credentials blob updated
                NULL,        //Connection state updated
                NULL,        //Scrobble error
                NULL         //Private session changed
};

/**
 * Inicializa a conexão com o Spotify.
 */
string init_spotify(void *directory) {
    string path = (char *)directory;

    sp_session_config config;

    memset(&config, 0, sizeof(config));

    string cache_location = path + "/cache";
    string settings_location = path + "/settings";
    string trace_file = path + "/cache/trace.txt";

    config.api_version = SPOTIFY_API_VERSION;
    config.cache_location = cache_location.c_str();
    config.settings_location = settings_location.c_str();

    //these next two variables are defined in key.h
    config.application_key = g_appkey;
    config.application_key_size = g_appkey_size;

    config.user_agent = "Musicast";
    config.callbacks = &callbacks;
    config.tracefile = trace_file.c_str();
    // config.tracefile = NULL;

    sp_error error = sp_session_create(&config, &s_session);

    string debug_strcallback;

    if (SP_ERROR_OK != error) {
        //Most calls in libspotify return an error enum, where 0 (SP_ERROR_OK) indicates success.
        //To be here means that Spotify has not been initialised and you will need to check the error code to see why.
    	// Implementar tratamento de códigos de erro.
    	switch (error) {
    	case SP_ERROR_BAD_API_VERSION:
    		debug_strcallback = "Erro: BAD_API_VERSION)";
    		break;
    	case SP_ERROR_BAD_USER_AGENT:
    		debug_strcallback = "Erro: BAD_USER_AGENT";
    		break;
    	case SP_ERROR_BAD_APPLICATION_KEY:
    		debug_strcallback = "Erro: SP_ERROR_BAD_APPLICATION_KEY";
    		break;
    	case SP_ERROR_API_INITIALIZATION_FAILED:
    		debug_strcallback = "Erro: SP_ERROR_API_INITIALIZATION_FAILED";
    		break;
    	case SP_ERROR_INVALID_DEVICE_ID:
    		debug_strcallback = "Erro: SP_ERROR_INVALID_DEVICE_ID";
    		break;
    	case SP_ERROR_CANT_OPEN_TRACE_FILE:
    		debug_strcallback = "Erro: SP_ERROR_CANT_OPEN_TRACE_FILE";
    		break;
    	default:
    		debug_strcallback = "Erro: erro desconhecido. Código:" + error;
    	}
    } else {
    	debug_strcallback = "Debug: Sessão iniciada com sucesso.";
    }

    return debug_strcallback;
}


