define(['jQuery'], function($){
	var globals = {};
	globals.ajax = ajax; 
	globals.loadPage = loadPage; 

	function ajax(params){
		var request = {
			url :  params.url,
			type :params.type,
			contentType : "application/json; charset=utf-8",
		 	success: params.success,
		  	error : onError
		};
		if(params.data)
			request.data = formToJSON(params.data);
		$.ajax(request);
	};

	function loadPage(page, callBack){
		$("#content").load("assets/html/"+page,callBack);
	}

	function onError(result) {
		var response = result.responseText;
		$('.alert-danger').text(response);
		$('.alert-danger').removeClass('hide');
		$('.alert-danger').removeAttr('style');
		$('.alert-danger').delay(2000).fadeOut("slow", function () { $(this).addClass('hide')});
	}

	function formToJSON(form) {
		if(typeof form == "string")
			return form;
		var o = {};
		$('.ignore', form).remove();
	    var formArray = form.serializeArray();
	    $.each(formArray, function(el) {
	        if (o[this.name] !== undefined) {
	            if (!o[this.name].push) {
	                o[this.name] = [o[this.name]];
	            }
	            o[this.name].push(this.value || '');
	        } else {
	        	if(this.name && this.name.indexOf(".") > 0){
	        		var index = this.name.indexOf(".");
	        		var entity = this.name.substr(0, index);
	        		var key = this.name.substr(index+1);
	        		o[entity] =  JSON.parse('{"'+ key +'": '+ this.value+'}') ;
	        	} else
	            	o[this.name] = this.value || '';
	        }
	    });
	    return JSON.stringify(o);
	};

	return globals;
});