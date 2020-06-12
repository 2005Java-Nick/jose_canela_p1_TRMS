<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
  <head>
    <title>Hello, world!</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
  </head>
  <body>
  	<div class="container">
  		<div class="row">
  			<div class="col-4">
  				<form action='signup' method="POST">
  					<div class="form-group">
  						<label for="email">Email:</label>
  						<input id="email" class="form-control" type="email" name="email" required>
  					</div>
  					<div class="form-group">
  						<label for="password">Password:</label>
  						<input id="password" class="form-control" type="password" name="password" required>
  					</div>
  					<div class="form-group">
  						<label for="confirmationPassowrd">Confirmation Password:</label>
  						<input id="confirmationPassword" class="form-control" type="password" name="confirmationPassword" required>
  					</div>
  					<div class="row">
  						<div class="col">
  							<label for="firstName">First Name:</label>
  							<input id="firstName" class="form-control" type="text" name="firstName" required>
  						</div>
  						<div class="col">
  							<label for="lastName">Last Name:</label>
  							<input id="lastName" class="form-control" type="text" name="lastName" required>
  						</div>
  					</div>
  					<br>
  					<div class="form-group">
  						<label for="departmentId">Department Id:</label>
  						<input id="departmentId" class="form-control" name="departmentId" type="number" min=0 required>
  					</div>
  					<div class="form-group">
  						<label for="supervisorId">Supervisor Id:</label>
  						<input id="supervisorId" class="form-control" name="supervisorId" type="number" min=0 required>
  					</div>
  					<div class="form-group">
  						<label for="roles">Select Role:</label>
  						<select id="roles" name="roles" multiple class="form-control">
     						<option value="Supervisor">Supervisor</option>
      						<option value="Department Head">Department Head</option>
      						<option value="Benefits Coordinator">Benefits Coordinator</option>
      					</select>
  					</div>
  					<button type="submit" class="btn btn-primary">Sign Up</button>
  				</form>
  			</div>
  		</div>
  	</div>
  
  
    
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
    <script src="static/js/login.js"></script>
  </body>
</html>