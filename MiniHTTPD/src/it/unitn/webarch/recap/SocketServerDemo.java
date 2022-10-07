package it.unitn.webarch.recap;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerDemo {
    public static void main(String a[]) {
        boolean finished = false;
        try {
            ServerSocket listener = new ServerSocket(1234);
            while (!finished) {
                Socket aClient = listener.accept();
                // wait for connection
                InputStream in = aClient.getInputStream();
                InputStreamReader din = new InputStreamReader(in);
                BufferedReader bin = new BufferedReader(din);
                OutputStream out = aClient.getOutputStream();
                PrintStream pout = new PrintStream(out);
                // Read a string
                String request = bin.readLine();
                System.out.println("[INFO] Got from the client: " + request);

                // Say "Goodbye"
                String greeting = "Goodbye! you were on port " + aClient.getPort() + ".";
                pout.println(greeting);
                System.out.println("[INFO] Said to the client: " + greeting);

                aClient.close();
            }
            listener.close();
        } catch (IOException e) {
        }
    }
}
