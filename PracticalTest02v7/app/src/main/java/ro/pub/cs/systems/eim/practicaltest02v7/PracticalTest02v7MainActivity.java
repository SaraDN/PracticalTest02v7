package ro.pub.cs.systems.eim.practicaltest02v7;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ro.pub.cs.systems.eim.practicaltest02v7.Network.ClientThread;
import ro.pub.cs.systems.eim.practicaltest02v7.Network.ServerThread;

public class PracticalTest02v7MainActivity extends AppCompatActivity {
    // Ex 3 - Server Implementare
    ServerThread serverThread = null;

    // Ex 4 - Client Implementare
    ClientThread clientThread = null;

    // Ex 2 - Interfata grafica
    // Interfata server
    private EditText serverPortEditText = null;
    private Button connectButton = null;

    // Interfata client
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
    private EditText timerActionEditText = null;
    private Button timerActionButton = null;
    private TextView timerActionTextView = null;

    // Listener Server
    // Listener Server
    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();

    private class ConnectButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            // Ex 2 - Listener Buton Server
            // Se extrage portul pe care serverul va astepta conexiuni
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.i("PracticalTest02", "Server connection parameters: " + serverPort);

            // Ex 3 - Server Implementare
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) { // getServerSocket() este un getter
                Log.e("PracticalTest02", "Could not create server thread!");
                return;
            }
            serverThread.start();
        }
    }

    // Listener Client
    private TimerActionButtonClickListener timerActionButtonClickListener = new TimerActionButtonClickListener();

    private class TimerActionButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            // Ex 2 - Listener Buton Client
            // Se extrag datele necesare pentru conectare
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();
            String timerAction = timerActionEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty() ||
                    clientPort == null || clientPort.isEmpty() ||
                    timerAction == null || timerAction.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.i("PracticalTest02", "Client connection parameters: " + clientAddress + " " + clientPort + " " + timerAction);

            // Ex 3 - Client Implementare
            timerActionTextView.setText("");
            clientThread = new ClientThread(clientAddress, Integer.parseInt(clientPort), timerAction, timerActionTextView);
            clientThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02v7_main);

        // Ex 2 - Interfata grafica
        // Interfata Server
        serverPortEditText = (EditText) findViewById(R.id.server_port_edit_text);
        connectButton = (Button) findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        // Interfata Client
        clientAddressEditText = (EditText) findViewById(R.id.client_address_edit_text);
        clientPortEditText = (EditText) findViewById(R.id.client_port_edit_text);
        timerActionEditText = (EditText) findViewById(R.id.timer_action_edit_text);
        timerActionButton = (Button) findViewById(R.id.get_timer_action_button);
        timerActionTextView = (TextView) findViewById(R.id.timer_action_text_view);
        timerActionButton.setOnClickListener(timerActionButtonClickListener);
    }

    @Override
    protected void onDestroy() {
        // Ex 3 - Server Implementare
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
