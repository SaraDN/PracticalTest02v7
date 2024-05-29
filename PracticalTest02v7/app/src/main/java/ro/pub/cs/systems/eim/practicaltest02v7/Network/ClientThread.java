package ro.pub.cs.systems.eim.practicaltest02v7.Network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02v7.Utilities;

public class ClientThread extends Thread{
    private String address;
    private int port;
    private String timer_action;
    private TextView timerTextView;

    public ClientThread(String address, int port, String timer_action, TextView timerTextView) {
        this.address = address;
        this.port = port;
        this.timer_action = timer_action;
        this.timerTextView = timerTextView;
    }

    @Override
    public void run() {
        try {
            // Ex 3-4 Conectare la server
            Socket socket = new Socket(address, port);
            if (socket == null) {
                Log.e("PracticalTest02", "Could not create socket!");
                return;
            }

            // Ex 3-4 Obiecte folosite pentru citire si scriere
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e("PracticalTest02", "Buffered Reader / Print Writer are null!");
                return;
            }

            Log.i("PracticalTest02", "ClientThread sent: " + timer_action);

            // Ex 3-4 Trimitere date catre server
            printWriter.println(timer_action);
            printWriter.flush(); // necesar pentru trimiterea datelor

            // Ex 3-4 Citire date de la server
            String response;
            response = bufferedReader.readLine();
            timerTextView.post(new Runnable() {
                @Override
                public void run() {
                    timerTextView.setText(response);
                }
            });
        } catch (IOException ioException) {
            Log.e("PracticalTest02", "An exception has occurred: " + ioException.getMessage());
            ioException.printStackTrace();
        }
    }
}
