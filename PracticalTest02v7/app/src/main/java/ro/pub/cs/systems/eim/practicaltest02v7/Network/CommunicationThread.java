package ro.pub.cs.systems.eim.practicaltest02v7.Network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02v7.Utilities;
import ro.pub.cs.systems.eim.practicaltest02v7.Model.TimerInformation;
import java.util.HashMap;

public class CommunicationThread extends Thread {
    private ServerThread serverThread;
    private Socket socket;

    private static boolean canGetUTC = true;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e("PracticalTest02", "Communication Thread - Socket is null!");
            return;
        }

        try {
            // Ex 3 - Obiecte folosite pentru citire si scriere
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            if (bufferedReader == null || printWriter == null) {
                Log.e("PracticalTest02", "Communication Thread - Buffered Reader / Print Writer are null!");
                return;
            }

            // Ex 3 - Citire date de la client
            String timer_action = bufferedReader.readLine();
            if (timer_action == null || timer_action.isEmpty()) {
                Log.e("PracticalTest02", "Communication Thread - Error receiving parameters from client (timer action)!");
                return;
            }

            Log.i("PracticalTest02", "Communication Thread - Received: " + timer_action);
            String[] actiondata = timer_action.substring(0, timer_action.length() - 2).split(",");
            String clientIP = socket.getInetAddress().toString();
            HashMap<String, TimerInformation> data = serverThread.getData();
            switch(actiondata[0]) {
                // cazul de setare a timerului
                case "set":
                    canGetUTC = true;
                    String hour = actiondata[1];
                    String minute = actiondata[2];
                    serverThread.setData(clientIP, new TimerInformation(hour, minute));
                    Log.i("PracticalTest02", "Communication Thread - Set: " + hour + " " + minute);
                    break;
                    // cazul de poll
                case "poll":
                    if (data.containsKey(clientIP)) {
                        if (canGetUTC) {
                            TimerInformation timerInformation = data.get(clientIP);
                            String timerHour = timerInformation.getHour();
                            String timerMinute = timerInformation.getMinute();
                            String utcTime = getTime();
                            String utcHour = utcTime.split(" ")[2].split(":")[0];
                            String utcMinute = utcTime.split(" ")[2].split(":")[1];
                            Log.i("PracticalTest02", "Communication Thread - Poll: " + timerHour + " " + timerMinute + " " + utcHour + " " + utcMinute);

                            int timerHourInt = Integer.parseInt(timerHour);
                            int timerMinuteInt = Integer.parseInt(timerMinute);
                            int utcHourInt = Integer.parseInt(utcHour);
                            int utcMinuteInt = Integer.parseInt(utcMinute);

                            // verificare depasire timp utc
                            if (utcHourInt > timerHourInt || (utcHourInt == timerHourInt && utcMinuteInt > timerMinuteInt)) {
                                canGetUTC = false;
                                printWriter.println("active\n");
                                printWriter.flush();
                            } else {
                                printWriter.println("inactive\n");
                                printWriter.flush();
                            }
                        } else {
                            printWriter.println("active\n");
                            printWriter.flush();
                        }


                    } else {
                        Log.i("PracticalTest02", "Communication Thread - None");
                        printWriter.println("none\n");
                        printWriter.flush();
                    }
                    break;
                    // cazul de reset
                case "reset":
                    canGetUTC = true;
                    data.remove(clientIP);
                    break;
                default:
                    Log.e("PracticalTest02", "Communication Thread - Invalid action!");
                    break;
            }


        } catch (IOException ioException) {
            Log.e("PracticalTest02", "Communication Thread - An exception has occurred: " + ioException.getMessage());
            ioException.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e("PracticalTest02", "Communication Thread - An exception has occurred: " + ioException.getMessage());
                    ioException.printStackTrace();
                }
            }
        }

    }

    private String getTime() {
        String utcTime = null;
        try {
            Socket socket = new Socket("utcnist.colorado.edu", 13);
            BufferedReader bufferedReader = Utilities.getReader(socket);
            bufferedReader.readLine();
            utcTime = bufferedReader.readLine();
            Log.i("PracticalTest02", "utcTime: " + utcTime);
        } catch (IOException ioException) {
            Log.e("PracticalTest02", ioException.getMessage());
            ioException.printStackTrace();
        }

        return utcTime;
    }
}
