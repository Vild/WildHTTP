var productsfeedIndex = 0;

window.onload = function() {
	Store_Init();
	setUpProductsfeed();
}

function setUpProductsfeed() {
	var products;
	try {
		products = document.getElementById("productsfeed");
	} catch (e) {
		return;
	}
	products.onmouseover = function(event) {clearTimeout(productsfeedTimer);};
	products.onmouseout = function(event) {productsfeedTimer = setTimeout("nextProduct();", 1000);};
	doForEach(products.getElementsByClassName("product"), function(item) {
		doForEach(item.getElementsByClassName("cartbutton"), function(a) {
			a.onclick = function(event) {Store_Add(item);}
		});
		hide(item);
	});
	show(products.getElementsByClassName("product")[productsfeedIndex]);
	productsfeedTimer = setTimeout("nextProduct();", 1000);
}

function nextProduct() {
	if (productsfeedTimer) {
		var products = document.getElementById("productsfeed");
		productsfeedIndex++;
		if (productsfeedIndex >= products.getElementsByClassName("product").length)
			productsfeedIndex = 0;
		doForEach(products.getElementsByClassName("product"), function(item){hide(item);});
		show(products.getElementsByClassName("product")[productsfeedIndex]);
	}
	productsfeedTimer = setTimeout("nextProduct();", 1000);
}

