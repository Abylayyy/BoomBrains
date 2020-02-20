package kz.almaty.boombrains;

import android.app.Application;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MyApplication extends Application {

    public static final String URL = "http://192.168.1.220:3000/online:battle";

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
