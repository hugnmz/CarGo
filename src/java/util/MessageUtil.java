package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import util.exception.ApplicationException;

// class xu ly thong bao va loi tu file properties
public class MessageUtil {
    // properties chua thong bao
    private static Properties messages;
    // properties chua loi
    private static Properties errors;
    
    // khoi tao properties khi class duoc load
    static {
        loadProperties();
    }
    
    // method tai properties tu file
    private static void loadProperties() {
        try {
            // tai file messages.properties
            messages = new Properties();
            InputStream messagesStream = MessageUtil.class.getClassLoader().getResourceAsStream("messages.properties");
            if (messagesStream != null) {
                messages.load(new InputStreamReader(messagesStream, "UTF-8"));
                messagesStream.close();
            }
            
            // tai file errors.properties
            errors = new Properties();
            InputStream errorsStream = MessageUtil.class.getClassLoader().getResourceAsStream("errors.properties");
            if (errorsStream != null) {
                errors.load(new InputStreamReader(errorsStream, "UTF-8"));
                errorsStream.close();
            }
        } catch (IOException e) {
            throw new ApplicationException("error.system.properties.load", e);
        }
    }
    
    // method lay thong bao theo key
    public static String getMessage(String key) {
        return messages.getProperty(key, "Message not found: " + key);
    }
    
    // method lay loi theo key
    public static String getError(String key) {
        return errors.getProperty(key, "Error not found: " + key);
    }
    
    // method lay error message tu exception
    // neu exception message la error key thi lay tu properties
    // nguoc lai tra ve message goc
    public static String getErrorFromException(Exception e) {
        if (e == null || e.getMessage() == null) {
            return getError("error.system");
        }
        
        String message = e.getMessage();
        
        // Neu message bat dau bang "error." thi coi nhu la error key
        if (message.startsWith("error.")) {
            return getError(message);
        }
        
        // Nguoc lai tra ve message goc
        return message;
    }
}