function getServerWebRoot(){
	var url = window.location;
	var protocol = url.protocol;
	var host = url.host;
	var fullUrl = protocol + "//" + host;
	fullUrl += webContextPath;
	return fullUrl;
} 

function saveXml(xmlString){
		parent.saveXml(xmlString);
}

function getUrl(){
	//alert(1);
//alert(window.location.href);
   var url = window.location.href;
   return url;
   }