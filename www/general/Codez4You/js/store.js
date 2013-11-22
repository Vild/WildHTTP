var store_cart = new Array();

function store_item(item, count) {
	this.item = item;
	this.count = count;
}

function Store_Init() {
	try {
		var products = document.getElementById("productsfeed");
		products.className;
	} catch (e) {
		return;
	}
	Store_AddProduct("PRODUCT NAME1", "100", "http://definewild.se/favicon.png", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris eu nunc lorem. Duis ornare dictum massa, eget egestas risus auctor vel. Curabitur accumsan nisi nulla, et convallis lorem tristique id. Suspendisse tristique nibh enim, molestie semper justo vulputate quis. Phasellus purus lectus, facilisis sit amet velit nec, tincidunt mollis lorem. In hac habitasse platea dictumst. Duis lacinia quis mauris eget laoreet. Nunc pellentesque iaculis pellentesque. Donec sed lorem feugiat, mollis mauris id, porta odio. Aliquam mollis consectetur dolor. Nulla facilisi. Morbi id risus interdum nunc vestibulum tempus. Donec scelerisque sapien metus. Nam laoreet vulputate sem, eu euismod justo scelerisque nec. Phasellus ut lacinia eros.");
	Store_AddProduct("PRODUCT NAME2", "20 000", "http://definewild.se/favicon.png", "Fusce dictum lacus felis, sed sollicitudin metus placerat vel. Fusce ullamcorper leo eget sem facilisis ornare. Mauris vehicula a nibh ac aliquam. Aenean lacinia sem at enim luctus sollicitudin. Mauris ultrices eros sit amet lacus porttitor laoreet. In congue ultrices felis eu tempor. Duis aliquet commodo sapien eu auctor. Phasellus nec rutrum dui. Nullam faucibus bibendum consectetur. Aenean risus neque, lacinia in lorem eget, mollis blandit est. Donec porttitor lorem nec diam tincidunt vulputate. Sed et tellus pulvinar, tempus libero ac, tempus nisl. Nam dapibus quis purus pulvinar tincidunt. Aenean et tempus purus, ut fringilla massa. Ut fermentum quam sed sem gravida, ac congue lorem pulvinar.");
	Store_AddProduct("PRODUCT NAME3", "300", "http://definewild.se/favicon.png", "Etiam dignissim lectus velit. Donec semper aliquam posuere. Praesent a dui sagittis, euismod massa quis, mollis nunc. Proin in mattis arcu, sit amet faucibus mi. Etiam vel suscipit eros. Donec elementum erat lobortis felis iaculis, vel pharetra lorem lacinia. Sed dictum ante vel imperdiet congue.");
	Store_AddProduct("PRODUCT NAME4", "1 400", "http://definewild.se/favicon.png", "Etiam tortor lectus, porttitor malesuada ullamcorper vel, pretium vitae ante. Aenean luctus, nulla ut vehicula lobortis, nisl arcu fermentum erat, in tincidunt lectus nisi eu augue. Sed commodo condimentum posuere. Etiam vitae odio a mi congue luctus. Donec arcu ligula, gravida sit amet erat a, tristique mattis turpis. Nunc elementum aliquet ornare. Vivamus congue lobortis pellentesque. Vestibulum convallis nunc eu porttitor elementum. Curabitur non ipsum non orci pharetra varius. Phasellus gravida pellentesque iaculis. Pellentesque venenatis enim ante, sed dignissim erat tincidunt quis. Sed convallis lectus auctor, scelerisque lorem ut, suscipit mauris.");
	Store_AddProduct("PRODUCT NAME5", "5 000", "http://definewild.se/favicon.png", "Phasellus sollicitudin mauris ut arcu laoreet, quis feugiat tellus condimentum. Praesent odio tortor, laoreet vel suscipit vitae, placerat at arcu. Duis massa nibh, pellentesque vel libero eget, aliquam malesuada enim. Aliquam sed pretium turpis. In sagittis odio lacus, vel commodo massa pretium sodales. Aliquam vel tristique lacus, vitae egestas libero. Proin faucibus cursus est. Sed hendrerit sed velit at porta. Suspendisse condimentum purus ut libero gravida pharetra. Etiam consequat sollicitudin risus, non congue nisi iaculis eget. Curabitur eu sem magna. Suspendisse lacinia odio quis est bibendum, in iaculis lacus eleifend. ");
	
	Store_Load();
}

var productidCount = 0;

function Store_AddProduct(name_, price_, img_, description_) {
	var product = document.createElement("div");
	product.className = "product";
	product.setAttribute("productid", productidCount++);
    
    var img = document.createElement("img");
    img.src = img_;
    img.alt = name;
    product.appendChild(img);

    var cartbutton = document.createElement("button");
    cartbutton.className = "cartbutton bluebutton";
    cartbutton.innerHTML = "Add to cart";
    product.appendChild(cartbutton);

	var price = document.createElement("span");
    price.className = "price";
    price.innerHTML = price_ + "€";
    product.appendChild(price);

	var title = document.createElement("span");
    title.className = "title";
    title.innerHTML = name_;
    product.appendChild(title);

    var description = document.createElement("span");
    description.className = "description";
    description.innerHTML = description_;
    product.appendChild(description);

    document.getElementById("products").appendChild(product);
}

function Store_Add(obj) {
	var found = false;
	doForEach(store_cart, function(item) {
		if (item.item === obj.getAttribute("productid")) {
			item.count++;
			found = true;
		}
	});

	if (!found)
		store_cart.push(new store_item(obj.getAttribute("productid"), 1));
	Store_Save();	
}

function Store_Save() {
	localStorage.store_cart = JSON.stringify(store_cart);
	document.getElementById("store_product_count").innerHTML = store_cart.length;
}

function Store_Load() {
	try {
		store_cart = JSON.parse(localStorage.store_cart)
	} catch(e){
		store_cart = new Array();
	}
		
	document.getElementById("store_product_count").innerHTML = store_cart.length;
}

function Store_Clean() {
	store_cart = new Array();
	Store_Save();
}