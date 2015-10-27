var modules = [
	'jQuery',
	'globals'
];

define(modules, function ($, globals) {
	function taxListService () {
		var exports = {};
		exports.load = load;

		function load() {
			globals.loadPage("TaxList.html", list);
		}

		function list() {
			$('#newTaxButton').on('click', loadTaxForm);
			globals.ajax({
				url : "v1/taxes",
				success : onLoadList
			});
		}

		function onLoadList(result) {
			createTable(result);
		}

		function loadTaxForm() {
			globals.loadPage("TaxForm.html", bindEvents);
		}

		function bindEvents() {
			$('#submitTaxButton').on('click', onClickPostTax);
		}

		function onClickPostTax() {
			onClickSubmitTax('POST');
		}

		function onClickPutTax(){
			onClickSubmitTax('PUT');
		}

		function onClickSubmitTax(type){
			globals.ajax({
				url : 'v1/taxes',
				type : type,
				data : $('#taxForm'),
			 	success: load
			})
		}

		function createTable(list) {
			var trHTML = '';
			$.each(list, function(i, item) {
				trHTML += '<tr><td class="itemId">' + item.id + '</td><td>' + item.name + '</td><td>'
						+ item.category + '</td><td>' + item.percentage
				'</td></tr>';
			});
			$('#taxList').append(trHTML);
			bindTableRowClick();
		}

		function bindTableRowClick(){
			$('#taxList tr').on('click', onRowClic);
		}

		function onRowClic() {
			var itemId = $('.itemId', this).text();
			loadById(itemId);
		}

		function loadById(itemId){
			globals.ajax({
				url : "v1/taxes/"+itemId,
				success : loadTax
			})
		}

		function loadTax(tax){
			globals.loadPage("TaxForm.html",function(){
				$('#submitTaxButton').on('click', onClickPutTax);
				var deleteElement = $('#deleteTaxButton');
				deleteElement.on('click', deleteTax);
				deleteElement.removeAttr("disabled");

				var form = $('#taxForm');
				$.each(tax,function(key,value) {
			         form.find("input[name='"+key+"']").val(value);
			    });
			});
		}

		function deleteTax(){
			globals.ajax({
				url : 'v1/taxes/' + $('#idTax').val(),
				type : 'DELETE',
			 	success: load
			})
		}
		return exports;

	}
	return taxListService();
});