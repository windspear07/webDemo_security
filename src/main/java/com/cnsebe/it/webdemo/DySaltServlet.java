package com.cnsebe.it.webdemo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wind on 16/8/18.
 * 根据参数u返回动态salt值。动态salt的有效期不要超过5min。
 */
@WebServlet(name="saltServlet", urlPatterns="/salt")
public class DySaltServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("u");
        response.getWriter().write( JMock.gen_salt(user) );
    }

}