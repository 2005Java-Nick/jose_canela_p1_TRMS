package com.revature.Objects;

import java.io.Serializable;

public class EmployeeReimbursement implements Serializable {

	private Employee employee;
	private Reimbursement reimbursement;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Reimbursement getReimbursement() {
		return reimbursement;
	}

	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
	}
}
