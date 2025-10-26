package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MessageUtil {
    private static Properties messages;
    private static Properties errors;
    
    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        try {
            // Load messages
            messages = new Properties();
            InputStream messagesStream = MessageUtil.class.getClassLoader().getResourceAsStream("messages.properties");
            if (messagesStream != null) {
                messages.load(new java.io.InputStreamReader(messagesStream, "UTF-8"));
                messagesStream.close();
            }
            
            // Load errors
            errors = new Properties();
            InputStream errorsStream = MessageUtil.class.getClassLoader().getResourceAsStream("errors.properties");
            if (errorsStream != null) {
                errors.load(new java.io.InputStreamReader(errorsStream, "UTF-8"));
                errorsStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String getMessage(String key) {
        return messages.getProperty(key, "Message not found: " + key);
    }
    
    public static String getError(String key) {
        return errors.getProperty(key, "Error not found: " + key);
    }
}
