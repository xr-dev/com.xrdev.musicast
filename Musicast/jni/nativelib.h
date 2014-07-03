/*
 * nativelib.h
 *	Interface da biblioteca nativa JNI que será chamada para resolver os callbacks à API do Spotify.
 *  Created on: 10/06/2014
 *      Author: Guilherme
 */

#ifndef NATIVELIB_H_
#define NATIVELIB_H_

#include <jni.h>

// Interface para a biblioteca NATIVA.

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
extern "C" {
JNIEXPORT jstring JNICALL Java_com_xrdev_musicast_connection_SpotifyWrapper_testeString(JNIEnv *je, jclass jc);


JNIEXPORT jstring JNICALL Java_com_xrdev_musicast_connection_SpotifyWrapper_init(JNIEnv *je, jclass jc, jstring username, jstring password, jstring cache, jstring tracefile);
JNIEXPORT void JNICALL Java_com_xrdev_musicast_connection_SpotifyWrapper_login(JNIEnv *je, jclass jc, jstring username, jstring password);

}


#endif /* NATIVELIB_H_ */



