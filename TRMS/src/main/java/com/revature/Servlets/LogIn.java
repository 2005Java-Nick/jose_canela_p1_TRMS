package com.revature.Servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.Objects.Employee;
import com.revature.DAO.*;

/**
 * Servlet implementation class LogIn
 */
//@WebServlet("/login")
public class LogIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	//static final Logger logger = LogManager.getLogger("LogIn");

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		if(request.getSession().getAttribute("employee") != null) {
			response.sendRedirect("dashboard");
			return;
		}else {
			RequestDispatcher rd = request.getRequestDispatcher("views/login.jsp");
			rd.forward(request, response);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String email = (String) request.getParameter("email");
		String password = (String) request.getParameter("password");
		EmployeeDAO employeeDAO = EmployeeDAO.getInstance();
		Employee e = employeeDAO.getEmployee(email, password);
		if(e == null) {
			doGet(request, response);
		}else {
			//logger.info(e.getEmail() + " logged in.");
			request.getSession().setAttribute("employee", e);
			response.sendRedirect("dashboard");
		}
		
	}

}
