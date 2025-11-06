package filter.validation;

import constant.DataType;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.validator.routines.BigDecimalValidator;
import org.apache.commons.validator.routines.DateValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.RegexValidator;
import util.MessageUtil;

@WebFilter("/*")
public class ValidationFilter implements Filter {

    private final EmailValidator emailValidator = EmailValidator.getInstance();
    private final RegexValidator phoneValidator = new RegexValidator("^0\\d{9,10}$");
    private final BigDecimalValidator decimalValidator = BigDecimalValidator.getInstance();
    private final DateValidator dateValidator = DateValidator.getInstance();
    private final Map<String, DataType> fieldMap = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) {
        fieldMap.put("email", DataType.EMAIL);
        fieldMap.put("phone", DataType.PHONE);
        fieldMap.put("startDate", DataType.DATE);
        fieldMap.put("endDate", DataType.DATE);
        fieldMap.put("dailyPrice", DataType.DECIMAL);
        fieldMap.put("depositAmount", DataType.DECIMAL);
        fieldMap.put("totalAmount", DataType.DECIMAL);
        fieldMap.put("price", DataType.DECIMAL);
        fieldMap.put("pickupDate", DataType.DATE);
        fieldMap.put("returnDate", DataType.DATE);
        fieldMap.put("dateOfBirth", DataType.DATE);
    }

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) sr;
        List<String> errors = new ArrayList<>();

        for (String name : request.getParameterMap().keySet()) {
            String value = request.getParameter(name);
            DataType type = fieldMap.get(name);

            if (type == null) {
                continue; // Bo qua field khong can validate
            }

            // Bo qua neu value rong (JSP da validate required)
            if (value.isEmpty()) {
                continue;
            }

            String err = validateType(name, value, type);
            if (err != null) {
                errors.add(err);
            }
        }

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);

            // Giu lai gia tri da nhap (tru password)
            for (String paramName : request.getParameterMap().keySet()) {
                if (!paramName.contains("password") && !paramName.contains("Password")) {
                    request.setAttribute(paramName, request.getParameter(paramName));
                }
            }

            String backPage = request.getParameter("_back");
            if (backPage == null || backPage.isEmpty()) {
                backPage = request.getServletPath();
            }

            // Forward ve servlet (servlet phai co doGet())
            request.getRequestDispatcher(backPage).forward(sr, sr1);
            return;
        }

        fc.doFilter(sr, sr1);
    }

    private String validateType(String field, String value, DataType type) {
        switch (type) {
            case EMAIL:
                if (!emailValidator.isValid(value)) {
                    return MessageUtil.getError("error.email.invalid").replace("{0}", value);
                }
                break;
            case PHONE:
                if (!phoneValidator.isValid(value)) {
                    return MessageUtil.getError("error.phone.invalid").replace("{0}", value);
                }
                break;
            case DECIMAL:
                if (decimalValidator.validate(value) == null) {
                    return MessageUtil.getError("error.decimal.invalid");
                }
                break;
            case DATE:
                if (dateValidator.validate(value, "yyyy-MM-dd") == null) {
                    return MessageUtil.getError("error.date.format.invalid");
                }
                break;
            case DATETIME:
                if (dateValidator.validate(value, "yyyy-MM-dd HH:mm:ss") == null) {
                    return MessageUtil.getError("error.datetime.format.invalid");
                }
                break;
        }
        return null;
    }

}
