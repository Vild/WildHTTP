function show(item) {
	item.style.display = "inline";
	item.style.visibility = "visible";
}

function hide(item) {
	item.style.display = "none";
	item.style.visibility = "hidden";
}

function doForEach(items, fun) {
	for (var i = items.length - 1; i >= 0; i--) {
		fun(items[i]);
	}
}