<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="com.revature.Objects.*,java.util.ArrayList,java.util.List,java.text.NumberFormat,java.text.DateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>TRMS | Dashboard</title>
		<meta charset="utf-8">
	    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	
	    <!-- Bootstrap CSS -->
	    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
		<link rel="stylesheet" href="static/css/dashboard.css">
	</head>
	<body>
		<div class="container">
			<div class="row">
				<div class="col-sm-3">
					<ul class="list-group">
					<% Employee employee = (Employee) request.getAttribute("employee"); %>
					<%= employee.getEmployeeId() %>
					<% if(employee.getRoles().contains("Employee")){ %>
						
							<a href="dashboard?view=employee" class="btn btn-info" role="button">Employee View</a>
						
					<% } %>
					<% if(employee.getRoles().contains("Supervisor")){ %>
					
							<a href="dashboard?view=supervisor" class="btn btn-info" role="button">Supervisor View</a>
						
					<% } %>
					<% if(employee.getRoles().contains("Department Head")){ %>
						
							<a href="dashboard?view=departmentHead" class="btn btn-info" role="button">Department Head View</a>
						
					<% } %>
					<% if(employee.getRoles().contains("Benefits Coordinator")){ %>
						
							<a href="dashboard?view=benefitsCoordinator" class="btn btn-info" role="button">Benefits Coordinator</a>
						
					<% } %>
						
							<button id="logout" type="button" class="btn btn-primary">Logout</button>
						
					</ul>
				</div>
				<div class="col-sm-9">
					<div id="accordion" role="tablist" aria-multiselectable="true">
					<%! List<EmployeeReimbursement> reimbursements; %>
					<% reimbursements = (List) request.getAttribute("employeeReimbursements"); %>
					<% NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(); %>
					<% for(int i = 0; i < reimbursements.size(); i++){ %>
						<div class="card">
							<div class="card-header collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapse<%= reimbursements.get(i).getReimbursement().getReimbursementId() %>" aria-expanded="false" aria-controls='collapse<%= reimbursements.get(i).getReimbursement().getReimbursementId() %>' role="tab" id="heading<%= reimbursements.get(i).getReimbursement().getReimbursementId() %>">
								<div class="row">
									<div class="col-sm-6">
										<h5>ID: <%= reimbursements.get(i).getReimbursement().getReimbursementId() %></h5>
									</div>
									<div class="col-sm-6">
										<h5>Employee Name: <%= reimbursements.get(i).getEmployee().getFirstName() %> <%= reimbursements.get(i).getEmployee().getLastName() %></h5>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-6">
										<% if(reimbursements.get(i).getReimbursement().getAdjustedCost() == 0){ %>
										<h5>Projected Reimbursement: <span id="projectedReimbursement"><%= currencyFormat.format(reimbursements.get(i).getReimbursement().getCost() * reimbursements.get(i).getReimbursement().getCoverage()) %></span></h5>
										<% }else{ %>
										<h5>Adjusted Reimbursement: <span id="projectedReimbursement"><%= currencyFormat.format(reimbursements.get(i).getReimbursement().getAdjustedCost()) %></span></h5>
										<% } %>
									</div>
									<div class="col-sm-6">
										<h5>Status: <%= reimbursements.get(i).getReimbursement().getStatus() %></h5>
									</div>
								</div>
							</div>
							<div id="collapse<%= reimbursements.get(i).getReimbursement().getReimbursementId() %>" class="collapse" role="tabpanel" aria-labelledby="heading<%= reimbursements.get(i).getReimbursement().getReimbursementId() %>">
								<div class="card-body">
									<div class="row">
										<div class="col-sm-4">
											Employee Name: <%= reimbursements.get(i).getEmployee().getFirstName() %> <%= reimbursements.get(i).getEmployee().getLastName() %>
										</div>
										<div class="col-sm-4">
											Employee Email: <%= reimbursements.get(i).getEmployee().getEmail() %>
										</div>
										<div class="col-sm-4">
											Submitted: <%= reimbursements.get(i).getReimbursement().getEmployeeCreationTime().toLocalDateTime().toLocalDate().toString() %> <%= reimbursements.get(i).getReimbursement().getEmployeeCreationTime().toLocalDateTime().toLocalTime().toString() %>
										</div>
									</div>
									<br>
									<div class="row">
										<div class="col-sm-3">
											Cost: <%= currencyFormat.format(reimbursements.get(i).getReimbursement().getCost()) %>
										</div>
										<div class="col-sm-3">
											Event Type: <%= reimbursements.get(i).getReimbursement().getEventType() %>
										</div>
										<div class="col-sm-3">
											Grade Format: <%= reimbursements.get(i).getReimbursement().getFormat() %>
										</div>
										<div class="col-sm-3">
											Default Passing Grade: <%= reimbursements.get(i).getReimbursement().getDefaultPassingGrade() %>
										</div>
									</div>
									<br>
									<div class="row">
										<div class="col-sm-4">
											<% if(reimbursements.get(i).getReimbursement().getSupervisorId() != 0){ %>
											Supervisor ID: <%= reimbursements.get(i).getReimbursement().getSupervisorId() %>
											<% }else{ %>
											Supervisor ID: --
											<% } %>
										</div>
										<div class="col-sm-4">
											<% if(reimbursements.get(i).getReimbursement().getSupervisorEmail() != null){ %>
											Supervisor Email: <%= reimbursements.get(i).getReimbursement().getSupervisorEmail() %>
											<% }else{ %>
											Supervisor Email: --
											<% } %>
										</div>
										<div clas="col-sm-4">
											<% if(reimbursements.get(i).getReimbursement().getSupervisorFirstName() != null){ %>
											Supervisor Name: <%= reimbursements.get(i).getReimbursement().getSupervisorFirstName() %> <%= reimbursements.get(i).getReimbursement().getSupervisorLastName() %>
											<% }else{ %>
											Supervisor Name: --
											<% } %>
										</div>
										<div class="col-sm-4">
											<% if(reimbursements.get(i).getReimbursement().getSupervisorApproveDate() == null){ %>
											Supervisor Approval Date: --
											<% }else{ %>
											Supervisor Approval Date: <%= reimbursements.get(i).getReimbursement().getSupervisorApproveDate().toLocalDate().toString() %>
											<% } %>
										</div>
									</div>
									<br>
									<div class="row">
										<div class="col-sm-4">
											<% if(reimbursements.get(i).getReimbursement().getDepartmentHeadId() != 0){ %>
											Department Head ID: <%= reimbursements.get(i).getReimbursement().getDepartmentHeadId() %>
											<% }else{ %>
											Department Head ID: --
											<% } %>
										</div>
										<div class="col-sm-4">
											<% if(reimbursements.get(i).getReimbursement().getDepartmentHeadEmail() != null){ %>
											Department Head Email: <%= reimbursements.get(i).getReimbursement().getDepartmentHeadEmail()  %>
											<% }else{ %>
											Department Head Email: --
											<% } %>
										</div>
										<div clas="col-sm-4">
											<% if(reimbursements.get(i).getReimbursement().getDepartmentHeadFirstName() != null){ %>
											Supervisor Name: <%= reimbursements.get(i).getReimbursement().getDepartmentHeadFirstName() %> <%= reimbursements.get(i).getReimbursement().getDepartmentHeadLastName() %>
											<% }else{ %>
											Supervisor Name: --
											<% } %>
										</div>
										<div class="col-sm-4">
											<% if(reimbursements.get(i).getReimbursement().getDepartmentHeadApproveDate() == null){ %>
											Department Head Approval Date: --
											<% }else{ %>
											Department Head Approval Date: <%= reimbursements.get(i).getReimbursement().getDepartmentHeadApproveDate().toLocalDate().toString() %>
											<% } %>
										</div>
									</div>
									<br>
									<div class="row">
										<div class="col-sm-6">	
											<% if(reimbursements.get(i).getReimbursement().getAdjustedCost() == 0){ %>
											Adjusted Reimbursement: --
											<% }else{ %>
											Adjusted Reimbursement: <%= reimbursements.get(i).getReimbursement().getAdjustedCost() %>
											<% } %>
										</div>
										<div class="col-sm-6">
										
										</div>
									</div>
									<br>
									<div class="row">
										<div class="col-sm-12">
											Description:<br>
											<%= reimbursements.get(i).getReimbursement().getDescription() %>
										</div>
									</div>
									<br>
									<div class="row">
										<div class="col-sm-12">
											Work Justification:<br>
											<%= reimbursements.get(i).getReimbursement().getWorkJustification() %>
										</div>
									</div>
									<br>
									<div class="row">
										<div class="col-sm-12">
											<% if(reimbursements.get(i).getReimbursement().getTimeMissed() == 0){ %>
											Time Missed: --
											<% }else{ %>
											Time Missed: <%= reimbursements.get(i).getReimbursement().getTimeMissed()/24 %>d   <%= reimbursements.get(i).getReimbursement().getTimeMissed() % 24%>h
											<% } %>
										</div>
									</div>
									<br>
									<div class="row">
										<div class="col-sm-3">
											Address: <%= reimbursements.get(i).getReimbursement().getAddress() %>
										</div>
										<div class="col-sm-3">
											City: <%= reimbursements.get(i).getReimbursement().getCity() %>
										</div>
										<div class="col-sm-3">
											Zip: <%= reimbursements.get(i).getReimbursement().getZip() %>
										</div>
										<div class="col-sm-3">
											Country: <%= reimbursements.get(i).getReimbursement().getCountry() %>
										</div>
									</div>
									<br>
									<div class="row">
										<div class="col-sm-6">
											<% if(reimbursements.get(i).getReimbursement().getDenyReason() == null){ %>
											Deny Reason: --
											<% }else{ %>
											Deny Reason: <%= reimbursements.get(i).getReimbursement().getDenyReason() %>
											<% } %>
										</div>
										<div class="col-sm-6">
											<% if(reimbursements.get(i).getReimbursement().getInflatedReimbursementReason() == null){ %>
											Inflated Reimbursement Reason: --
											<% }else{ %>
											Inflated Reimbursement Reason: <%= reimbursements.get(i).getReimbursement().getInflatedReimbursementReason() %>
											<% } %>
										</div>
									</div>
									<% if(reimbursements.get(i).getReimbursement().getStatus().equals("PENDING APPROVAL FROM DEPARTMENT HEAD")){ %>
									<br>
									<div class="row">
										<div class="col-sm-12">
											<input id="approve-reimbursementId" type="hidden" value="<%= reimbursements.get(i).getReimbursement().getReimbursementId() %>" readonly>
											<input id="approve-role" name="role" type="hidden" value="departmentHead" readonly>
											<input id="approve-approval" name="approval" type="hidden" value="APPROVED" readyonly>
											<button type="submit" class="btn btn-success approve">Approve</button>
											<button id="deny" type="button" class="btn btn-danger" data-toggle="modal" data-target="#denyReimbursement">Deny</button>
										</div>
									</div>
									<% } %>
								</div>
							</div>
						</div>
					<% } %>				
					</div>
				</div>
			</div>
		</div>
		
		<div id="denyReimbursement" class="modal fade" role="dialog">
			<div class="modal-dialog">
		    	<div class="modal-content">
		      		<div class="modal-header">
		      			<h4 class="modal-title">Reason to Deny</h4>
		        		<button type="button" class="close" data-dismiss="modal">&times;</button>
		      		</div>
		      		<div class="modal-body">
		        		<form action="updatestatus" method="POST" id="denyForm">
		        			<div class="form-group">
		        				<input id="reimbursementId" type="hidden" name="reimbursementId" value="">
		        				<input type="hidden" name="role" value="departmentHead">
		        				<input type="hidden" name="approval" value="DENIED">
		        				<textarea class="form-control" name="reason" id="reasonToDeny" max=500></textarea>
		        				<small for="reasonToDeny">Required. Max 500 characters.</small>
		        			</div>
		        			<button type="submit" class="btn btn-danger">Submit</button>
		        		</form>
		      		</div>
		      		<div class="modal-footer">
		        		<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		      		</div>
		    	</div>
		  	</div>
		</div>
	
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
	    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
		<script src="static/js/dashboard-departmenthead.js"></script>
	</body>
</html>