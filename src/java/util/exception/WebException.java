/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.exception;

/**
 *
 * @author admin
 */
public class WebException {
    // Base

    public class AppException extends RuntimeException {

        public AppException(String message) {
            super(message);
        }

        public AppException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public class ValidationException extends AppException {

        public ValidationException(String message) {
            super(message);
        }
    }

    public class BusinessException extends AppException {

        public BusinessException(String message) {
            super(message);
        }
    }

    public class DataAccessException extends AppException {

        public DataAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
