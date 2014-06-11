/*
 * nativelib.cpp
 *	Implementação da biblioteca nativa JNI que será chamada para resolver os callbacks à API do Spotify.
 *  Created on: 10/06/2014
 *      Author: Guilherme
 */

#include "nativelib.h"


/*
 * Como representar os métodos vindos do Java:
 * JNIEXPORT void JNICALL
 * Java_
 * pacote_subpacotes_
 * classe_
 * metodo
 */

/*
 * Tipos de dados do Java são definidos para tipos nativos pelo JNIEnv: http://docs.oracle.com/javase/1.5.0/docs/guide/jni/spec/types.html
 */

JNIEXPORT jstring JNICALL Java_com_xrdev_musicast_connection_SpotifyWrapper_testeString(JNIEnv *je, jclass jc) {
	return je->NewStringUTF("JNI configurado com sucesso!");
}

JNIEXPORT void JNICALL Java_com_xrdev_musicast_connection_SpotifyWrapper_init(JNIEnv *je, jclass jc, jstring directory) {

}

JNIEXPORT void JNICALL Java_com_xrdev_musicast_connection_SpotifyWrapper_login(JNIEnv *je, jclass jc, jstring username, jstring password) {

}



