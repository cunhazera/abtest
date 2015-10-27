var modules = [
	'jQuery',
	'globals'
];

define(modules, function ($, globals) {

	function saleService () {

		var exports = {};
		exports.load = load;

		function load() {
			globals.loadPage("SaleList.html",list);
		}

		function list() {
			$('#newSaleButton').on('click', loadNewSaleForm);
			globals.ajax({
				url : 'v1/sales',
				type :"GET",
			 	success: createTable
			});
		}

		function loadNewSaleForm(){
			globals.loadPage("SaleForm.html", initializeSaleForm);
		}

		function initializeSaleForm() {
			initAutocomplete();
			bindFormEvents();
		}

		function bindFormEvents(){
			$('#addProduct').on('click', addProduct);
			$('#quantity').on('change', calculateItemTotals);
			$('#value').on('change', calculateItemTotals);
			$('#saveSale').on('click', saveSale);
			
		}

		function addProduct() {
			var productItemElement = createProductItemElement();
			addProductToList(productItemElement);
			clearSaleItem();
			calculateTotals();
		}

		function clearSaleItem() {
			$('#quantity').val("");
			$('#tax').val("");
			$('#value').val("");
			$('#total').val("");
			$('#productName').val("")
			currentProduct = null;
		}

		function createProductItemElement() {
			var rowTemplate = $('#rowTemplate').clone();
			rowTemplate.removeClass('hidden').removeAttr('id');
			replaceRowValue(rowTemplate, 'saleItemId');
			replaceRowValue(rowTemplate, 'productName');
			replaceRowValue(rowTemplate, 'quantity');
			replaceRowValue(rowTemplate, 'value');
			replaceRowValue(rowTemplate, 'tax');
			replaceRowValue(rowTemplate, 'total');
			$('.productId', rowTemplate).text(currentProduct.id);
			return rowTemplate;
		}

		function replaceRowValue(template, selector) {
			$('.'+selector, template).text($('#'+ selector).val());
		}

		function addProductToList(productElement) {
			$('#saleProductsList').append(productElement);
		}

		var currentProduct;
		function initAutocomplete() {
			$('#productName').autocomplete({
			    serviceUrl: 'v1/products',
			    paramName: 'search',
			    transformResult: function(response) {
			        return {
			            suggestions: $.map(JSON.parse(response), function(product) {
			                return { value: product.name + " - " + product.tax.name + " - " + product.tax.category + " - " + product.tax.percentage + "%", data: product };
			            })
			        };
			    },
			    onSelect: function (suggestion) {
			    	currentProduct = suggestion.data;
			    	$('#value').val(suggestion.data.value);
		        	calculateItemTotals();
			    }
			});
		}

		function saveSale(){
			var items = [];
			$('#saleProductsList > tr').each(function() {
				if(this.id == 'rowTemplate')
					return;
				var item = {};
				item.value = $('.value',this).text();
				item.product = { id: $('.productId',this).text() };
				item.quantity = $('.quantity',this).text();
				items.push(item);
		     });
			
			globals.ajax({
				url : 'v1/sales',
				type : 'POST',
				data : JSON.stringify({items}),
			 	success: load
			})
		}
		
		function calculateItemTotals(){
	        
	        var quantity = $('#quantity');
	        if(!quantity.val())
	        	quantity.val(1);
	        
	        var value = $('#value');
	        if(!value.val())
	        	value.val(0);
	        
	        var totalTax = quantity.val() * value.val() * (currentProduct.tax.percentage / 100);
	        $('#tax').val(totalTax);
	        $('#total').val(quantity.val() * value.val());
		}
		
		function calculateTotals(){
			var taxTotal = 0.0;
			var saleTotal = 0.0;
			$('#saleProductsList > tr').each(function() {
				var value = $('.total',this).text();
				var tax = $('.tax',this).text();
				if(value)
					saleTotal = saleTotal + parseFloat(value);
		        if(tax)
		        	taxTotal = taxTotal + parseFloat(tax);
		     });
			 $('#taxTotal').val(taxTotal);
			 $('#totalSale').val(saleTotal);
		}

		function createTable(list) {
			var trHTML = '';
			$.each(list, function(i, item) {
				trHTML += '<tr><td>' + item.id + '</td>'
				+'<td>' + getProductsList(item.items) + '</td>'
				+ '<td>' + item.total.total
				+ '</td><td>' + item.total.taxTotal + '</td></tr>';
			});
			$('#records_table').append(trHTML);
		}
		return exports;
		
		function getProductsList(items){
			var productsNames = '';
			$.each(items, function(i, item) {
				productsNames = ' '+productsNames + item.product.name+ ",";
			});
			return productsNames.substring(0, productsNames.length - 1);
		}

	}
	
	return saleService();

});


