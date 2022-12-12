package it.unitn.padalino.assignment4;

import it.unitn.padalino.assignment4remote.StudentDTO;
import it.unitn.padalino.assignment4remote.ejb.StudentHandlerBeanIf;

import javax.naming.Context;
import java.util.HashMap;
import java.util.Properties;

public class BusinessDelegate {

    private static StudentHandlerBeanIf studentHandler = null;

    static HashMap<Integer, StudentDTO> cache = new HashMap<Integer, StudentDTO>();

    private void getStudentHandler() {

        String jndiName = "ejb:/assignment4remote-1.0-SNAPSHOT/StudentHandlerBean!it.unitn.padalino.assignment4remote.ejb.StudentHandlerBeanIf";

        Properties jndiProps = new Properties();
        jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        jndiProps.put(Context.PROVIDER_URL, "remote+http://127.0.0.1:8080");
        jndiProps.put(Context.SECURITY_PRINCIPAL, "admin");
        jndiProps.put(Context.SECURITY_CREDENTIALS, "admin");

        studentHandler = ServiceLocator.lookup(jndiName, jndiProps, StudentHandlerBeanIf.class);

    }

    public static StudentDTO getStudent(int i) {

        if (studentHandler == null) {
            System.out.println("[INFO] StudentHandler needed for the first time. Creating it...");
            new BusinessDelegate().getStudentHandler();
        }

        if (cache.containsKey(i)) {
            System.out.println("[INFO] Returning cached SudentDTO for student #" + i);
            return cache.get(i);

        } else {
            StudentDTO student = studentHandler.getStudent(i);
            System.out.println("[INFO] Returning new SudentDTO for student #" + i);
            cache.put(i, student);
            System.out.println("[INFO] Cached SudentDTO for student #" + i);
            return student;
        }

    }


}
