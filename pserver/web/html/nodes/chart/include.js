function chart_function_createtab(src){
	var proxy = new ServerProxy(null,null,false); //代理类
	proxy.addParam('clc', 'a.TestController' );//控制类
	proxy.addParam('m_n', 'onPopViewByHyperLink');//方法名
	proxy.addParam('src',src);//参数
	proxy.execute();
}