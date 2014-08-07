/**
 * 
 */
$(document).ready(function(){	
		
	loadingStart = function() { 
		$("body").addClass("loading");    
	};
	loadingStop= function()   { 
		$("body").removeClass("loading"); 
	};

	$('.editable').each(function(){
		this.contentEditable = true;
	});
		
	/* form submit */

	$('#send-feedBack-btn').click(function(){	
		

		$("#msg").html('');
		
		$('#textMsg').val($('#editable').html());
		
		var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;  
		var nameReg = /^[A-Za-z\s]+$/;	
		
		//..name
		if(feedBackForm.name.value=="")
		{
			$("#msg").html("Name field should not be blank");
			feedBackForm.name.focus();
			return false;
		}
		if (nameReg.test(feedBackForm.name.value) == false) 
		{
			$("#msg").html('Name should be alphabetical.');
			feedBackForm.name.focus();	
			return false;
		}	


		//..email
		if(feedBackForm.email.value=="")
		{
			$("#msg").html("email should not be blank");
			feedBackForm.email.focus();
			return false;
		}

		if (reg.test(feedBackForm.email.value) == false) 
		{
			$("#msg").html('Invalid email address');
			feedBackForm.email.focus();	
			return false;
		}


		//..textMsg
		if(feedBackForm.textMsg.value=="")
		{
			$("#msg").html("Message should not be blank");
			feedBackForm.textMsg.focus();
			return false;
		}			

		var message=$('#editable').html();			

		if( (message.indexOf("xxxxx") != -1)||(message.indexOf("XXXXX") != -1)){

			$("#msg").html("Enter proper value in place of xxxxx");
			$('#editable').focus();
			return false;
		}	

		var url="../service/sendEmail";

		loadingStart();

		$.ajax({		  
			url:url,
			type:"POST",
			dataType: "json",
			async:true,
			data:$("#feedBackForm").serialize(),			
			success: function(data) { 

				loadingStop();
				if(data.error != undefined)
				{
					$("#msg").html("Error");
				}else
				{	        		 

					$("#success-msg").html(data.output);

					//clearing all fields....
					
					$("#name").val('');
					$("#email").val("");	
					$("#mobile").val("");
					$("#editable").html(''); 
					$("#msg").html('');        				

				}         		

			},
			error:function(e)
			{
				loadingStop();
				$("#msg").html("service unavailable");
			}
		});  

	});
	

});
