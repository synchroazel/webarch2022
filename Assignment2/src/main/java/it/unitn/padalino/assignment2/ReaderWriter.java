package it.unitn.padalino.assignment2;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.io.*;
import java.util.Enumeration;

public class ReaderWriter {

    public void serialWriter(ServletContextEvent servletContextEvent, String filename) {
        /* Write every UserBean object in the ServletContext to a file */

        ServletContext ctx = servletContextEvent.getServletContext();

        try {
            FileOutputStream f = new FileOutputStream(new File(filename));
            ObjectOutputStream o = new ObjectOutputStream(f);

            Enumeration allAttributes = ctx.getAttributeNames();

            while (allAttributes.hasMoreElements()) {

                String curAttribute = (String) allAttributes.nextElement();

                if (ctx.getAttribute(curAttribute) instanceof UserBean) {
                    ((UserBean) ctx.getAttribute(curAttribute)).setActive(false);
                    o.writeObject(ctx.getAttribute(curAttribute));
                    System.out.println("[INFO] Successfully written:\n" +
                            ctx.getAttribute(curAttribute));
                }
            }
            o.close();
            f.close();

        } catch (FileNotFoundException e) {
            System.out.println("[ERROR] File not found.");
        } catch (IOException e) {
            System.out.println("[ERROR] Error initializing stream.");
        }
    }

    public void serialReader(ServletContextEvent servletContextEvent, String filename) {
        /* LOAD EVERYTHING FROM A FILE TO THE SERVLET CONTEXT */

        ServletContext ctx = servletContextEvent.getServletContext();

        try {
            FileInputStream fi = new FileInputStream(filename);
            ObjectInputStream oi = new ObjectInputStream(fi);

            boolean cont = true;

            while (cont) {
                UserBean obj = (UserBean) oi.readObject();
                ctx.setAttribute(obj.getUsername(), obj);
                System.out.println("[INFO] Successfully read:\n" +
                        ctx.getAttribute(obj.getUsername()));
                if (obj.equals(null)) {
                    cont = false;
                }
            }
            oi.close();
            fi.close();

        } catch (FileNotFoundException e) {
            System.out.println("[ERROR] File not found.");
        } catch (IOException e) {
            System.out.println("[INFO] End of reading.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}




