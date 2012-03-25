var cainited = false;
function initCtrl() {
	var obj = document.createElement("object");
	obj.id = "baseca";
	obj.classid = "clsid:9C879779-5C5F-4510-88B8-5BA18EEAA931";
	document.appendChild(obj);

	window.MyFilter=baseca.MyFilter;
	window.certs=baseca.MyCertificates;
	window.cert=baseca.MyCertificate;
	window.Collection=baseca.MyCollection;
	window.CertOther=baseca.MyCertOther;
	cainited = true;
}		

function getUser() {
	return GetCertDNItemByName("O");
}

function GetCertDNItemByName(Item){
	return cert.GetCertDNItemByName(Item);
}


function sign(oridata) {
	try{
		if(!cainited)
			initCtrl();
		//枚举是否有证书,如果有多个证书会弹出选择框,如果只有一个证书则不会弹出选择框
		certs.NewEnum(MyFilter);
		cert = certs.GetTheChooseCert();	
		//判断KEY是否插入到电脑中
		var Container = cert.Container;
		//如果返回值为空字符	
		if(Container=="")
		{
			//提示用户没插KEY
			alert("输入key先");
		}
		else
		{
			//取得用户唯一标识, 1.2.86.21.1.4为东方中讯专门给轻轨项目设定的OID键,无需进行修改直接调用
			Oid =cert.GetCertExtension('1.2.86.21.1.4');
			//由系统判断用户在系统的权限
		
			//对数据进行签名
			//如果签名返回值-1为签名失败
			var signData=cert.SignDataByP7(oridata, 1);
			//将signData 值传入服务器后台进行验证操作
			return signData;
		}
	}
	catch(error)
	{
		//提示没有安装驱动
		alert("读取key出错");
	}
}

function keyready() {
	if(!cainited)
		initCtrl();
	//枚举是否有证书,如果有多个证书会弹出选择框,如果只有一个证书则不会弹出选择框
	certs.NewEnum(MyFilter);
	cert = certs.GetTheChooseCert();	
	//判断KEY是否插入到电脑中
	var Container = cert.Container;
	//如果返回值为空字符	
	if(Container=="")
	{
		return false;
	}
	else
	{
		return true;
	}
}