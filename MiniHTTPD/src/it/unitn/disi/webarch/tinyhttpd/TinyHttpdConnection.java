package it.unitn.disi.webarch.tinyhttpd;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

class TinyHttpdConnection extends Thread {

    Socket sock;

    TinyHttpdConnection(Socket s) {
        sock = s;
        setPriority(NORM_PRIORITY - 1);
        start();
    }

    @Override
    public void run() {
        System.out.println("====================================");
        System.out.println("Connected on port " + sock.getPort());
        OutputStream out = null;
        try {
            // OPEN SOCKETS FOR READING AND WRITING
            BufferedReader d =
                    new BufferedReader(new InputStreamReader(
                            sock.getInputStream()));
            out = sock.getOutputStream();
            PrintStream ps = new PrintStream(out);
            // READ REQUEST
            String req = d.readLine();
            if (req == null) return;
            System.out.println("Request: " + req);
            // READ REQUEST HEADERS
            String head = null;
            do {
                head = d.readLine();
                System.out.println("Header: " + head);
            } while (head != null && head.length() > 0);
            // PARSE REQUEST
            StringTokenizer st = new StringTokenizer(req);
            if ((st.countTokens() >= 2) && st.nextToken().equals("GET")) {
                if ((req = st.nextToken()).startsWith("/")) {
                    req = req.substring(1);
                }
                if (req.endsWith("/") || req.equals("")) {
                    req = req + "index.html";
                }
                // OPEN REQUESTED FILE AND COPY IT TO CLIENT
                try {
                    //All our requested files must be in the "Documents" directory
                    FileInputStream fis = new FileInputStream("Documents/" + req);
                    int responseLength = fis.available();
                    // LET'S SEND THE RESPONSE HEADERS
                    ps.print("HTTP/1.1 200 OK\r\n");
                    ps.print("Content-Length: " + responseLength + "\r\n");
                    ps.print("Content-Type: text/html\r\n");
                    ps.print("\r\n");
                    // LET'S SEND THE CONTENT
                    byte[] data = new byte[responseLength];
                    fis.read(data);
                    out.write(data);
                    fis.close();
                } catch (FileNotFoundException e) {
                    ps.print("HTTP/1.1 404 Not Found \r\n\r\n");
                    System.out.println("404 Not Found: " + req);
                }
            } else {
                ps.print("HTTP/1.1 400 Bad Request\r\n\r\n");
                System.out.println("400 Bad Request: " + req);
            }
        } catch (IOException e) { // Let's catch all generic I/O troubles
            System.out.println("Generic I/O error " + e);
        } finally { // the following code is ALWAYS executed
            try {
                // let's close all channels
                out.close();
                sock.close();
            } catch (IOException ex) {
                System.out.println("I/O error on close" + ex);
            }
        }

        System.out.println("====================================");
    }
}
