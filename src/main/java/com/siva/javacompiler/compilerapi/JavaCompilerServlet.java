package com.siva.javacompiler.compilerapi;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.siva.javacompiler.codecompiler.CodeCompiler;

import org.json.simple.JSONObject;

/**
 * Servlet implementation class JavaCompiler
 */
@WebServlet("/JavaCompiler")
public class JavaCompilerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public JavaCompilerServlet() {
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("hai..");
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		StringBuilder requestBody = new StringBuilder();
		BufferedReader br = request.getReader();
		String line;
		while((line=br.readLine())!=null)
		{
			requestBody.append(line);
		}
		System.out.println(requestBody);
		br.close();
		String requestBodyString = requestBody.toString().replace("&gt;", ">").replace("&lt;", "<");
		JSONParser parser = new JSONParser();
		JSONObject jsonObj = null;
		try {
			jsonObj = (JSONObject) parser.parse(requestBodyString);
			CodeCompiler compiler = new CodeCompiler();
            String filePath = getServletContext().getRealPath("/WEB-INF");
            JSONObject output = compiler.run(filePath,jsonObj);
            response.getWriter().write(output.toJSONString());
		} 
		catch (ParseException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
