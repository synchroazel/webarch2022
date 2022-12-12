package it.unitn.padalino.assignment4;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

public class ServiceLocator {
    public static <T> T lookup(String jndiName, Properties jndiProps, Class<T> jndiClass) {
        try {
            Context ctx = new InitialContext(jndiProps);
            return (T) ctx.lookup(jndiName);
        } catch (javax.naming.NamingException e) {
            throw new RuntimeException(e);
        }
    }
}

