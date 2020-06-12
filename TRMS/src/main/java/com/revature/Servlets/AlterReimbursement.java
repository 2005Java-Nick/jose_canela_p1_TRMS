package com.revature.Servlets;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.DAO.ReimbursementDAO;

/**
 * Servlet implementation class AlterReimbursement
 */
//@WebServlet("/alterreimbursement")
public class AlterReimbursement extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlterReimbursement() {
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
		// TODO Auto-generated method stub
		ReimbursementDAO reimbursementDAO = ReimbursementDAO.getInstance();
		double alterAmount =  Math.round(Double.parseDouble(request.getParameter("alterAmount")));
		
		String reason = request.getParameter("reason");
		int id = Integer.parseInt(request.getParameter("alterReimbursementId"));
		
		reimbursementDAO.alterReimbursementAmount(id, alterAmount, reason);
		
		response.sendRedirect("dashboard?view=benefitsCoordinator");
		return;
		
	}

}
