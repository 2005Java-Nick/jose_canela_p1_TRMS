package com.revature.Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.revature.DAO.ReimbursementDAO;

/**
 * Servlet implementation class UploadGrade
 */
//@WebServlet("/uploadgrade")
@MultipartConfig
public class UploadGrade extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadGrade() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		byte[] grade = IOUtils.toByteArray(request.getPart("grade").getInputStream());
		int id = Integer.parseInt(request.getParameter("id"));
		ReimbursementDAO reimbursementDAO = ReimbursementDAO.getInstance();
		reimbursementDAO.uploadGrade(id, grade);
		response.sendRedirect("dashboard");
		return;
	}

}
