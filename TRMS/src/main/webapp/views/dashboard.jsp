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
					<%=  employee.getEmployeeId() %>
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
					<div id="errCreate" class="hidden alert alert-danger alert-dismissible fade show" role="alert">
						
  						<button type="button" class="close" data-dismiss="alert" aria-label="Close">
   						 <span aria-hidden="true">&times;</span>
  						</button>
					</div>
					<button id="submitNewReimbursement" type="button" class="btn btn-primary" data-toggle="modal" data-target="#createReimbursement">Submit New Reimbursement Form</button>
					<div id="accordion" role="tablist" aria-multiselectable="true">
					<%! List<Reimbursement> reimbursements; %>
					<% reimbursements = (List) request.getAttribute("reimbursements"); %>
					<% NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(); %>
					<% DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM); %>
					<% for(int i = reimbursements.size() - 1; i >= 0; i--){ %>
						<div class="card">
							<div class="card-header collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapse<%= reimbursements.get(i).getReimbursementId() %>" aria-expanded="false" aria-controls='collapse<%= reimbursements.get(i).getReimbursementId() %>' role="tab" id="heading<%= reimbursements.get(i).getReimbursementId() %>">
								<div class="row">
									<div class="col-sm-12">
										<h5>ID: <%= reimbursements.get(i).getReimbursementId() %></h5>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-6">
										<% if(reimbursements.get(i).getAdjustedCost() == 0){ %>
										<h5>Projected Reimbursement: <span id="projectedReimbursement"><%= currencyFormat.format(reimbursements.get(i).getCost() * reimbursements.get(i).getCoverage()) %></span></h5>
										<% }else{ %>
										<h5>Adjusted Reimbursement: <span id="projectedReimbursement"><%= currencyFormat.format(reimbursements.get(i).getAdjustedCost()) %></span></h5>
										<% } %>
									</div>
									<div class="col-sm-6">
										<h5>Status: <%= reimbursements.get(i).getStatus() %></h5>
									</div>
								</div>
							</div>
							<div id="collapse<%= reimbursements.get(i).getReimbursementId() %>" class="collapse" role="tabpanel" aria-labelledby="heading<%= reimbursements.get(i).getReimbursementId() %>">
								<div class="card-body">
									<div class="row">
										<div class="col-sm-12"> 
											Submitted: <%= reimbursements.get(i).getEmployeeCreationTime().toLocalDateTime().toLocalDate().toString() %> <%= reimbursements.get(i).getEmployeeCreationTime().toLocalDateTime().toLocalTime().toString() %>
										</div>
									</div>
									<br>
									<div class="row">
										<div class="col-sm-4">
											Cost: <%= currencyFormat.format(reimbursements.get(i).getCost()) %>
										</div>
										<div class="col-sm-4">
											Event Type: <%= reimbursements.get(i).getEventType() %>
										</div>
										<div class="col-sm-4">
											Start Date: <%= reimbursements.get(i).getStartDate().toLocalDate().toString() %>
										</div>
									</div>
									<br>
									<div class="row">
										<div class="col-sm-6">
											Grade Format: <%= reimbursements.get(i).getFormat() %>
										</div>
										<% if(reimbursements.get(i).getCustomPassingGrade().equals("DEFAULT")){ %>
										<div class="col-sm-6">
											 Passing Grade: <%= reimbursements.get(i).getDefaultPassingGrade() %>
										</div>
										<% }else{ %>
										<div class="col-sm-6">
											 Passing Grade: <%= reimbursements.get(i).getCustomPassingGrade() %>
										</div>
										<% } %>
									</div>
									<br>
									<div class="row">
										<div class="col-sm-4">
											<% if(reimbursements.get(i).getSupervisorId() != 0){ %>
											Supervisor ID: <%= reimbursements.get(i).getSupervisorId() %>
											<% }else{ %>
											Supervisor ID: --
											<% } %>
										</div>
										<div class="col-sm-4">
											<% if(reimbursements.get(i).getSupervisorEmail() != null){ %>
											Supervisor Email: <%= reimbursements.get(i).getSupervisorEmail() %>
											<% }else{ %>
											Supervisor Email: --
											<% } %>
										</div>
										<div class="col-sm-4">
											<% if(reimbursements.get(i).getSupervisorFirstName() != null){ %>
											Supervisor Name: <%= reimbursements.get(i).getSupervisorFirstName() %> <%= reimbursements.get(i).getSupervisorLastName() %>
											<% }else{ %>
											Supervisor Name: --
											<% } %>
										</div>
										<div class="col-sm-4">
											<% if(reimbursements.get(i).getSupervisorApproveDate() == null){ %>
											Supervisor Approval Date: --
											<% }else{ %>
											Supervisor Approval Date: <%= reimbursements.get(i).getSupervisorApproveDate().toLocalDate().toString() %>
											<% } %>
										</div>
									</div>
									<br>
									<div class="row">
										<div class="col-sm-4">
											<% if(reimbursements.get(i).getDepartmentHeadId() != 0){ %>
											Department Head ID: <%= reimbursements.get(i).getDepartmentHeadId() %>
											<% }else{ %>
											Department Head ID: --
											<% } %>
										</div>
										<div class="col-sm-4">
											<% if(reimbursements.get(i).getDepartmentHeadEmail() != null){ %>
											Department Head Email: <%= reimbursements.get(i).getDepartmentHeadEmail()  %>
											<% }else{ %>
											Department Head Email: --
											<% } %>
										</div>
										<div class="col-sm-4">
											<% if(reimbursements.get(i).getDepartmentHeadFirstName() != null){ %>
											Supervisor Name: <%= reimbursements.get(i).getDepartmentHeadFirstName() %> <%= reimbursements.get(i).getDepartmentHeadLastName() %>
											<% }else{ %>
											Supervisor Name: --
											<% } %>
										</div>
										<div class="col-sm-4">
											<% if(reimbursements.get(i).getDepartmentHeadApproveDate() == null){ %>
											Department Head Approval Date: --
											<% }else{ %>
											Department Head Approval Date: <%= reimbursements.get(i).getDepartmentHeadApproveDate().toLocalDate().toString() %>
											<% } %>
										</div>
									</div>
									<br>
									<div class="row">
										<div class="col-sm-6">	
											<% if(reimbursements.get(i).getAdjustedCost() == 0){ %>
											Adjusted Reimbursement: --
											<% }else{ %>
											Adjusted Reimbursement: <%= reimbursements.get(i).getAdjustedCost() %>
											<% } %>
										</div>
										<div class="col-sm-6">
										
										</div>
									</div>
									<br>
									<div class="row">
										<div class="col-sm-12">
											Description:<br>
											<%= reimbursements.get(i).getDescription() %>
										</div>
									</div>
									<br>
									<div class="row">
										<div class="col-sm-12">
											Work Justification:<br>
											<%= reimbursements.get(i).getWorkJustification() %>
										</div>
									</div>
									<br>
									<div class="row">
										<div class="col-sm-12">
											<% if(reimbursements.get(i).getTimeMissed() == 0){ %>
											Time Missed: --
											<% }else{ %>
											Time Missed: <%= reimbursements.get(i).getTimeMissed()/24 %>d   <%= reimbursements.get(i).getTimeMissed() % 24%>h
											<% } %>
										</div>
									</div>
									<br>
									<div class="row">
										<div class="col-sm-3">
											Address: <%= reimbursements.get(i).getAddress() %>
										</div>
										<div class="col-sm-3">
											City: <%= reimbursements.get(i).getCity() %>
										</div>
										<div class="col-sm-3">
											Zip: <%= reimbursements.get(i).getZip() %>
										</div>
										<div class="col-sm-3">
											Country: <%= reimbursements.get(i).getCountry() %>
										</div>
									</div>
									<br>
									<div class="row">
										<div class="col-sm-6">
											<% if(reimbursements.get(i).getDenyReason() == null){ %>
											Deny Reason: --
											<% }else{ %>
											Deny Reason: <%= reimbursements.get(i).getDenyReason() %>
											<% } %>
										</div>
										<div class="col-sm-6">
											<% if(reimbursements.get(i).getInflatedReimbursementReason() == null){ %>
											Inflated Reimbursement Reason: --
											<% }else{ %>
											Inflated Reimbursement Reason: <%= reimbursements.get(i).getInflatedReimbursementReason() %>
											<% } %>
										</div>
									</div>
									<br>
									<% if(reimbursements.get(i).getGrade() != null){ %>
									<div class="row">
										<div class="col-sm-12">
										Grade/Presentation Submitted
									</div>
									<% }else{ %>
									<br>
									<div class="row">
										<div class="col-sm-12">
											<form id="formSubmitGrade" class="form-inline" enctype="multipart/form-data" name="submitGrade">
												<div class="form-group">
													<input name="id" type="hidden" value="<%= reimbursements.get(i).getReimbursementId() %>">
													<input name="grade" type="file">
												</div>
												<div class="form-group">
													<button type="submit" class="btn btn-info">Submit</button>
												</div>
											</form>
										</div>
									</div>
									<br>
									<% } %>
									<% if(reimbursements.get(i).getAdjustedCost() != 0 && !reimbursements.get(i).getStatus().equals("EMPLOYEE CANCELED") && !reimbursements.get(i).getStatus().equals("APPROVED")){ %>
									<div class="row">
										<div class="col-sm-12">
											<input id="cancel-reimbursementId" type="hidden" value="<%= reimbursements.get(i).getReimbursementId() %>" readonly>
											<button type="button" class="btn btn-success approve-reimbursement">Approve Reimbursement Change</button> 
											<button id="cancelReimbursement" type="button" class="btn btn-danger">Cancel Reimbursement</button></div>
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
		<div id="createReimbursement" class="modal fade" id="createReimbursement" tabindex="-1" role="dialog" aria-labelledby="ReimbursementForm" aria-hidden="true">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="modalLabel">New Reimbursement</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<div class="container">
							<div class="row">
								<div class="col-sm-12">
									<form id="newReimbursement" enctype="multipart/form-data" name="newReimbursement">
										<div class="form-group">
											<label for="formCost">Reimbursement Request Amount</label>
											$<input name="cost" class="form-control" type="number" min="0" id="formCost" aria-describedby="formCostSmall" placeholder="Ex. 249.99" required>
											<small id="formCostSmall">Required</small>
										</div>
										<div class="form-group">
											<label for="formDate">Start Date</label>
											<input name="startDate" class="form-control" type="date" id="formDate" aria-describedby="formDateSmall" required>
											<small id="formDateSmall">Required</small>
										</div>
										<div class="form-group">
											<label for="formTimeMissedHours">Time Missed (hrs)</label>
											<input name="timeMissed" class="form-control" type="number" min="0" id="formTimeMissedDays" min="0" aria-describedby="formTimeMissedHoursSmall">
										</div>
										<div class="form-group">
											<label for="formEventType">Event Type</label>
											<select name="eventType" class="form-control" id="formEventTypes" required>
											<%! ArrayList<EventType> eventTypes; %>
											<% eventTypes = (ArrayList<EventType>) request.getAttribute("eventTypes"); %>
												<option value=""></option>
												<% for(int i = 0; i < eventTypes.size(); i++){ %>
													<option value="<%= eventTypes.get(i).getEventType() %>"><%= eventTypes.get(i).getEventType() %></option>
												<% } %>
											</select>
											<small for="formEventTypes">Required</small>
										</div>
										<div class="form-group">
											<label for="formGradeFormat">GradeFormat</label>
											<%! ArrayList<GradeFormat> gradeFormats; %>
											<% gradeFormats = (ArrayList<GradeFormat>) request.getAttribute("gradeFormats"); %>
											<select name="gradeFormat" class="form-control" id="formGradeFormats" required>
												<option value=""></option>
												<% for(int i = 0; i < gradeFormats.size(); i++){ %>
													<option value="<%=gradeFormats.get(i).getFormat() %>"><%= gradeFormats.get(i).getFormat() %></option>
												<% } %>
											</select>
											<small for="formGradeFormats">Required</small>
										</div>
										<div class="form-row">
											<div class="col">
												<label for="formPassingGradeSelection">Passing Grade</label>
												<select name="passingGradeSelection" class="form-control" id="formPassingGradeSelection" required>
													<option value="DEFAULT">Default</option>
													<option value="CUSTOM">Custom</option>
												</select>
											</div>
											<div class="col">
												<label for="formPassingGradeSelection">Grade</label>
												<input id="formPassingGrade" class="form-control" name="passingGrade" type="text">
											</div>
										</div>
										<br>
										<h6 id="eventLocation">Event Location</h6>
										<small for="eventLocation">required</small>
										<div class="form-group">
											<label for="formAddress">Address</label>
											<input name="address" class="form-control" type="text" id="formAddress" aria-describedby="formAddress" required>
										</div>
										<div class="form-row">
											<div class="col">
												<label for="formCity">City</label>
												<input name="city" class="form-control" type="text" id="formCity" aria-describedby="formCity" required>
											</div>
											<div class="col">
												<label for="formZip">Zip</label>
												<input name="zip" class="form-control" type="text" id="formZip" aria-describedby="formZip" required>
											</div>
											<div class="col">
												<label for="formCountry">Country</label>
												<input name="country" class="form-control" type="text" id="forCountry" aria-describedby="formCountry" required>
											</div>
										</div>
										<br/>
										<div class="form-group">
											<label for="formDescription">Description</label>
											<textarea name="description" class="form-control" id="formDescription" max-length="500" required></textarea>
											<small for=formDescription>Required. 500 max characters.</small>
										</div>
										<div class="form-group">
											<label for="formWorkJustification">Work Justification</label>
											<textarea name="workJustification" class="form-control" id="formJustification" max-length="500" required></textarea>
											<small for=formWorkJustification>Required. 500 max characters.</small>
										</div>
										<div class="form-group">
											<label for="formAttachement">Event-related Document</label>
											<input name="attachment" class="form-control" id="formAttachment" type="file" accept=".pdf, .png, .jpeg, .txt, .doc">
										</div>
										<div class="form-group">
											<label for="formApprovalDocument">Approval Document</label>
											<input name="approvalDocument" class="form-control" id="formApprovalDocument" type="file" accept=".msg">
										</div>
										<br>
										<div class="from-group">
											<label for="formProjectedReimbursement">Projected Reimbursement</label>
											<input type="number" class="form-control" id="formProjectedReimbursement" step="0.01" readonly>
										</div>
										<button type="submit" class="btn btn-primary">Submit</button>
										<button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
	    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
		<script src="static/js/dashboard.js"></script>
		<script>
		$(function(){
			
			
			function updateProjectedReimbursement(){
				let cost = $("#formCost");
				let eventType = $("#formEventTypes");
				let projectedReimbursement = $("#formProjectedReimbursement");
				let eventTypes= {};
				<% for(int i = 0; i < eventTypes.size(); i++){ %>
					eventTypes["<%= eventTypes.get(i).getEventType() %>"] = <%= eventTypes.get(i).getCoverage() %>;
				<% } %>
				projectedReimbursement.val((cost.val() * eventTypes[eventType.val()]).toFixed(2));
				
				
			}
			
			$("#formCost").change(function(){
				if($("#formCost").val() && $("#formEventTypes").val()){
					updateProjectedReimbursement();
				}
			});
			
			$("#formEventTypes").change(function(){
				if($("#formCost").val() && $("#formEventTypes").val()){
					updateProjectedReimbursement();
				}
			});
			
			
		});
		</script>
	</body>
</html>