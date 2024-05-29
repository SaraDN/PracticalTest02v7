package ro.pub.cs.systems.eim.practicaltest02v7.Network;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import cz.msebera.android.httpclient.client.ClientProtocolException;
import ro.pub.cs.systems.eim.practicaltest02v7.Model.TimerInformation;

public class ServerThread extends Thread{
    private int port = 0;

    private ServerSocket serverSocket = null;

    private HashMap<String, TimerInformation> data = null;

    public ServerThread(int port) {
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException ioException) {
            Log.e("PracticalTest02", "Server Thread - An exception has occurred: " + ioException.getMessage());
            ioException.printStackTrace();
        }

        // Ex 3 - Initializare obiect local
        this.data = new HashMap<>();
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Ex 3 - Asteptare conexiune client
                Log.i("PracticalTest02", "Server Thread - Waiting for a client invocation...");
                Socket socket = serverSocket.accept();
                Log.i("PracticalTest02", "Server Thread - A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());

                // Ex 3 - Creare thread de comunicare client
                CommunicationThread communicationThread = new CommunicationThread(this, socket);
                communicationThread.start();
            }

        } catch (ClientProtocolException clientProtocolException) { // Cred ca merge si fara asta
            Log.e("PracticalTest02", "Server Thread - An exception has occurred: " + clientProtocolException.getMessage());
            clientProtocolException.printStackTrace();
        } catch (IOException ioException) {
            Log.e("PracticalTest02", "Server Thread - An exception has occurred: " + ioException.getMessage());
            ioException.printStackTrace();
        }
    }

    public void stopThread() {
        interrupt();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ioException) {
                Log.e("PracticalTest02", "Server Thread - An exception has occurred: " + ioException.getMessage());
                ioException.printStackTrace();
            }
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public synchronized void setData(String key, TimerInformation data) {
        this.data.put(key, data);
    }

    public synchronized HashMap<String, TimerInformation> getData() {
        return data;
    }
}

