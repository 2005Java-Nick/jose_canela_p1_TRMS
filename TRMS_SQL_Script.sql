--------------------------------------------------------
--Project 1 TRMS PostgreSQL Script
--------------------------------------------------------

--------------------------------------------------------
--  DDL for Table APPROVAL
--------------------------------------------------------
create table approval(
approval_id serial primary key,
status varchar not null
);
--------------------------------------------------------
--  DDL for Table APPROVALPROCESS
--------------------------------------------------------
create table approvalprocess(
approvalprocess_id serial primary key,
employee_creation_date date not null,
employee_creation_time timestamp with time zone not null,
supervisor_approve_date date,
departmenthead_approve_date date
);
--------------------------------------------------------
--  DDL for Table DEPARTMENT
--------------------------------------------------------
create table department(
department_id serial primary key,
department varchar not null
);
--------------------------------------------------------
--  DDL for Table EMPLOYEE
--------------------------------------------------------
create table employees(
employees_id serial primary key,
email varchar unique not null,
firstname varchar not null,
lastname varchar not null,
reportsto bigint, --fk
department_id bigint, --fk
pw varchar not null
);
--------------------------------------------------------
--  DDL for Table EMPLOYEEROLE
--------------------------------------------------------
create table employee_role(
employee_role_id serial primary key,
employees_id bigint, --fk
employeetype_id bigint --fk
);
--------------------------------------------------------
--  DDL for Table EMPLOYEETYPE
--------------------------------------------------------
create table employee_type(
employeetype_id serial primary key,
employeetype varchar not null
);
--------------------------------------------------------
--  DDL for Table EVENTTYPE
--------------------------------------------------------
drop table event_type cascade;
create table event_type(
eventtype_id serial primary key,
eventtype varchar not null,
coverage numeric --- fix
);
--------------------------------------------------------
--  DDL for Table GRADEFORMAT
--------------------------------------------------------
create table grade_format(
grade_format_id serial primary key,
format varchar not null,
default_passing_grade varchar not null
);
--------------------------------------------------------
--  DDL for Table REIMBURSEMENT
--------------------------------------------------------
create table reimbursement(
reimbursement_id serial primary key,
employee_id bigint not null, --fk
supervisor_id bigint, --fk
departmenthead_id bigint, --fk
benco_id bigint, --fk
approvalprocess_id  bigint not null, --fk
reimbursementlocation_id bigint not null, --fk
description varchar not null,
reimbursement_cost numeric(9,2) not null,
adjustedcost numeric(9,2),
grade_format_id bigint not null, --fk
eventtype_id bigint not null, --fk
workjustification varchar not null,
attachment  bytea,
approvaldocument  bytea,
approval_id bigint not null, --fk
timemissed int,
denyreason varchar,
inflated_reimbursement_reason varchar,
custom_passing_grade varchar,
grade  bytea
);
--------------------------------------------------------
--  DDL for Table REIMBURSEMENTLOCATION
--------------------------------------------------------
create table reimbursementlocation(
reimbursementlocation_id serial primary key,
startdate date not null,
address varchar not null,
city varchar not null,
zip varchar,
country varchar not null
);

--------------------------------------------------------
--  Ref Constraints for Table EMPLOYEE
--------------------------------------------------------
alter table employees add constraint FK_employees_department_id
	foreign key (department_id) references department (department_id) on delete cascade on update cascade;
create index i_FK_employees_department_id on employees (department_id);

alter table employees add constraint FK_employees_reportsto
	foreign key (reportsto) references employees (employees_id) on delete cascade on update cascade;
create index i_FK_employees_reportsto on employees (reportsto);
--------------------------------------------------------
--  Ref Constraints for Table EMPLOYEEROLE
--------------------------------------------------------
alter table employee_role add constraint FK_employee_role_employeetype_id
	foreign key (employeetype_id) references employee_type (employeetype_id) on delete cascade on update cascade;
create index i_FK_employeetype_id on employee_role (employeetype_id);

alter table employee_role add constraint FK_employee_role_employees_id
	foreign key (employees_id) references employees (employees_id) on delete cascade on update cascade;
create index i_FK_employee_role_employees_id on employee_role (employees_id);
--------------------------------------------------------
--  Ref Constraints for Table REIMBURSEMENT
--------------------------------------------------------
alter table reimbursement add constraint FK_reimbursement_approval_id
	foreign key (approval_id) references approval (approval_id) on delete cascade on update cascade;
create index i_FK_reimbursement_approval_id on reimbursement (approval_id);

alter table reimbursement add constraint FK_reimbursement_approvalprocess_id
	foreign key (approvalprocess_id) references approvalprocess (approvalprocess_id) on delete cascade on update cascade;
create index i_FK_reimbursement_approvalprocess_id on reimbursement (approvalprocess_id);
	 
alter table reimbursement add constraint FK_reimbursement_benco_id
	foreign key (benco_id) references employees (employees_id) on delete cascade on update cascade;
create index i_FK_reimbursement_benco_id on reimbursement (benco_id);

alter table reimbursement add constraint FK_reimbursement_employee_id
	foreign key (employee_id) references employees (employees_id) on delete cascade on update cascade;
create index i_FK_reimbursement_employee_id on reimbursement (employee_id);	 

alter table reimbursement add constraint FK_reimbursement_eventtype_id
	foreign key (eventtype_id) references event_type (eventtype_id) on delete cascade on update cascade;
create index i_FK_reimbursement_eventtype_id on reimbursement (eventtype_id);	 
	 
alter table reimbursement add constraint FK_reimbursement_grade_format_id
	foreign key (grade_format_id) references grade_format (grade_format_id) on delete cascade on update cascade;
create index i_FK_reimbursement_grade_format_id on reimbursement (grade_format_id);	 	
	 
alter table reimbursement add constraint FK_reimbursement_departmenthead_id
	foreign key (departmenthead_id) references employees (employees_id) on delete cascade on update cascade;
create index i_FK_reimbursement_departmenthead_id on reimbursement (departmenthead_id);

alter table reimbursement add constraint FK_reimbursement_reimbursementlocation_id
	foreign key (reimbursementlocation_id) references reimbursementlocation (reimbursementlocation_id) on delete cascade on update cascade;
create index i_FK_reimbursement_reimbursementlocation_id on reimbursement (reimbursementlocation_id);

alter table reimbursement add constraint FK_reimbursement_supervisor_id
	foreign key (supervisor_id) references employees (employees_id) on delete cascade on update cascade;
create index i_FK_reimbursement_supervisor_id on reimbursement (supervisor_id);

insert into approval (approval_id,status) values (1,'PENDING APPROVAL FROM SUPERVISOR');
insert into approval (approval_id,status) values (2,'PENDING APPROVAL FROM DEPARTMENT HEAD');
insert into approval (approval_id,status) values (3,'PENDING APPROVAL FROM BENEFITS COORDINATOR');
insert into approval (approval_id,status) values (4,'NEED ADDITIONAL INFORMATION');
insert into approval (approval_id,status) values (5,'REIMBURSEMENT AMOUNT ALTERED');
insert into approval (approval_id,status) values (6,'EMPLOYEE CANCELED');
insert into approval (approval_id,status) values (7,'DENIED');
insert into approval (approval_id,status) values (8,'APPROVED');

insert into department (department_id, department) values (1,'Department 1');
insert into department (department_id, department) values (2,'Department 2');
insert into department (department_id, department) values (3,'Department 3');

insert into employee_type (employeetype_id, employeetype) values (1,'Employee');
insert into employee_type (employeetype_id, employeetype) values (2,'Supervisor');
insert into employee_type (employeetype_id, employeetype) values (3,'Department Head');
insert into employee_type (employeetype_id, employeetype) values (4,'Benefits Coordinator');

insert into event_type (eventtype_id, eventtype, coverage) values (1,'University Course',0.8);
insert into event_type (eventtype_id, eventtype, coverage) values (2,'Seminar',0.6);
insert into event_type (eventtype_id, eventtype, coverage) values (3,'Certification Preparation Class',0.75);
insert into event_type (eventtype_id, eventtype, coverage) values (4,'Certification',1);
insert into event_type (eventtype_id, eventtype, coverage) values (5,'Technical Training',0.9);
insert into event_type (eventtype_id, eventtype, coverage) values (6,'Other',0.3);

insert into grade_format (grade_format_id, format, default_passing_grade) values (1,'PASS/FAIL','PASS');
insert into grade_format (grade_format_id, format, default_passing_grade) values (2,'LETTER GRADES','C');
insert into grade_format (grade_format_id, format, default_passing_grade) values (3,'PERCENTAGE','70');
insert into grade_format (grade_format_id, format, default_passing_grade) values (4,'PRESENTATION','NONE');

-- Procedure SP_CANCEL_REIM
--------------------------------------------------------
create or replace procedure sp_cancel_reim(p_id  in bigint)
language plpgsql as $$
begin
    update reimbursement set approval_id =(select approval_id from approval where status='EMPLOYEE CANCELED') where reimbursement_id=p_id;
commit;
end
$$;

--call sp_insert_approvalprocess(?, ?, ?)
--------------------------------------------------------
-- Procedure SP_INSERT_APPROVALPROCESS
--------------------------------------------------------
create or replace procedure sp_insert_approvalprocess(inputdate in date, inputtimestamp in timestamp with time zone, pk inout bigint) 
language plpgsql as $$
begin
    insert into approvalprocess (employee_creation_date, employee_creation_time, supervisor_approve_date, departmenthead_approve_date) values (inputdate, inputtimestamp, null, null) returning approvalprocess.approvalprocess_id into pk;
commit;
end
$$;

-- call sp_insert_approvalprocess(?, ?, ?);
--------------------------------------------------------
-- Procedure SP_INSERT_REIMBURSEMENT
--------------------------------------------------------
create or replace procedure sp_insert_reimbursement(inputemployeeid IN bigint, inputapprovalprocessid IN bigint, inputreimbursementlocationid IN bigint, inputdescription IN VARCHAR, inputcost IN numeric(9,2), inputgradeformat IN VARCHAR, inputeventtype IN VARCHAR, inputworkjustification IN VARCHAR, inputattachment IN BYTEA, inputapprovaldocument IN BYTEA, inputtimemissed IN int, inputcustompassinggrade IN VARCHAR, pk inout bigint)
language plpgsql as $$
DECLARE pk_gradeformat bigint; pk_eventtype bigint; pk_approval bigint;
BEGIN
    SELECT gradeformat_id INTO pk_gradeformat FROM grade_format WHERE format=inputgradeformat;
    SELECT eventtype_id INTO pk_eventtype FROM event_type WHERE eventtype=inputeventtype;
    SELECT approval_id INTO pk_approval FROM approval WHERE status='PENDING APPROVAL FROM SUPERVISOR';
    INSERT INTO reimbursement (employee_id, approvalprocess_id, reimbursementlocation_id, description, reimbursement_cost, grade_format_id, eventtype_id, workjustification, attachment, approvaldocument, approval_id, timemissed, custom_passing_grade) VALUES (inputemployeeid, inputapprovalprocessid, inputreimbursementlocationid, inputdescription, inputcost, pk_gradeformat, pk_eventtype, inputworkjustification, inputattachment, inputapprovaldocument, pk_approval, inputtimemissed, inputcustompassinggrade) returning reimbursement.reimbursement_id into pk;
commit;
END;
$$;

-- call sp_insert_reimbursement(?,?,?,?,?,?,?,?,?,?,?,?,?);

--------------------------------------------------------
--  DDL for Procedure SP_INSERT_REIMLOCATION
--------------------------------------------------------
CREATE OR REPLACE procedure sp_insert_reimlocation(inputdate IN DATE, inputaddress IN VARCHAR, inputcity IN VARCHAR, inputzip IN VARCHAR, inputcountry IN VARCHAR, inputreimid inout bigint)
language plpgsql as $$
begin
	INSERT INTO reimbursementlocation (startdate, address, city, zip, country) VALUES (inputdate,inputaddress,inputcity,inputzip,inputcountry) returning reimbursementlocation_id into inputreimid;
commit;
end
$$;

-- call sp_insert_reimlocation(?, ?, ?, ?, ?, ?);
--------------------------------------------------------
--  DDL for Procedure SP_SELECT_ALL_BENCO
--------------------------------------------------------
create or replace function sp_select_all_benco() returns 
refcursor 
as $$
declare
ref_cursor refcursor := 'allben'; 
begin 
	open ref_cursor for (SELECT reimbursement.*, approvalprocess.*, reimbursementlocation.*, grade_format.*, event_type.*, approval.*, employees.*, supervisor.employees_id AS supervisorid, supervisor.email AS supervisoremail, supervisor.firstname AS supervisorfirstname, supervisor.lastname AS supervisorlastname, departmenthead.employees_id AS departmentheadid, departmenthead.employees_id AS departmentid, departmenthead.email AS departmentheademail, departmenthead.firstname AS departmentheadfirstname, departmenthead.lastname AS departmentheadlastname FROM ((((((((reimbursement INNER JOIN approvalprocess ON approvalprocess.approvalprocess_id=reimbursement.approvalprocess_id) INNER JOIN reimbursementlocation ON reimbursementlocation.reimbursementlocation_id=reimbursement.reimbursementlocation_id) INNER JOIN grade_format ON grade_format.grade_format_id=reimbursement.grade_format_id) INNER JOIN event_type ON event_type.eventtype_id=reimbursement.eventtype_id) INNER JOIN approval ON approval.approval_id=reimbursement.approval_id) INNER JOIN employees ON reimbursement.employee_id=employees.employees_id) LEFT JOIN employees supervisor ON reimbursement.supervisor_id=supervisor.employees_id) LEFT JOIN employees departmenthead ON reimbursement.departmenthead_id=departmenthead.employees_id) WHERE reimbursement.approval_id IN (3, 5) ORDER BY employee_creation_date, employee_creation_time);
	return (ref_cursor);
end;
$$ language plpgsql;
-- select sp_select_all_benco();
call sp_select_all_benco();
begin;
select sp_select_all_benco();
fetch all in allben;
commit;

SELECT reimbursement.*, approvalprocess.*, reimbursementlocation.*, grade_format.*, event_type.*, approval.*, employees.*, supervisor.employees_id AS supervisorid, supervisor.email AS supervisoremail, supervisor.firstname AS supervisorfirstname, supervisor.lastname AS supervisorlastname, departmenthead.employees_id AS departmentheadid, departmenthead.employees_id AS departmentid, departmenthead.email AS departmentheademail, departmenthead.firstname AS departmentheadfirstname, departmenthead.lastname AS departmentheadlastname FROM ((((((((reimbursement INNER JOIN approvalprocess ON approvalprocess.approvalprocess_id=reimbursement.approvalprocess_id) INNER JOIN reimbursementlocation ON reimbursementlocation.reimbursementlocation_id=reimbursement.reimbursementlocation_id) INNER JOIN grade_format ON grade_format.grade_format_id=reimbursement.grade_format_id) INNER JOIN event_type ON event_type.eventtype_id=reimbursement.eventtype_id) INNER JOIN approval ON approval.approval_id=reimbursement.approval_id) INNER JOIN employees ON reimbursement.employee_id=employees.employees_id) LEFT JOIN employees supervisor ON reimbursement.supervisor_id=supervisor.employees_id) LEFT JOIN employees departmenthead ON reimbursement.departmenthead_id=departmenthead.employees_id) WHERE reimbursement.approval_id IN (3, 5) ORDER BY employee_creation_date, employee_creation_time;
--------------------------------------------------------
--  DDL for Procedure SP_SELECT_ALL_DEPARTMENT
--------------------------------------------------------
create or replace function sp_select_all_department(p_departmentid bigint) returns 
refcursor as $$
declare
ref_cursor refcursor := 'alldepart';
begin 
	open ref_cursor for (SELECT reimbursement.*, approvalprocess.*, reimbursementlocation.*, grade_format.*, event_type.*, approval.*, employees.*, supervisor.employees_id AS supervisorid, supervisor.email AS supervisoremail, supervisor.firstname AS supervisorfirstname, supervisor.lastname AS supervisorlastname, departmenthead.employees_id AS departmentheadid, departmenthead.employees_id AS departmentid, departmenthead.email AS departmentheademail, departmenthead.firstname AS departmentheadfirstname, departmenthead.lastname AS departmentheadlastname FROM ((((((((reimbursement INNER JOIN approvalprocess ON approvalprocess.approvalprocess_id=reimbursement.approvalprocess_id) INNER JOIN reimbursementlocation ON reimbursementlocation.reimbursementlocation_id=reimbursement.reimbursementlocation_id) INNER JOIN grade_format ON grade_format.grade_format_id=reimbursement.grade_format_id) INNER JOIN event_type ON event_type.eventtype_id=reimbursement.eventtype_id) INNER JOIN approval ON approval.approval_id=reimbursement.approval_id) INNER JOIN employees ON reimbursement.employee_id=employees.employees_id) LEFT JOIN employees supervisor ON reimbursement.supervisor_id=supervisor.employees_id) LEFT JOIN employees departmenthead ON reimbursement.departmenthead_id=departmenthead.employees_id) WHERE employees.department_id=p_departmentid AND status IN ('PENDING APPROVAL FROM DEPARTMENT HEAD', 'PENDING APPROVAL FROM BENEFITS COORDINATOR','REIMBURSEMENT AMOUNT ALTERED','APPROVED') ORDER BY employee_creation_date, employee_creation_time);
	return (ref_cursor);
end;
$$ language plpgsql;
-- select sp_select_all_department(p_departmentid in bigint);

/*
begin;
select sp_select_all_department(p_departmentid bigint);
fetch all in alldepart;
commit;
*/
--------------------------------------------------------
--  DDL for Procedure SP_SELECT_ALL_UNDERLING_REIM
--------------------------------------------------------
create or replace function sp_select_all_underling_reim(p_employeeid bigint) returns 
refcursor as $$
declare
ref_cursor refcursor := 'allunderling';
begin 
	open ref_cursor for (SELECT reimbursement.*, approvalprocess.*, reimbursementlocation.*, grade_format.*, event_type.*, approval.*, employees.*, supervisor.employees_id AS supervisorid, supervisor.email AS supervisoremail, supervisor.firstname AS supervisorfirstname, supervisor.lastname AS supervisorlastname, departmenthead.employees_id AS departmentheadid, departmenthead.employees_id AS departmentid, departmenthead.email AS departmentheademail, departmenthead.firstname AS departmentheadfirstname, departmenthead.lastname AS departmentheadlastname FROM ((((((((reimbursement INNER JOIN approvalprocess ON approvalprocess.approvalprocess_id=reimbursement.approvalprocess_id) INNER JOIN reimbursementlocation ON reimbursementlocation.reimbursementlocation_id=reimbursement.reimbursementlocation_id) INNER JOIN grade_format ON grade_format.grade_format_id=reimbursement.grade_format_id) INNER JOIN event_type ON event_type.eventtype_id=reimbursement.eventtype_id) INNER JOIN approval ON approval.approval_id=reimbursement.approval_id) INNER JOIN employees ON reimbursement.employee_id=employees.employees_id) LEFT JOIN employees supervisor ON reimbursement.supervisor_id=supervisor.employees_id) LEFT JOIN employees departmenthead ON reimbursement.departmenthead_id=departmenthead.employees_id) where employees.reportsto=p_employeeid order by employee_creation_date, employee_creation_time);
	return (ref_cursor);
end;
$$ language plpgsql;
-- select sp_select_all_underling_reim(p_employeeid bigint);

/*
begin;
select sp_select_all_underling_reim(p_employeeid bigint);
fetch all in allunderling;
commit;
*/
--------------------------------------------------------
--  DDL for Procedure SP_SELECT_ONE_REIMBURSEMENT
--------------------------------------------------------
create or replace function sp_select_one_reimbursement(p_reimbursementid bigint) returns 
refcursor as $$
declare
ref_cursor refcursor := 'onereimb';
begin 
	open ref_cursor for (SELECT * FROM (((((reimbursement INNER JOIN approvalprocess ON approvalprocess.approvalprocess_id=reimbursement.approvalprocess_id) INNER JOIN reimbursementlocation ON reimbursementlocation.reimbursementlocation_id=reimbursement.reimbursementlocation_id) INNER JOIN grade_format ON grade_format.grade_format_id=reimbursement.grade_format_id) INNER JOIN event_type ON event_type.eventtype_id=reimbursement.eventtype_id) INNER JOIN approval ON approval.approval_id=reimbursement.approval_id) WHERE reimbursement.reimbursement_id=p_reimbursementid ORDER BY employee_creation_date, employee_creation_time);
	return (ref_cursor);
end;
$$ language plpgsql;
-- select sp_select_one_reimbursement(p_reimbursementid bigint);

/*
begin;
select sp_select_one_reimbursement(p_reimbursementid bigint);
fetch all in onereimb;
commit;
 */
--------------------------------------------------------
--  DDL for Procedure SP_SELECT_REIMBURSEMENT
--------------------------------------------------------
create or replace function sp_select_reimbursement(inputemployeeid bigint) returns
refcursor as $$
declare
ref_cursor refcursor := 'reimb';
begin 
	open ref_cursor for (SELECT reimbursement.*, approvalprocess.*, reimbursementlocation.*, grade_format.*, event_type.*, approval.*, employees.*, supervisor.employees_id AS supervisorid, supervisor.email AS supervisoremail, supervisor.firstname AS supervisorfirstname, supervisor.lastname AS supervisorlastname, departmenthead.employees_id AS departmentheadid, departmenthead.employees_id AS departmentid, departmenthead.email AS departmentheademail, departmenthead.firstname AS departmentheadfirstname, departmenthead.lastname AS departmentheadlastname FROM ((((((((reimbursement INNER JOIN approvalprocess ON approvalprocess.approvalprocess_id=reimbursement.approvalprocess_id) INNER JOIN reimbursementlocation ON reimbursementlocation.reimbursementlocation_id=reimbursement.reimbursementlocation_id) INNER JOIN grade_format ON grade_format.grade_format_id=reimbursement.grade_format_id) INNER JOIN event_type ON event_type.eventtype_id=reimbursement.eventtype_id) INNER JOIN approval ON approval.approval_id=reimbursement.approval_id) INNER JOIN employees ON reimbursement.employee_id=employees.employees_id) LEFT JOIN employees supervisor ON reimbursement.supervisor_id=supervisor.employees_id) LEFT JOIN employees departmenthead ON reimbursement.departmenthead_id=departmenthead.employees_id) where reimbursement.employee_id=inputemployeeid order by employee_creation_date, employee_creation_time);
	return (ref_cursor);
end;
$$ language plpgsql;
-- select sp_select_reimbursement(inputemployeeid bigint);

/*
begin;
select sp_select_reimbursement(inputemployeeid bigint);
fetch all in reimb;
commit;
 */

--------------------------------------------------
--  DDL for Procedure SP_UPDATE_ALTER_REIM
--------------------------------------------------------
create or replace procedure sp_update_alter_reim(p_id IN bigint, p_alterAmount IN numeric(9,2), p_reason IN varchar)
language plpgsql as $$
begin
    UPDATE reimbursement SET adjustedcost=p_alterAmount, inflated_reimbursement_reason=p_reason, approval_id=(SELECT approval_id FROM approval WHERE status='REIMBURSEMENT AMOUNT ALTERED') WHERE reimbursement_id=p_id;
commit;
end
$$;
--------------------------------------------------------
--  DDL for Procedure SP_UPDATE_APPROVE_BENCO_REIM
--------------------------------------------------------
create or replace procedure sp_update_approve_benco_reim(p_id IN bigint)
language plpgsql as $$
begin
    UPDATE reimbursement SET approval_id=(SELECT approval_id FROM approval WHERE status='APPROVED') WHERE reimbursement_id=p_id;
commit;
end
$$;
--------------------------------------------------------
--  DDL for Procedure SP_UPDATE_APPROVE_DEPART_REIM
--------------------------------------------------------
create or replace procedure sp_update_approve_depart_reim(p_id IN bigint, p_departmenthead IN bigint)
language plpgsql as $$
begin
    UPDATE reimbursement SET departmenthead_id=p_departmenthead, approval_id=(SELECT approval_id FROM approval WHERE status='PENDING APPROVAL FROM BENEFITS COORDINATOR') WHERE reimbursement_id=p_id;
    UPDATE approvalprocess SET departmenthead_approve_date=CURRENT_DATE;
commit;
end
$$;
--------------------------------------------------------
--  DDL for Procedure SP_UPDATE_APPROVE_SUPERV_REIM
--------------------------------------------------------
create or replace procedure sp_update_approve_superv_reim(p_id IN bigint, p_supervisor IN bigint)
language plpgsql as $$
begin
    UPDATE reimbursement SET supervisor_id=p_supervisor, approval_id=(SELECT approval_id FROM approval WHERE status='PENDING APPROVAL FROM DEPARTMENT HEAD') WHERE reimbursement_id=p_id;
    UPDATE approvalprocess SET supervisor_approve_date=CURRENT_DATE;
commit;
end
$$;
--------------------------------------------------------
--  DDL for Procedure SP_UPDATE_DENY_BENCO_REIM
--------------------------------------------------------

create or replace procedure sp_update_deny_benco_reim(p_id IN bigint, p_bencoid IN bigint, p_approval IN VARCHAR, p_reason IN VARCHAR)
language plpgsql as $$
begin
    UPDATE reimbursement SET benco_id=p_bencoid, approval_id=(SELECT approval_id FROM approval WHERE status=p_approval), denyreason=p_reason WHERE reimbursement_id=p_id;
commit;
end
$$;
--------------------------------------------------------
--  DDL for Procedure SP_UPDATE_DENY_DEPARTMENT_REIM
--------------------------------------------------------
create or replace procedure sp_update_deny_department_reim(p_id IN bigint, p_departmenthead IN bigint, p_approval IN VARCHAR, p_reason IN VARCHAR)
language plpgsql as $$
begin
    UPDATE reimbursement SET departmenthead_id=p_departmenthead, approval_id=(SELECT approval_id FROM approval WHERE status=p_approval), denyreason=p_reason WHERE reimbursement_id=p_id;
commit;
end
$$;
--------------------------------------------------------
--  DDL for Procedure SP_UPDATE_DENY_SUPERVISOR_REIM
--------------------------------------------------------
create or replace procedure sp_update_deny_supervisor_reim(p_id IN bigint, p_supervisor IN bigint, p_approval IN VARCHAR, p_reason IN VARCHAR)
language plpgsql as $$
begin
    UPDATE reimbursement SET supervisor_id=p_supervisor, approval_id=(SELECT approval_id FROM approval WHERE status=p_approval), denyreason=p_reason WHERE reimbursement_id=p_id;
commit;
end
$$;
--------------------------------------------------------
--  DDL for Function getEmployee
--------------------------------------------------------
create or replace function get_employee(email varchar, pw varchar) 
returns table (emp_id bigint, emp_email varchar, emp_firstname varchar, emp_lastname varchar,emp_reportsto int,emp_departmentid bigint,emp_pw varchar) as $$
begin
	return query (select * from employees where employees.email=email and employees.pw = pw)
end;
$$ language plpgsql;

--select * from get_employee(?,?);
--------------------------------------------------------
--  DDL for Function createEmployee
--------------------------------------------------------
create or replace procedure create_employee(email varchar, passW varchar, firstName varchar, lastName varchar, reportsTo int, departmentId int, pk inout bigint)
language plpgsql as $$
begin
	insert into employees (email, firstname, lastname, reportsto, department_id, pw) values (email, firstName, lastName, reportsTo, departmentId, passW) returning employees.employees_id into pk;
commit;
end
$$;
-- call create_employee(?, ?, ?, ?, ?, ?, ?);
