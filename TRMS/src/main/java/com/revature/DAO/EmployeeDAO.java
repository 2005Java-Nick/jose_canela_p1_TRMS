package com.revature.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.revature.Objects.Employee;
import com.revature.Util.ConnectionFactory;

/**
 * Handles the operations between the server and the database for Employee-related functions.
 *
 */
public class EmployeeDAO {
	private static Logger log = Logger.getRootLogger();
	private static EmployeeDAO employeeDAO;

	private EmployeeDAO() {

	}

	/**
	 * Returns the singleton instance of the EmployeeDAO used for communicating with
	 * the database related to the Employee model.
	 * 
	 * @return The singleton instance of the EmployeeDAO
	 */
	public static EmployeeDAO getInstance() {
		if (employeeDAO == null) {
			employeeDAO = new EmployeeDAO();
			return employeeDAO;
		} else {
			return employeeDAO;
		}
	}
	
	/**
	 * Creates a new Employee record in the employee table.
	 * @param email The email for the new employee.
	 * @param password The password for the new employee.
	 * @param firstName The first name of the new employee.
	 * @param lastName The last name of the new employee.
	 * @param reportsTo The id of the employee whom the new employee would directly work under.
	 * @param departmentId THe id of the department the employee belongs to.
	 * @return The employee id of the employee that was created. 0 if the employee could not be created
	 */
	
	public int create(String email, String password, String firstName, String lastName, Integer reportsTo, Integer departmentId) {

		
		int pk = 0;
		
		String sql = "{call create_employee(?, ?, ?, ?, ?, ?, ?)}";
		Connection conn = ConnectionFactory.getConnection();
		
		CallableStatement callableStatement = null;
		try{
			callableStatement = conn.prepareCall(sql);
			callableStatement.setString(1, email);
			callableStatement.setString(2, password);
			callableStatement.setString(3, firstName);
			callableStatement.setString(4, lastName);
		
			if(reportsTo == null) {
				callableStatement.setNull(5, Types.INTEGER);
			}else {
				callableStatement.setInt(5, reportsTo);
			}
			callableStatement.setInt(6, departmentId);
			
			callableStatement.registerOutParameter(7, Types.BIGINT);
			
			callableStatement.execute();
			
			pk = callableStatement.getInt(7);
			
			
			callableStatement.close();
			
		}catch (SQLException e) {
			
			log.trace(e.getMessage());

		} finally {
			try {
				conn.close();
				
				log.trace("Connection Closed");
			} catch (SQLException e) {
			
				log.trace(e.getMessage());
			}
		}
		
		
		
		return pk;
		
	}

	/**
	 * Finds an employee based on an email and password combination.
	 * @param email The email of the employee to find.
	 * @param password The password of the employee to find
	 * @return An instance of the employee that was retrieved.
	 */
	public Employee getEmployee(String email, String password) {
		String sql = "select * from get_employee(?,?)";
		Connection conn = ConnectionFactory.getConnection();
		PreparedStatement cs = null;
		ResultSet rs = null;
		Employee em = new Employee();
		try{
			cs = conn.prepareStatement(sql);
			cs.setString(1, email);
			cs.setString(2, password);
			
			rs = cs.executeQuery();
				while(rs.next()) {
					em.setEmployeeId(rs.getInt(1));
					em.setEmail(rs.getString(2));
					em.setFirstName(rs.getString(3));
					em.setLastName(rs.getString(4));
					em.setReportsTo(rs.getInt(5));
					em.setDepartmentId(rs.getInt(6));
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
		
		employeeDAO.getAllRolesForEmployee(em);
		
		return em;
	}
	
	/**
	 * Sets the roles for a specific employee. Also updates the employee to have those roles.
	 * @param e The Employee for which the roles will be set for
	 * @param roles Vararg of roles to add to the employee
	 * @return The number of roles added.
	 */
	public int setRoles(Employee e, String ...roles) {
		String sql = "INSERT INTO employee_role(employees_id, employeetype_id) VALUES (?, (SELECT employeetype_id FROM employee_type WHERE employee_type.employeetype=?))";
		Connection conn = ConnectionFactory.getConnection();
		PreparedStatement ps = null;
		int rolesAdded = 0;
		try{
			
			
			for(String role: roles) {
				
				ps = conn.prepareStatement(sql);
				ps.setInt(1, e.getEmployeeId());
				ps.setString(2, role);
				rolesAdded += ps.executeUpdate();
				e.getRoles().add(role);
				
			}
			
		} catch (SQLException e1) {
			
			log.trace(e1.getMessage());

		} finally {
			try {
				conn.close();
				
				log.trace("Connection Closed");
			} catch (SQLException e1) {
			
				log.trace(e1.getMessage());
			}
		}
		
		
		return rolesAdded;
	}
	
	/**
	 * Get all the roles for a particular employee.
	 * @param e The employee for which to get all the roles from.
	 * @return All roles held by the employee in the form of a list of strings.
	 */
	public List<String> getAllRolesForEmployee(Employee e){
		if(e == null) {
			return null;
		}
		ArrayList<String> roles = new ArrayList<String>();
		String sql  = "SELECT employee_type.employeetype FROM (SELECT * FROM employee_role WHERE employees_id=?) result INNER JOIN employee_type ON result.employeetype_id=employee_type.employeetype_id";
		Connection conn = ConnectionFactory.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			
			ps = conn.prepareStatement(sql);
			ps.setInt(1, e.getEmployeeId());
			rs = ps.executeQuery();
			while(rs.next()) {
				roles.add(rs.getString(1));
			}
			ps.close();
			rs.close();
			
		} catch (SQLException e1) {
			
			log.trace(e1.getMessage());

		} finally {
			try {
				conn.close();
				
				log.trace("Connection Closed");
			} catch (SQLException e1) {
			
				log.trace(e1.getMessage());
			}
		}
		
		e.setRoles(roles);
		
		
		return roles;
		
	}
	
}
