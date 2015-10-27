var modules = ['saleService',
	'productService',
	'taxService',
	'globals',
	'jQuery'
];


define(modules, function (saleService,productService,taxService,globals,$) {
	
	$('#tax_link').on('click', function() {
			taxService.load();
	});

	$('#product_link').on('click', function() {
			productService.load();
	});

	$('#sale_link').on('click', function() {
			saleService.load();
	});
	
	saleService.load();
});


