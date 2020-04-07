package com.nawin.tutorial.servlet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AndroidServlet
 */
@WebServlet("/AndroidServlet")
public class AndroidServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AndroidServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void doGet(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        ObjectOutputStream out = new ObjectOutputStream(response
                .getOutputStream());
        Enumeration<String> paramNames = request.getParameterNames();
        String params[]= new String[2];
        int i=0;
        while(paramNames.hasMoreElements()) {
            String paramName = (String)paramNames.nextElement();

            System.out.println(paramName );
            String[] paramValues = request.getParameterValues(paramName);
            params[i] = paramValues[0];

            System.out.println(params[i]);
            i++;

        }

        if(params[0].equals("nawin"))
        {

            if(params[1].equals("shiva"))
            {
                out.writeObject("success");
            }
            else
            {
                out.writeObject("failure");
            }
        }
        else
        {
            out.writeObject("failure"); 
        }
    } 
    public void doPost(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
