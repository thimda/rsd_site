function _hidMoreBar(id){
	var c = $("#"+id);
	var fun = c[0].hideMoreBar;
	c.find("[tp=hidMoreBar]").hide();
	c.find("[tp=showMoreBar]").show();
	if(fun)
		fun.call(c);
	
}
function _showMoreBar(id){
	var c = $("#"+id);
	var fun = c[0].showMoreBar;
	c.find("[tp=hidMoreBar]").show();
	c.find("[tp=showMoreBar]").hide();
	if(fun)
		fun.call(c);
}

/**
 * ×¢²á¸ü¶à°´Å¥
 */
function registerMoreBar(id, count, hideFun, showFun){
	var cspan = $("#"+id).find("[tp=count]");
	cspan.html(count);
	$("#"+id)[0].hideMoreBar = hideFun;
	$("#"+id)[0].showMoreBar = showFun;
}