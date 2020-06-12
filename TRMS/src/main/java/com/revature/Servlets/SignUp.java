package com.revature.Servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.Objects.Employee;
import com.revature.DAO.EmployeeDAO;

/**
 * Servlet implementation class SignUp
 */
//@WebServlet("/signup")
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    //static final Logger logger = LogManager.getLogger("SignUp");

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(request.getSession().getAttribute("employee") == null) {
			RequestDispatcher rd = request.getRequestDispatcher("views/signup.jsp");
			rd.forward(request, response);
		}else {
			response.sendRedirect("dashboard");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		EmployeeDAO employeeDAO = EmployeeDAO.getInstance();
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		Integer reportsTo = Integer.parseInt(request.getParameter("supervisorId"));
		Integer departmentId = Integer.parseInt(request.getParameter("departmentId"));
		
		ArrayList<String> roles = new ArrayList<String>();
		roles.add("Employee");
		if(request.getParameterValues("roles") != null) {
			roles.addAll(Arrays.asList(request.getParameterValues("roles")));
		}
		
		employeeDAO.create(email, password, firstName, lastName, reportsTo, departmentId);
		Employee e = employeeDAO.getEmployee(email, password);
		employeeDAO.setRoles(e, roles.toArray(new String[roles.size()]));
		request.getSession().setAttribute("employee", e);
		//logger.info(e.getEmail() + " signed up.");
		response.sendRedirect("dashboard");
	}

}
