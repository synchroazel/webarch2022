package it.unitn.padalino.assignment1;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

public class TinyHttpdConnection extends Thread {

    Socket sock;

    TinyHttpdConnection(Socket s) {
        sock = s;
        setPriority(NORM_PRIORITY - 1);
        start();
    }

    @Override
    public void run() {

        // print current timestamp (not essential but nice-to-have)
        String timeStamp = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss,SSS]").format(new java.util.Date());
        System.out.println("──────" + timeStamp + "──────");
        System.out.println("Connected on port " + sock.getPort());
        OutputStream out = null;

        try {

            // OPEN SOCKETS FOR READING AND WRITING
            BufferedReader d = new BufferedReader(new InputStreamReader(sock.getInputStream()));
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
                    System.out.println(req);
                }

                if (req.equals("/") || req.equals("")) {
                    // go to default page
                    req = req + "index.html";
                }

                if (req.equals("process") || req.equals("process/") || req.matches("process\\/reverse\\?.*=.*")) {
                    // do process
                    if (req.contains("?")) {  // if parameters were passed

                        String params = req.split("\\?")[1];
                        String pname = params.split("=")[0];
                        String pval = params.split("=")[1];

                        ProcessBuilder build = new ProcessBuilder();

                        String path_to_cmd = "src/main/java/it/unitn/padalino/assignment1/StringFormatter.java";

                        build.command("bash", "-c", "java " + path_to_cmd + " " + pval);

                        String reversed = null;

                        try {
                            Process process = build.start();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            reversed = reader.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        System.out.println(reversed);

                        // Let's send the response headers
                        ps.print("HTTP/1.1 200 OK\r\n");
                        ps.print("Content-Type: text/html\r\n");
                        ps.print("\r\n");

                        // Let's write the response contents
                        ps.println("<!DOCTYPE html>");
                        ps.println("<html>");
                        ps.println("<h1>String Reversal</h1>");
                        ps.println("<p1>Original string is: <b>" + pval + "</b></p1><br>");
                        ps.println("<br><p1>Reversed string is: <b>" + reversed + "</b></p1>");
                        ps.println("</html>");

                    } else {  // if no parameters were passed
                        req = "reverse.html";
                    }
                }

                // OPEN REQUESTED FILE AND COPY IT TO CLIENT

                try {

                    FileInputStream fis = new FileInputStream("src/main/resources/static/" + req);
                    int responseLength = fis.available();

                    // Let's send the response headers
                    ps.print("HTTP/1.1 200 OK\r\n");
                    ps.print("Content-Length: " + responseLength + "\r\n");
                    ps.print("Content-Type: text/html\r\n");
                    ps.print("\r\n");

                    // Let's write the response contents
                    byte[] data = new byte[responseLength];
                    fis.read(data);
                    out.write(data);
                    fis.close();

                } catch (FileNotFoundException e) {

                    if (req.contains(".html")) {
                        ps.print("HTTP/1.1 404 Not Found \r\n\r\n");
                        System.out.println("404 Not Found: " + req);
                    }
                }

            } else {
                ps.print("HTTP/1.1 400 Bad Request\r\n\r\n");
                System.out.println("400 Bad Request: " + req);
            }

        } catch (IOException e) {
            System.out.println("Generic I/O error " + e);

        } finally {

            try {
                out.close();
                sock.close();
            } catch (IOException ex) {
                System.out.println("I/O error on close" + ex);
            }
        }

        System.out.println("─────────────────────────────────────\n");
    }
}
