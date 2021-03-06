package com.cnsebe.it.webdemo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wind on 16/8/8.
 */
@WebServlet(name="logServlet", urlPatterns="/login")
public class LoginActionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        String u = request.getParameter("u");
        String p = request.getParameter("p");
        String m = request.getParameter("m");
        response.getWriter().write( JMock.validate_pum(u,p,m));
    }

}