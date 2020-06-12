package com.revature.Servlets;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Date;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.revature.Objects.*;
import com.revature.DAO.ReimbursementDAO;

/**
 * Servlet implementation class NewReimbursement
 */
//@WebServlet("/newreimbursement")
@MultipartConfig
public class NewReimbursement extends HttpServlet {
	private static final long serialVersionUID = 1L;
	byte[] attachment;
	byte[] approvalDocument;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewReimbursement() {
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
		JSONObject json = new JSONObject();
		ReimbursementDAO reimbursementDAO = ReimbursementDAO.getInstance();
		Employee employee = (Employee) request.getSession().getAttribute("employee");
		DateFormat dateFormat = DateFormat.getDateInstance();
		double cost = Double.parseDouble(request.getParameter("cost"));
		Integer timeMissed;
		try{
			timeMissed = Integer.parseInt(request.getParameter("timeMissed"));
		} catch (NumberFormatException e) {
			timeMissed = 0;
		}
		String[] date = request.getParameter("startDate").split("-");

		Calendar calendar = new GregorianCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]) -1 , Integer.parseInt(date[2]));
		//attachment
		byte[] attachment = IOUtils.toByteArray(request.getPart("attachment").getInputStream());
		
		//approval document
		byte[] approvalDocument = IOUtils.toByteArray(request.getPart("approvalDocument").getInputStream());		
		
		Date startDate = new Date(calendar.getTimeInMillis());
		Date nowDate = new Date(new java.util.Date().getTime());
		if(startDate.before(nowDate)) {
			json.put("ERROR", "Unable to submit reimbursement form. The start date has passed.");
			response.getWriter().print(json.toString());
			return;
		}
		
		long difference = TimeUnit.MILLISECONDS.toDays(startDate.getTime() - nowDate.getTime());
		
		if(difference < 7) {
			json.put("ERROR", "Unable to submit reimbursement form. Cannot create reimbursement for an event that will happen within a week.");
			response.getWriter().print(json.toString());
			return;
		}
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
		
		String passingGradeSelection = request.getParameter("passingGradeSelection");
		String passingGrade;
		if(passingGradeSelection.equals("CUSTOM")) {
			passingGrade = request.getParameter("passingGrade");
		}else {
			passingGrade = "DEFAULT";
		}
		
		int pk = reimbursementDAO.create(employee, request.getParameter("description"), cost, request.getParameter("gradeFormat"), request.getParameter("eventType"), request.getParameter("workJustification"), attachment, approvalDocument, timeMissed, startDate, request.getParameter("address"), request.getParameter("city"), request.getParameter("zip"), request.getParameter("country"), passingGrade);
		Reimbursement reimbursement = reimbursementDAO.getById(pk);
		json.put("reimbursementId", reimbursement.getReimbursementId());
		json.put("status", reimbursement.getStatus());
		json.put("coverage", reimbursement.getCoverage());
		json.put("cost", currencyFormat.format(reimbursement.getCost()));
		json.put("projectedCost", currencyFormat.format(reimbursement.getCost() * reimbursement.getCoverage()));
		json.put("employeeCreationTime", reimbursement.getEmployeeCreationTime().toLocaleString()); //DateFormat.format(Date date)
		json.put("eventType", reimbursement.getEventType());
		json.put("gradeFormat", reimbursement.getFormat());
		json.put("customPassingGrade", reimbursement.getCustomPassingGrade());
		json.put("defaultPassingGrade", reimbursement.getDefaultPassingGrade());
		json.put("startDate", startDate.toLocalDate().toString());
		if(reimbursement.getSupervisorApproveDate() == null) {
			json.put("supervisorApprovalDate", "--");
		}else {
			json.put("supervisorApprovalDate", reimbursement.getSupervisorApproveDate().toLocaleString()); //DateFormat.format(Date date)
		}
		if(reimbursement.getDepartmentHeadApproveDate() == null) {
			json.put("departmentHeadApprovalDate", "--");
		}else {
			json.put("departmentHeadApprovalDate", reimbursement.getDepartmentHeadApproveDate().toLocaleString()); //DateFormat.format(Date date)
		}
		if(reimbursement.getAdjustedCost() == 0) {
			json.put("adjustedReimbursement", "--");
		}else {
			json.put("adjustedReimbursement", reimbursement.getAdjustedCost());
		}
		
		json.put("description", reimbursement.getDescription());
		json.put("workJustification", reimbursement.getWorkJustification());
		json.put("timeMissed", reimbursement.getTimeMissed());
		json.put("address", reimbursement.getAddress());
		json.put("city", reimbursement.getCity());
		json.put("zip", reimbursement.getZip());
		json.put("country", reimbursement.getCountry());
		if(reimbursement.getDenyReason() == null) {
			json.put("denyReason", "--");
		}else {
			json.put("denyReason", reimbursement.getDenyReason());
		}
		if(reimbursement.getInflatedReimbursementReason() == null) {
			json.put("inflatedReimbursementReason", "--");
		}else {
			json.put("inflatedReimbursementReason", reimbursement.getInflatedReimbursementReason());
		}
		
		response.getWriter().print(json.toString());
	}

}
