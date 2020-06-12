package com.revature.DAO;

import java.io.IOException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.Objects.*;
import com.revature.Util.ConnectionFactory;

//import oracle.jdbc.OracleTypes;

/**
 * Handles the operations between the server and the database for Reimbursement-related functions.
 *
 */
public class ReimbursementDAO {
	private static Logger log = Logger.getRootLogger();
	private static ReimbursementDAO reimbursementDAO;
	
	private ReimbursementDAO() {
		
	}
	
	/**
	 * Get the singleton instance of the reimbursementDAO. If it doesn't exist, it will instantiate it and return that.
	 * @return The singleton instance of the ReimbursementDAO.
	 */
	public static ReimbursementDAO getInstance() {
		if(reimbursementDAO == null) {
			reimbursementDAO = new ReimbursementDAO();
			return reimbursementDAO;
		}else {
			return reimbursementDAO;
		}
	}
	
	/**
	 * Creates a reimbursement for the specified employee.
	 * @param em The employe that submitted the reimbursement.
	 * @param description The description of the reimbursement.
	 * @param cost The cost of the event.
	 * @param gradeFormat The grading format for the event.
	 * @param eventType The event type.
	 * @param workJustification The reason to take the event.
	 * @param attachment An event-related attachement
	 * @param approvalDocument An approval document
	 * @param timeMissed The expected time missed for this event.
	 * @param startDate The start date of the event.
	 * @param address The address of the event.
	 * @param city The city of the event.
	 * @param zip The zip of the event.
	 * @param country The country of the event.
	 * @param passingGrade The passing grade of the event.
	 * @return The id of the row that represents the reimbursement.
	 */
	public int create(Employee em, String description, double cost, String gradeFormat, String eventType, String workJustification, byte[] attachment, byte[] approvalDocument, int timeMissed, java.util.Date startDate, String address, String city, String zip, String country, String passingGrade) {
		
		String sql = "call sp_insert_reimbursement(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = ConnectionFactory.getConnection();
		CallableStatement cs = null;
		int reimbursementId = 0;
		try{
			int approvalProcessId = createApprovalProcess();
			int reimbursementLocationId = createReimbursementLocation(startDate, address, city, zip, country);
			
			cs = conn.prepareCall(sql);
			cs.setInt(1, em.getEmployeeId());
			cs.setInt(2, approvalProcessId);
			cs.setInt(3, reimbursementLocationId);
			cs.setString(4, description);
			cs.setDouble(5, cost);
			cs.setString(6, gradeFormat);
			cs.setString(7, eventType);
			cs.setString(8, workJustification);
			if(attachment == null || attachment.length == 0) {
				cs.setNull(9, Types.BINARY);
			}else {
				cs.setBytes(9, attachment);
			}
			
			if(approvalDocument == null || approvalDocument.length == 0) {
				cs.setNull(10, Types.BINARY);
			}else {
				cs.setBytes(10, approvalDocument);
			}
			
			cs.setInt(11, timeMissed);
			
			cs.setString(12, passingGrade);
			
			cs.registerOutParameter(13, Types.BIGINT);
			
			cs.execute();
			
			reimbursementId = cs.getInt(13);
			
			
		} catch (SQLException e) {
			
			log.trace(e.getMessage());

		} finally {
			try {
				conn.close();
				
				log.trace("Connection Closed");
			} catch (SQLException e) {
			
				log.trace(e.getMessage());
			}
		}
		
		return reimbursementId;
	}
	
	/**
	 * Creates an Approval Process record that will contained the creation time and approve dates =.
	 * @return The id of the approval process record.
	 */
	private int createApprovalProcess() {
		
		String sql = "call sp_insert_approvalprocess(?, ?, ?)";
		CallableStatement cs = null;
		Connection conn = ConnectionFactory.getConnection();
		java.util.Date date = new java.util.Date();
		int approvalProcessId = 0;
		try{
			
			cs = conn.prepareCall(sql);
			cs.setDate(1, new Date(date.getTime()));
			cs.setTimestamp(2, new Timestamp(date.getTime()));
			cs.registerOutParameter(3, Types.BIGINT);
			cs.execute();
			
			approvalProcessId = cs.getInt(3);
			
			cs.close();
		} catch (SQLException e) {
			
			log.trace(e.getMessage());

		} finally {
			try {
				conn.close();
				
				log.trace("Connection Closed");
			} catch (SQLException e) {
			
				log.trace(e.getMessage());
			}
		}
		
		return approvalProcessId;
		
	}
	
	/**
	 * Creates a reimbursement location record.
	 * @param startDate The start date of the event.
	 * @param address The address of the event.
	 * @param city The city of the event.
	 * @param zip The zip of the event.
	 * @param country The country of the event.
	 * @return The id of the reimbursement location record.
	 */
	private int createReimbursementLocation(java.util.Date startDate, String address, String city, String zip, String country) {
		String sql = "call sp_insert_reimlocation(?, ?, ?, ?, ?, ?)";
		
		CallableStatement cs = null;
		Connection conn = ConnectionFactory.getConnection();
		int reimbursementLocationId = 0;
		try{
			
			cs = conn.prepareCall(sql);
			cs.setDate(1, new Date(startDate.getTime()));
			cs.setString(2, address);
			cs.setString(3, city);
			if(zip == null || zip.equals("")) {
				cs.setNull(4, Types.VARCHAR);
			}else {
				cs.setString(4, zip);
			}
			cs.setString(5, country);
			cs.registerOutParameter(6, Types.BIGINT);
			cs.execute();
			
			reimbursementLocationId = cs.getInt(6);
			
			
			
		} catch (SQLException e) {
			
			log.trace(e.getMessage());

		} finally {
			try {
				conn.close();
				
				log.trace("Connection Closed");
			} catch (SQLException e) {
			
				log.trace(e.getMessage());
			}
		}
		
		return reimbursementLocationId;
	}
	
	/**
	 * Returns all reimbursements for the specified employee.
	 * @param em The employee to get all reimbursements for.
	 * @return A list of reimbursements belonging to an employee.
	 */
	public List<Reimbursement> getAllByEmployee(Employee em){
		/* EXAMPLE OF HOW TO GET RESULTSET FROM POSTGRES REFCURSORS USING JDBC
		 * 
		 // Setup function to call.
		Statement stmt = conn.createStatement();
		stmt.execute("CREATE OR REPLACE FUNCTION refcursorfunc() RETURNS refcursor AS '"
				+ " DECLARE "
				+ "    mycurs refcursor; "
				+ " BEGIN "
				+ "    OPEN mycurs FOR SELECT 1 UNION SELECT 2; "
				+ "    RETURN mycurs; "
				+ " END;' language plpgsql");
		stmt.close();

		// We must be inside a transaction for cursors to work.
		conn.setAutoCommit(false);

		// Procedure call.
		CallableStatement proc = conn.prepareCall("{ ? = call refcursorfunc() }");
		proc.registerOutParameter(1, Types.OTHER);
		proc.execute();
		ResultSet results = (ResultSet) proc.getObject(1);
		while (results.next()) {
		    // do something with the results...
		}
		results.close();
		proc.close();
		
		 */
		String getAllSQL = "{call sp_select_reimbursement (?, ?)}";
		List<Reimbursement> list = new ArrayList<Reimbursement>();
		Reimbursement reimbursement = null;
		Connection conn = ConnectionFactory.getConnection();
		CallableStatement cs = null;
		ResultSet rs = null;
		try{
			
			cs = conn.prepareCall(getAllSQL);
			cs.setInt(1, em.getEmployeeId());
			cs.registerOutParameter(2, Types.REF_CURSOR);
			
			cs.execute();
			
			rs = (ResultSet) cs.getObject(2);
			
			while(rs.next()) {
				
				reimbursement = new Reimbursement();
				reimbursement.setReimbursementId(rs.getInt("reimbursementid"));
				reimbursement.setEmployeeId(rs.getInt("employeeid"));
				reimbursement.setSupervisorId(rs.getInt("supervisorid"));
				reimbursement.setDepartmentHeadId(rs.getInt("departmentheadid"));
				reimbursement.setBenCoId(rs.getInt("bencoid"));
				reimbursement.setApprovalProcessId(rs.getInt("approvalprocessid"));
				reimbursement.setEmployeeCreationDate(rs.getDate("employeecreationdate"));
				reimbursement.setEmployeeCreationTime(rs.getTimestamp("employeecreationtime"));
				reimbursement.setSupervisorApproveDate(rs.getDate("supervisorapprovedate"));
				reimbursement.setDepartmentHeadApproveDate(rs.getDate("departmentheadapprovedate"));
				reimbursement.setReimbursementLocationId(rs.getInt("reimbursementlocationid"));
				reimbursement.setStartDate(rs.getDate("startdate"));
				reimbursement.setAddress(rs.getString("address"));
				reimbursement.setCity(rs.getString("city"));
				reimbursement.setZip(rs.getString("zip"));
				reimbursement.setCountry(rs.getString("country"));
				reimbursement.setDescription(rs.getString("description"));
				reimbursement.setCost(rs.getDouble("cost"));
				reimbursement.setAdjustedCost(rs.getDouble("adjustedcost"));
				reimbursement.setGradeFormatId(rs.getInt("gradeformatid"));
				reimbursement.setFormat(rs.getString("format"));
				reimbursement.setCustomPassingGrade(rs.getString("custompassinggrade"));
				reimbursement.setDefaultPassingGrade(rs.getString("defaultpassinggrade"));
				reimbursement.setEventTypeId(rs.getInt("eventtypeid"));
				reimbursement.setEventType(rs.getString("eventtype"));
				reimbursement.setCoverage(rs.getDouble("coverage"));
				reimbursement.setWorkJustification(rs.getString("workjustification"));
				reimbursement.setAttachment(rs.getBlob("attachment"));
				reimbursement.setApprovalDocument(rs.getBlob("approvaldocument"));
				reimbursement.setApprovalId(rs.getInt("approvalid"));
				reimbursement.setStatus(rs.getString("status"));
				reimbursement.setTimeMissed(rs.getInt("timemissed"));
				reimbursement.setDenyReason(rs.getString("denyreason"));
				reimbursement.setInflatedReimbursementReason(rs.getString("inflatedreimbursementreason"));
				reimbursement.setSupervisorId(rs.getInt("supervisorid"));
				reimbursement.setSupervisorEmail(rs.getString("supervisoremail"));
				reimbursement.setSupervisorFirstName(rs.getString("supervisorfirstname"));
				reimbursement.setSupervisorLastName(rs.getString("supervisorlastname"));
				reimbursement.setDepartmentHeadId(rs.getInt("departmentheadid"));
				reimbursement.setDepartmentHeadEmail(rs.getString("departmentheademail"));
				reimbursement.setDepartmentHeadFirstName(rs.getString("departmentheadfirstname"));
				reimbursement.setDepartmentHeadLastName(rs.getString("departmentheadlastname"));
				reimbursement.setGrade(rs.getBlob("grade"));
				list.add(reimbursement);
			}
			
			
		} catch (SQLException e) {
			
			log.trace(e.getMessage());

		} finally {
			try {
				conn.close();
				
				log.trace("Connection Closed");
			} catch (SQLException e) {
			
				log.trace(e.getMessage());
			}
		}
		
		return list;
	}
	
	/**
	 * Returns a list of all the eventypes available.
	 * @return A list of all event types.
	 */
	public List<EventType> getEventTypes(){
		List<EventType> events = new ArrayList<EventType>();
		String sql = "SELECT * FROM eventtype ORDER BY eventtypeid";
		EventType eventType = null;
		Statement statement = null;
		ResultSet rs = null;
		Connection conn = ConnectionFactory.getConnection();
		try{
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			
			while(rs.next()) {
				eventType = new EventType();
				eventType.setEventTypeId(rs.getInt("eventtypeid"));
				eventType.setEventType(rs.getString("eventtype"));
				eventType.setCoverage(rs.getDouble("coverage"));
				events.add(eventType);
			}
			
			statement.close();
			rs.close();
		} catch (SQLException e) {
			
			log.trace(e.getMessage());

		} finally {
			try {
				conn.close();
				
				log.trace("Connection Closed");
			} catch (SQLException e) {
			
				log.trace(e.getMessage());
			}
		}
		return events;
	}

	/**
	 * Returns all grade formats.
	 * @return A list of grade types.
	 */
	public List<GradeFormat> getGradeFormats(){
		List<GradeFormat> gradeFormats = new ArrayList<GradeFormat>();
		String sql = "SELECT * FROM gradeformat ORDER BY gradeformatid";
		GradeFormat gradeFormat = null;
		Statement statement = null;
		ResultSet rs = null;
		Connection conn = ConnectionFactory.getConnection();
		try{
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			
			while(rs.next()) {
				gradeFormat = new GradeFormat();
				gradeFormat.setGradeFormatId(rs.getInt("gradeformatid"));
				gradeFormat.setFormat(rs.getString("format"));
				gradeFormat.setDefaultPassingGrade(rs.getString("defaultpassinggrade"));
				gradeFormats.add(gradeFormat);
			}
			
			statement.close();
			rs.close();
		} catch (SQLException e) {
			
			log.trace(e.getMessage());

		} finally {
			try {
				conn.close();
				
				log.trace("Connection Closed");
			} catch (SQLException e) {
			
				log.trace(e.getMessage());
			}
		}
		return gradeFormats;
	}
	
	/**
	 * Get a reimbursement by id.
	 * @param id The id of a reimbursement.
	 * @return A reimbursement object. 
	 */
	public Reimbursement getById(int id) {
		Reimbursement reimbursement = new Reimbursement();
		String sql = "{call sp_select_one_reimbursement (?, ?)}";
		Connection conn = ConnectionFactory.getConnection();
		CallableStatement cs = null;
		ResultSet rs = null;
		try{
			cs = conn.prepareCall(sql);
			cs.setInt(1, id);
			cs.registerOutParameter(2, Types.REF_CURSOR);
			cs.execute();
			rs = (ResultSet) cs.getObject(2);
			while(rs.next()) {
				reimbursement.setReimbursementId(rs.getInt("reimbursementid"));
				reimbursement.setEmployeeId(rs.getInt("employeeid"));
				reimbursement.setSupervisorId(rs.getInt("supervisorid"));
				reimbursement.setDepartmentHeadId(rs.getInt("departmentheadid"));
				reimbursement.setBenCoId(rs.getInt("bencoid"));
				reimbursement.setApprovalProcessId(rs.getInt("approvalprocessid"));
				reimbursement.setEmployeeCreationDate(rs.getDate("employeecreationdate"));
				reimbursement.setEmployeeCreationTime(rs.getTimestamp("employeecreationtime"));
				reimbursement.setSupervisorApproveDate(rs.getDate("supervisorapprovedate"));
				reimbursement.setDepartmentHeadApproveDate(rs.getDate("departmentheadapprovedate"));
				reimbursement.setReimbursementLocationId(rs.getInt("reimbursementlocationid"));
				reimbursement.setStartDate(rs.getDate("startdate"));
				reimbursement.setAddress(rs.getString("address"));
				reimbursement.setCity(rs.getString("city"));
				reimbursement.setZip(rs.getString("zip"));
				reimbursement.setCountry(rs.getString("country"));
				reimbursement.setDescription(rs.getString("description"));
				reimbursement.setCost(rs.getDouble("cost"));
				reimbursement.setAdjustedCost(rs.getDouble("adjustedcost"));
				reimbursement.setGradeFormatId(rs.getInt("gradeformatid"));
				reimbursement.setFormat(rs.getString("format"));
				reimbursement.setCustomPassingGrade(rs.getString("custompassinggrade"));
				reimbursement.setDefaultPassingGrade(rs.getString("defaultpassinggrade"));
				reimbursement.setEventTypeId(rs.getInt("eventtypeid"));
				reimbursement.setEventType(rs.getString("eventtype"));
				reimbursement.setCoverage(rs.getDouble("coverage"));
				reimbursement.setWorkJustification(rs.getString("workjustification"));
				reimbursement.setAttachment(rs.getBlob("attachment"));
				reimbursement.setApprovalDocument(rs.getBlob("approvaldocument"));
				reimbursement.setApprovalId(rs.getInt("approvalid"));
				reimbursement.setStatus(rs.getString("status"));
				reimbursement.setTimeMissed(rs.getInt("timemissed"));
				reimbursement.setDenyReason(rs.getString("denyreason"));
				reimbursement.setInflatedReimbursementReason(rs.getString("inflatedreimbursementreason"));
				reimbursement.setSupervisorId(rs.getInt("supervisorid"));
				reimbursement.setSupervisorEmail(rs.getString("supervisoremail"));
				reimbursement.setSupervisorFirstName(rs.getString("supervisorfirstname"));
				reimbursement.setSupervisorLastName(rs.getString("supervisorlastname"));
				reimbursement.setDepartmentHeadId(rs.getInt("departmentheadid"));
				reimbursement.setDepartmentHeadEmail(rs.getString("departmentheademail"));
				reimbursement.setDepartmentHeadFirstName(rs.getString("departmentheadfirstname"));
				reimbursement.setDepartmentHeadLastName(rs.getString("departmentheadlastname"));
				reimbursement.setGrade(rs.getBlob("grade"));
			}
			
			rs.close();
			cs.close();
			
		} catch (SQLException e) {
			
			log.trace(e.getMessage());

		} finally {
			try {
				conn.close();
				
				log.trace("Connection Closed");
			} catch (SQLException e) {
			
				log.trace(e.getMessage());
			}
		}
		
		if(reimbursement.getReimbursementId() == 0) {
			return null;
		}else {
			return reimbursement;
		}
	}

	/**
	 * The supervisor method to get all reimbursements of employees who reports to him/her.
	 * @param id The id of the supervisor.
	 * @return A list of all reimbursements of all reimbursements of employees who reports to him/her.
	 */
	public List<EmployeeReimbursement> getAllReimbursementsFromUnderlings(int id){
		List<EmployeeReimbursement> list = new ArrayList<EmployeeReimbursement>();
		String sql = "{call sp_select_all_underling_reim (?, ?)}";
		CallableStatement cs = null;
		ResultSet rs = null;
		Employee employee = null;
		Reimbursement reimbursement = null;
		EmployeeReimbursement employeeReimbursement = null;
		Connection conn = ConnectionFactory.getConnection();
		try{
			
			cs = conn.prepareCall(sql);
			cs.setInt(1, id);
			cs.registerOutParameter(2, Types.REF_CURSOR);
			
			cs.execute();
			
			rs = (ResultSet) cs.getObject(2);
			while(rs.next()) {
				employee = new Employee();
				reimbursement = new Reimbursement();
				reimbursement.setReimbursementId(rs.getInt("reimbursementid"));
				reimbursement.setEmployeeId(rs.getInt("employeeid"));
				reimbursement.setSupervisorId(rs.getInt("supervisorid"));
				reimbursement.setDepartmentHeadId(rs.getInt("departmentheadid"));
				reimbursement.setBenCoId(rs.getInt("bencoid"));
				reimbursement.setApprovalProcessId(rs.getInt("approvalprocessid"));
				reimbursement.setEmployeeCreationDate(rs.getDate("employeecreationdate"));
				reimbursement.setEmployeeCreationTime(rs.getTimestamp("employeecreationtime"));
				reimbursement.setSupervisorApproveDate(rs.getDate("supervisorapprovedate"));
				reimbursement.setDepartmentHeadApproveDate(rs.getDate("departmentheadapprovedate"));
				reimbursement.setReimbursementLocationId(rs.getInt("reimbursementlocationid"));
				reimbursement.setStartDate(rs.getDate("startdate"));
				reimbursement.setAddress(rs.getString("address"));
				reimbursement.setCity(rs.getString("city"));
				reimbursement.setZip(rs.getString("zip"));
				reimbursement.setCountry(rs.getString("country"));
				reimbursement.setDescription(rs.getString("description"));
				reimbursement.setCost(rs.getDouble("cost"));
				reimbursement.setAdjustedCost(rs.getDouble("adjustedcost"));
				reimbursement.setGradeFormatId(rs.getInt("gradeformatid"));
				reimbursement.setFormat(rs.getString("format"));
				reimbursement.setCustomPassingGrade(rs.getString("custompassinggrade"));
				reimbursement.setDefaultPassingGrade(rs.getString("defaultpassinggrade"));
				reimbursement.setEventTypeId(rs.getInt("eventtypeid"));
				reimbursement.setEventType(rs.getString("eventtype"));
				reimbursement.setCoverage(rs.getDouble("coverage"));
				reimbursement.setWorkJustification(rs.getString("workjustification"));
				reimbursement.setAttachment(rs.getBlob("attachment"));
				reimbursement.setApprovalDocument(rs.getBlob("approvaldocument"));
				reimbursement.setApprovalId(rs.getInt("approvalid"));
				reimbursement.setStatus(rs.getString("status"));
				reimbursement.setTimeMissed(rs.getInt("timemissed"));
				reimbursement.setDenyReason(rs.getString("denyreason"));
				reimbursement.setInflatedReimbursementReason(rs.getString("inflatedreimbursementreason"));
				reimbursement.setSupervisorId(rs.getInt("supervisorid"));
				reimbursement.setSupervisorEmail(rs.getString("supervisoremail"));
				reimbursement.setSupervisorFirstName(rs.getString("supervisorfirstname"));
				reimbursement.setSupervisorLastName(rs.getString("supervisorlastname"));
				reimbursement.setDepartmentHeadId(rs.getInt("departmentheadid"));
				reimbursement.setDepartmentHeadEmail(rs.getString("departmentheademail"));
				reimbursement.setDepartmentHeadFirstName(rs.getString("departmentheadfirstname"));
				reimbursement.setDepartmentHeadLastName(rs.getString("departmentheadlastname"));
				reimbursement.setGrade(rs.getBlob("grade"));
				employee.setEmployeeId(rs.getInt("employeeid"));
				employee.setEmail(rs.getString("email"));
				employee.setFirstName(rs.getString("firstname"));
				employee.setLastName(rs.getString("lastname"));
				employee.setReportsTo(rs.getInt("reportsto"));
				employee.setDepartmentId(rs.getInt("departmentid"));
				
				employeeReimbursement = new EmployeeReimbursement();
				employeeReimbursement.setReimbursement(reimbursement);
				employeeReimbursement.setEmployee(employee);
				list.add(employeeReimbursement);
				
			}
			
			
			rs.close();
			cs.close();
		} catch (SQLException e) {
			
			log.trace(e.getMessage());

		} finally {
			try {
				conn.close();
				
				log.trace("Connection Closed");
			} catch (SQLException e) {
			
				log.trace(e.getMessage());
			}
		}
		

		return list;

		
	}
	
	/**
	 * Gets all the reimbursements in a specific department
	 * @param departmentId The id of the department.
	 * @return A list of reimbursements in a specified department.
	 */
	public List<EmployeeReimbursement> getAllReimbursementsFromDepartment(int departmentId) {
		List<EmployeeReimbursement> list = new ArrayList<EmployeeReimbursement>();
		String sql = "{call sp_select_all_department (?, ?)}";
		CallableStatement cs = null;
		ResultSet rs = null;
		Employee employee = null;
		Reimbursement reimbursement = null;
		EmployeeReimbursement employeeReimbursement = null;
		Connection conn = ConnectionFactory.getConnection();
		try{
			
			cs = conn.prepareCall(sql);
			cs.setInt(1, departmentId);
			cs.registerOutParameter(2, Types.REF_CURSOR);
			
			cs.execute();
			
			rs = (ResultSet) cs.getObject(2);
			while(rs.next()) {
				employee = new Employee();
				reimbursement = new Reimbursement();
				reimbursement.setReimbursementId(rs.getInt("reimbursementid"));
				reimbursement.setEmployeeId(rs.getInt("employeeid"));
				reimbursement.setSupervisorId(rs.getInt("supervisorid"));
				reimbursement.setDepartmentHeadId(rs.getInt("departmentheadid"));
				reimbursement.setBenCoId(rs.getInt("bencoid"));
				reimbursement.setApprovalProcessId(rs.getInt("approvalprocessid"));
				reimbursement.setEmployeeCreationDate(rs.getDate("employeecreationdate"));
				reimbursement.setEmployeeCreationTime(rs.getTimestamp("employeecreationtime"));
				reimbursement.setSupervisorApproveDate(rs.getDate("supervisorapprovedate"));
				reimbursement.setDepartmentHeadApproveDate(rs.getDate("departmentheadapprovedate"));
				reimbursement.setReimbursementLocationId(rs.getInt("reimbursementlocationid"));
				reimbursement.setStartDate(rs.getDate("startdate"));
				reimbursement.setAddress(rs.getString("address"));
				reimbursement.setCity(rs.getString("city"));
				reimbursement.setZip(rs.getString("zip"));
				reimbursement.setCountry(rs.getString("country"));
				reimbursement.setDescription(rs.getString("description"));
				reimbursement.setCost(rs.getDouble("cost"));
				reimbursement.setAdjustedCost(rs.getDouble("adjustedcost"));
				reimbursement.setGradeFormatId(rs.getInt("gradeformatid"));
				reimbursement.setFormat(rs.getString("format"));
				reimbursement.setCustomPassingGrade(rs.getString("custompassinggrade"));
				reimbursement.setDefaultPassingGrade(rs.getString("defaultpassinggrade"));
				reimbursement.setEventTypeId(rs.getInt("eventtypeid"));
				reimbursement.setEventType(rs.getString("eventtype"));
				reimbursement.setCoverage(rs.getDouble("coverage"));
				reimbursement.setWorkJustification(rs.getString("workjustification"));
				reimbursement.setAttachment(rs.getBlob("attachment"));
				reimbursement.setApprovalDocument(rs.getBlob("approvaldocument"));
				reimbursement.setApprovalId(rs.getInt("approvalid"));
				reimbursement.setStatus(rs.getString("status"));
				reimbursement.setTimeMissed(rs.getInt("timemissed"));
				reimbursement.setDenyReason(rs.getString("denyreason"));
				reimbursement.setInflatedReimbursementReason(rs.getString("inflatedreimbursementreason"));
				reimbursement.setSupervisorId(rs.getInt("supervisorid"));
				reimbursement.setSupervisorEmail(rs.getString("supervisoremail"));
				reimbursement.setSupervisorFirstName(rs.getString("supervisorfirstname"));
				reimbursement.setSupervisorLastName(rs.getString("supervisorlastname"));
				reimbursement.setDepartmentHeadId(rs.getInt("departmentheadid"));
				reimbursement.setDepartmentHeadEmail(rs.getString("departmentheademail"));
				reimbursement.setDepartmentHeadFirstName(rs.getString("departmentheadfirstname"));
				reimbursement.setDepartmentHeadLastName(rs.getString("departmentheadlastname"));
				reimbursement.setGrade(rs.getBlob("grade"));
				employee.setEmployeeId(rs.getInt("employeeid"));
				employee.setEmail(rs.getString("email"));
				employee.setFirstName(rs.getString("firstname"));
				employee.setLastName(rs.getString("lastname"));
				employee.setReportsTo(rs.getInt("reportsto"));
				employee.setDepartmentId(rs.getInt("departmentid"));
				
				employeeReimbursement = new EmployeeReimbursement();
				employeeReimbursement.setReimbursement(reimbursement);
				employeeReimbursement.setEmployee(employee);
				list.add(employeeReimbursement);
				
			}
			
			
			rs.close();
			cs.close();
		} catch (SQLException e) {
			
			log.trace(e.getMessage());

		} finally {
			try {
				conn.close();
				
				log.trace("Connection Closed");
			} catch (SQLException e) {
			
				log.trace(e.getMessage());
			}
		}
		
		return list;
	}
	
	/**
	 * Returns all reimbursements that are waiting for Benefits Coordinator approval.
	 * @return A list of all reimbursements that are waiting for Benefits Coordinator approval.
	 */
	public List<EmployeeReimbursement> getAllReimbursementsForBenCo() {
		List<EmployeeReimbursement> list = new ArrayList<EmployeeReimbursement>();
		String sql = "{call sp_select_all_benco (?)}";
		CallableStatement cs = null;
		ResultSet rs = null;
		Employee employee = null;
		Reimbursement reimbursement = null;
		EmployeeReimbursement employeeReimbursement = null;
		Connection conn = ConnectionFactory.getConnection();
		try{
			
			cs = conn.prepareCall(sql);
			cs.registerOutParameter(1, Types.REF_CURSOR);
			
			cs.execute();
			
			rs = (ResultSet) cs.getObject(1);
			while(rs.next()) {
				employee = new Employee();
				reimbursement = new Reimbursement();
				reimbursement.setReimbursementId(rs.getInt("reimbursementid"));
				reimbursement.setEmployeeId(rs.getInt("employeeid"));
				reimbursement.setSupervisorId(rs.getInt("supervisorid"));
				reimbursement.setDepartmentHeadId(rs.getInt("departmentheadid"));
				reimbursement.setBenCoId(rs.getInt("bencoid"));
				reimbursement.setApprovalProcessId(rs.getInt("approvalprocessid"));
				reimbursement.setEmployeeCreationDate(rs.getDate("employeecreationdate"));
				reimbursement.setEmployeeCreationTime(rs.getTimestamp("employeecreationtime"));
				reimbursement.setSupervisorApproveDate(rs.getDate("supervisorapprovedate"));
				reimbursement.setDepartmentHeadApproveDate(rs.getDate("departmentheadapprovedate"));
				reimbursement.setReimbursementLocationId(rs.getInt("reimbursementlocationid"));
				reimbursement.setStartDate(rs.getDate("startdate"));
				reimbursement.setAddress(rs.getString("address"));
				reimbursement.setCity(rs.getString("city"));
				reimbursement.setZip(rs.getString("zip"));
				reimbursement.setCountry(rs.getString("country"));
				reimbursement.setDescription(rs.getString("description"));
				reimbursement.setCost(rs.getDouble("cost"));
				reimbursement.setAdjustedCost(rs.getDouble("adjustedcost"));
				reimbursement.setGradeFormatId(rs.getInt("gradeformatid"));
				reimbursement.setFormat(rs.getString("format"));
				reimbursement.setCustomPassingGrade(rs.getString("custompassinggrade"));
				reimbursement.setDefaultPassingGrade(rs.getString("defaultpassinggrade"));
				reimbursement.setEventTypeId(rs.getInt("eventtypeid"));
				reimbursement.setEventType(rs.getString("eventtype"));
				reimbursement.setCoverage(rs.getDouble("coverage"));
				reimbursement.setWorkJustification(rs.getString("workjustification"));
				reimbursement.setAttachment(rs.getBlob("attachment"));
				reimbursement.setApprovalDocument(rs.getBlob("approvaldocument"));
				reimbursement.setApprovalId(rs.getInt("approvalid"));
				reimbursement.setStatus(rs.getString("status"));
				reimbursement.setTimeMissed(rs.getInt("timemissed"));
				reimbursement.setDenyReason(rs.getString("denyreason"));
				reimbursement.setInflatedReimbursementReason(rs.getString("inflatedreimbursementreason"));
				reimbursement.setSupervisorId(rs.getInt("supervisorid"));
				reimbursement.setSupervisorEmail(rs.getString("supervisoremail"));
				reimbursement.setSupervisorFirstName(rs.getString("supervisorfirstname"));
				reimbursement.setSupervisorLastName(rs.getString("supervisorlastname"));
				reimbursement.setDepartmentHeadId(rs.getInt("departmentheadid"));
				reimbursement.setDepartmentHeadEmail(rs.getString("departmentheademail"));
				reimbursement.setDepartmentHeadFirstName(rs.getString("departmentheadfirstname"));
				reimbursement.setDepartmentHeadLastName(rs.getString("departmentheadlastname"));
				reimbursement.setGrade(rs.getBlob("grade"));
				employee.setEmployeeId(rs.getInt("employeeid"));
				employee.setEmail(rs.getString("email"));
				employee.setFirstName(rs.getString("firstname"));
				employee.setLastName(rs.getString("lastname"));
				employee.setReportsTo(rs.getInt("reportsto"));
				employee.setDepartmentId(rs.getInt("departmentid"));
				
				employeeReimbursement = new EmployeeReimbursement();
				employeeReimbursement.setReimbursement(reimbursement);
				employeeReimbursement.setEmployee(employee);
				list.add(employeeReimbursement);
				
			}
			
			
			rs.close();
			cs.close();
		} catch (SQLException e) {
			
			log.trace(e.getMessage());

		} finally {
			try {
				conn.close();
				
				log.trace("Connection Closed");
			} catch (SQLException e) {
			
				log.trace(e.getMessage());
			}
		}
		
		return list;
	}
	
	/**
	 * Updates a specific reimbursement's status.
	 * @param id The id of the reimbursement.
	 * @param user The user who is either approving or denying the reimbursement.
	 * @param approval "APPROVED" or "DENIED".
	 * @param reason The reason for denying if denying a reimbursement.
	 * @param role The role of the user
	 */
	public void updateStatus(int id, Employee user , String approval, String reason, String role) {
		Connection conn = ConnectionFactory.getConnection();
		CallableStatement cs = null;
		if(role.equals("supervisor")) {
			if(approval.equals("APPROVED")) {
				String sql = "{call sp_update_approve_superv_reim (?,?)}";
				try{
					cs = conn.prepareCall(sql);
					cs.setInt(1, id);
					cs.setInt(2, user.getEmployeeId());
					cs.execute();
					cs.close();
				} catch (SQLException e) {
					
					log.trace(e.getMessage());

				} finally {
					try {
						conn.close();
						
						log.trace("Connection Closed");
					} catch (SQLException e) {
					
						log.trace(e.getMessage());
					}
				}
				
				if(user.getRoles().contains("Department Head")) {
					updateStatus(id, user, approval, reason, "departmentHead");
					return;
				}
			}else {
				
				String sql = "{call sp_update_deny_supervisor_reim (?,?,?,?)}";
				try{
					cs = conn.prepareCall(sql);
					cs.setInt(1, id);
					cs.setInt(2, user.getEmployeeId());
					cs.setString(3, approval);
					cs.setString(4, reason);
					
					cs.execute();
					cs.close();
				} catch (SQLException e) {
					
					log.trace(e.getMessage());

				} finally {
					try {
						conn.close();
						
						log.trace("Connection Closed");
					} catch (SQLException e) {
					
						log.trace(e.getMessage());
					}
				}
			}
		}else if(role.equals("departmentHead")) {
			if(approval.equals("APPROVED")) {
				String sql = "{call sp_update_approve_depart_reim (?,?)}";
				try{
					cs = conn.prepareCall(sql);
					cs.setInt(1, id);
					cs.setInt(2, user.getEmployeeId());
					cs.execute();
					cs.close();
				} catch (SQLException e) {
					
					log.trace(e.getMessage());

				} finally {
					try {
						conn.close();
						
						log.trace("Connection Closed");
					} catch (SQLException e) {
					
						log.trace(e.getMessage());
					}
				}
			}else {
				String sql = "{call sp_update_deny_department_reim (?,?,?,?)}";
				try{
					cs = conn.prepareCall(sql);
					cs.setInt(1, id);
					cs.setInt(2, user.getEmployeeId());
					cs.setString(3, approval);
					cs.setString(4, reason);
					
					cs.execute();
					cs.close();
				} catch (SQLException e) {
					
					log.trace(e.getMessage());

				} finally {
					try {
						conn.close();
						
						log.trace("Connection Closed");
					} catch (SQLException e) {
					
						log.trace(e.getMessage());
					}
				}
			}
		}else if(role.equals("benefitsCoordinator")) {
			if(approval.equals("APPROVED")) {
				String sql = "{call sp_update_approve_benco_reim (?)}";
				try{
					cs = conn.prepareCall(sql);
					cs.setInt(1, id);
					cs.execute();
					cs.close();
				} catch (SQLException e) {
					
					log.trace(e.getMessage());

				} finally {
					try {
						conn.close();
						
						log.trace("Connection Closed");
					} catch (SQLException e) {
					
						log.trace(e.getMessage());
					}
				}
			}else {
				String sql = "{call sp_update_deny_benco_reim (?,?,?,?)}";
				try{
					cs = conn.prepareCall(sql);
					cs.setInt(1, id);
					cs.setInt(2, user.getEmployeeId());
					cs.setString(3, approval);
					cs.setString(4, reason);
					
					cs.execute();
					cs.close();
				} catch (SQLException e) {
					
					log.trace(e.getMessage());

				} finally {
					try {
						conn.close();
						
						log.trace("Connection Closed");
					} catch (SQLException e) {
					
						log.trace(e.getMessage());
					}
				}
			}
		}else if(role.equals("employee")) {
			if(approval.equals("APPROVED")) {
				String sql = "{call sp_approve_reimb_employee (?)}";
				try{
					
					cs = conn.prepareCall(sql);
					cs.setInt(1, id);
					
					cs.execute();
					cs.close();
					
				} catch (SQLException e) {
					
					log.trace(e.getMessage());

				} finally {
					try {
						conn.close();
						
						log.trace("Connection Closed");
					} catch (SQLException e) {
					
						log.trace(e.getMessage());
					}
				}
				
			}else {
				String sql = "{call sp_cancel_reim(?)}";
				try{
					
					cs = conn.prepareCall(sql);
					cs.setInt(1, id);
					
					cs.execute();
					cs.close();
					
				} catch (SQLException e) {
					
					log.trace(e.getMessage());

				} finally {
					try {
						conn.close();
						
						log.trace("Connection Closed");
					} catch (SQLException e) {
					
						log.trace(e.getMessage());
					}
				}
			}
		}
		
	}

	/**
	 * Alters a reimbursements amount
	 * @param id The reimbursement.
	 * @param alterAmount The new amount.
	 * @param reason The reason for the alteration.
	 */
	public void alterReimbursementAmount(int id, double alterAmount, String reason) {
		String sql = "{call sp_update_alter_reim (?,?,?)}";
		CallableStatement cs = null;
		Connection conn = ConnectionFactory.getConnection();
		try{
			
			cs = conn.prepareCall(sql);
			cs.setInt(1, id);
			cs.setDouble(2, alterAmount);
			cs.setString(3, reason);
			cs.execute();
			cs.close();
			
			
		} catch (SQLException e) {
			
			log.trace(e.getMessage());

		} finally {
			try {
				conn.close();
				
				log.trace("Connection Closed");
			} catch (SQLException e) {
			
				log.trace(e.getMessage());
			}
		}
		
	}
	
	/**
	 * Upload grade for a reimbursement.
	 * @param id The id of the reimbursement.
	 * @param file The file to upload.
	 */
	public void uploadGrade(int id, byte[] file) {
		String sql = "UPDATE reimbursement SET grade=? WHERE reimbursementid=?";
		PreparedStatement ps = null;
		Connection conn = ConnectionFactory.getConnection();
		try{
			
			ps = conn.prepareStatement(sql);
			Blob grade = conn.createBlob();
			grade.setBytes(file.length, file);
			ps.setBlob(1, grade);
			ps.setInt(2, id);
			
			ps.executeUpdate();
			
			ps.close();
		} catch (SQLException e) {
			
			log.trace(e.getMessage());

		} finally {
			try {
				conn.close();
				
				log.trace("Connection Closed");
			} catch (SQLException e) {
			
				log.trace(e.getMessage());
			}
		}
	}
}
