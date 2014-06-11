package com.xrdev.musicast.connection;

public class SpotifyWrapper {
	static {
		System.loadLibrary("spotify"); // Carregar a API spotify.so
		System.loadLibrary("SpotifyWrapper"); // Carregar as implementa��es em C++.
	}
	
	// public native int testeSoma(int v1, int v2);
	
	public static native String testeString();
	
	// Para implementar de fato no Spotify depois:
	
	public native static void init(String directory);
	
	public native static void login(String username, String password);
}
