/*
 * nativelib.cpp
 *	Implementa��o da biblioteca nativa JNI que ser� chamada para resolver os callbacks � API do Spotify.
 *  Created on: 10/06/2014
 *      Author: Guilherme
 */

#include "nativelib.h"


/*
 * Como representar os m�todos vindos do Java:
 * JNIEXPORT void JNICALL
 * Java_
 * pacote_subpacotes_
 * classe_
 * metodo
 */

/*
 * Tipos de dados do Java s�o definidos para tipos nativos pelo JNIEnv: http://docs.oracle.com/javase/1.5.0/docs/guide/jni/spec/types.html
 */

JNIEXPORT jstring JNICALL Java_com_xrdev_musicast_connection_SpotifyWrapper_testeString(JNIEnv *je, jclass jc) {
	return je->NewStringUTF("JNI configurado com sucesso!");
}

JNIEXPORT void JNICALL Java_com_xrdev_musicast_connection_SpotifyWrapper_init(JNIEnv *je, jclass jc, jstring directory) {

}

JNIEXPORT void JNICALL Java_com_xrdev_musicast_connection_SpotifyWrapper_login(JNIEnv *je, jclass jc, jstring username, jstring password) {

}



