package com.revature.Servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.Objects.*;
import com.revature.DAO.*;

/**
 * Servlet implementation class Dashboard
 */
//@WebServlet("/dashboard")
public class Dashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static EmployeeDAO employeeDAO;
	private static ReimbursementDAO reimbursementDAO;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Dashboard() {
        super();
        employeeDAO = EmployeeDAO.getInstance();
        reimbursementDAO = ReimbursementDAO.getInstance();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession().getAttribute("employee") != null) {
			Employee employee = (Employee) request.getSession().getAttribute("employee");
			if(request.getParameter("view") != null && request.getParameter("view").equals("employee") && employee.getRoles().contains("Employee")) {
				
				List<Reimbursement> reimbursements = reimbursementDAO.getAllByEmployee(employee);
				List<EventType> eventTypes = reimbursementDAO.getEventTypes();
				List<GradeFormat> gradeFormats = reimbursementDAO.getGradeFormats();
				request.setAttribute("employee", employee);
				request.setAttribute("reimbursements", reimbursements);
				request.setAttribute("eventTypes", eventTypes);
				request.setAttribute("gradeFormats", gradeFormats);
				RequestDispatcher rd = request.getRequestDispatcher("views/dashboard.jsp");
				rd.forward(request, response);
			}else if(request.getParameter("view") != null && request.getParameter("view").equals("supervisor") && employee.getRoles().contains("Supervisor")) {
				
				List<EmployeeReimbursement> employeeReimbursements = reimbursementDAO.getAllReimbursementsFromUnderlings(employee.getEmployeeId());
				request.setAttribute("employee", employee);
				request.setAttribute("employeeReimbursements", employeeReimbursements);
				RequestDispatcher rd = request.getRequestDispatcher("views/dashboard-supervisor.jsp");
				rd.forward(request, response);
			}else if(request.getParameter("view") != null && request.getParameter("view").equals("departmentHead") && employee.getRoles().contains("Department Head")) {
				
				List<EmployeeReimbursement> employeeReimbursements = reimbursementDAO.getAllReimbursementsFromDepartment(employee.getDepartmentId());
				request.setAttribute("employee", employee);
				request.setAttribute("employeeReimbursements", employeeReimbursements);
				RequestDispatcher rd = request.getRequestDispatcher("views/dashboard-departmenthead.jsp");
				rd.forward(request, response);
			}else if(request.getParameter("view") != null && request.getParameter("view").equals("benefitsCoordinator") && employee.getRoles().contains("Benefits Coordinator")) {
				List<EmployeeReimbursement> employeeReimbursements = reimbursementDAO.getAllReimbursementsForBenCo();
				request.setAttribute("employee", employee);
				request.setAttribute("employeeReimbursements", employeeReimbursements);
				RequestDispatcher rd = request.getRequestDispatcher("views/dashboard-benco.jsp");
				rd.forward(request, response);
			}else{
				
				List<Reimbursement> reimbursements = reimbursementDAO.getAllByEmployee(employee);
				List<EventType> eventTypes = reimbursementDAO.getEventTypes();
				List<GradeFormat> gradeFormats = reimbursementDAO.getGradeFormats();
				request.setAttribute("employee", employee);
				request.setAttribute("reimbursements", reimbursements);
				request.setAttribute("eventTypes", eventTypes);
				request.setAttribute("gradeFormats", gradeFormats);
				RequestDispatcher rd = request.getRequestDispatcher("views/dashboard.jsp");
				rd.forward(request, response);
			}
		}else {
			response.sendRedirect("login");
		}
		

		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//doGet(request, response);
		
	}

}
