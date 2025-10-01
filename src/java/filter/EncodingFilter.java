/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/*") // áp dụng cho mọi url trong webapp
public class EncodingFilter implements Filter{

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) 
            throws IOException, ServletException {
    
        // ép charset của req alf UTF-8, đảm bảo POST đọc đc tham số đúng
        req.setCharacterEncoding("UTF-8");
        
        // đặt content-type của response là HTML và UTF-8 để response trả ra đúng Unicode
        resp.setContentType("text/html;charset=UTF-8");
        
        //chuyển tiếp sang filter tiếp theo/servlet
        chain.doFilter(req, resp);
    }
    
}
