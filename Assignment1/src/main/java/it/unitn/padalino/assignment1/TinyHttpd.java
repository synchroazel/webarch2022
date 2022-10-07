package it.unitn.padalino.assignment1;

import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;


public class TinyHttpd {
    public static void main(String argv[])
            throws IOException {
        int port = 8000;
        if (argv.length > 0) port = Integer.parseInt(argv[0]);
        ServerSocket ss = new ServerSocket(port);

        String timeStamp = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss,SSS] ").format(new java.util.Date());
        System.out.println(timeStamp + "[INFO] Server is ready.\n");

        while (true)
            new TinyHttpdConnection(ss.accept());
    }
}