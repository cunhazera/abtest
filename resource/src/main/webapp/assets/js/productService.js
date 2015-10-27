var modules = [
	'jQuery',
	'globals',
	'jquery.autocomplete'
];

define(modules, function ($, globals) {

	function productService () {

		var exports = {};
		exports.load = load;

		function load() {
			globals.loadPage("ProductList.html",list);
		}

		function list() {
			$('#newProductButton').on('click', loadNewProductForm);
			globals.ajax({
				url : 'v1/products',
				type :"GET",
			 	success: createTable
			});
		}

		function loadNewProductForm() {
			globals.loadPage("ProductForm.html", initializeProductForm);
		}

		function initializeProductForm() {
			initAutocomplete();
			bindFormEvents();
		}

		function initAutocomplete() {
			$('#taxName').autocomplete({
			    serviceUrl: 'v1/taxes',
			    paramName: 'search',
			    transformResult: function(response) {
			        return {
			            suggestions: $.map(JSON.parse(response), function(tax) {
			                return { value: tax.name + " - " + tax.category + " - " + tax.percentage + "%", data: tax.id };
			            })
			        };
			    },
			    onSelect: function (suggestion) {
			        $('#idTax').val(suggestion.data);
			    }
			});
		}

		function bindFormEvents() {
			$('#saveProductButton').on('click', saveProduct);
		}

		function saveProduct() {
			globals.ajax({
				url : 'v1/products',
				type : 'POST',
				data : $('#productForm'),
			 	success: load
			});
		}

		function createTable(list) {
			var trHTML = '';
			$.each(list, function(i, item) {
				trHTML += 
				'<tr><td>' + item.id + '</td><td>' 
				+ item.name+ '</td><td>' 
				+ item.value+ '</td><td>' 
				+ item.tax.name+ '</td><td>' 
				+ item.tax.category+ '</td><td>' 
				+ item.tax.percentage
				+ '</td></tr>';
			});
			$('#productList').append(trHTML);
		}

		return exports;

	}
	
	return productService();

});


