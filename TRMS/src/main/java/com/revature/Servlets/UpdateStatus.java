package com.revature.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.Objects.Employee;
import com.revature.DAO.ReimbursementDAO;

/**
 * Servlet implementation class UpdateStatus
 */
//@WebServlet("/updatestatus")
public class UpdateStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateStatus() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//doGet(request, response);
		ReimbursementDAO reimbursementDAO = ReimbursementDAO.getInstance();
		reimbursementDAO.updateStatus(Integer.parseInt(request.getParameter("reimbursementId")), ((Employee)request.getSession().getAttribute("employee")) ,request.getParameter("approval"), request.getParameter("reason"), request.getParameter("role"));
		if(request.getParameter("role").equals("supervisor")) {
			response.sendRedirect("dashboard?view=supervisor");
		}else if(request.getParameter("role").equals("departmentHead")) {
			response.sendRedirect("dashboard?view=departmentHead");
		}else if(request.getParameter("role").equals("benefitsCoordinator")) {
			response.sendRedirect("dashboard?view=benefitsCoordinator");
		}else {
			response.sendRedirect("dashboard");
		}
		
	}

}
