$(function(){
	$('.approve').click(function(){
		let i_id = $(this).siblings('#approve-reimbursementId').val();
		let i_role = $('#approve-role').val();
		let i_approval = $('#approve-approval').val();
		let i_data = {reimbursementId : i_id, role: i_role, approval: i_approval};
		console.log(i_data);
		$.ajax({
			url:'updatestatus',
			method:'POST',
			data: i_data,
			success:function(){
				location.reload(true);
			}
		
		});
	});
	
	$('.change').click(function(){
		$('#projectedReimbursementAlter').val($('#projectedReimbursement').text().substring(1));
		$('#alterReimbursementId').val($(this).siblings('#approve-reimbursementId').val());
	});
	
	$('.deny').click(function(){
		let id = $(this).siblings('#approve-reimbursementId').val();
		$('#reimbursementId').val(id);
		
	});
	
	$('#logout').click(function(){
		$.ajax({
			url:'logout',
			method:'GET',
			success: function(){
				location.reload(true);
			}
		});
	});
	
});