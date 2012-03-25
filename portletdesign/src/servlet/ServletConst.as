package servlet
{
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.utils.URLUtil;
	
	/**
	 * 与后台交互的servlet路径
	 * */
	public final class ServletConst
	{		
		private static var _server_web_root:String = ""; //http://localhost/pbase
		private static const urlPrefix:String = "pt";
		//portlet分类加载
		public static function get PORTLETCATES_SERVLET_URL():String {
			return Server_web_root+"/servlet/portletCatesServlet/doPost";
		}
		//portlet加载
		public static function get PORTLET_SERVLET_URL():String {
			return Server_web_root+"/servlet/portletLoadServlet/doPost";
		}
		//template加载
		public static function get THEAM_SERVLET_URL():String {
			return Server_web_root+"/servlet/theamServlet/doPost";
		}
		//skin加载
		public static function get SKIN_SERVLET_URL():String {
			return Server_web_root+"/servlet/skinServlet/doPost";
		}
		public static function get LAYOUT_SERVLET_URL():String {
			return Server_web_root+"/servlet/layoutLoadServlet/doPost";
		}
		//新建保存xml
		public static function get SAVE_NEW_XML_SERVLET_URL():String {
			return _server_web_root+"/pt/portalpage/doNew";
		}
		//修改保存xml
		public static function get SAVE_EDIT_XML_SERVLET_URL():String {
			return _server_web_root+"/pt/portalpage/doEdit";
		}
		//加载portlet设计xml
		public static function get GETXML_SERVLET_URL():String {
			return _server_web_root+"/pt/portalpage/getPml";
		}
		//导出xml(先到服务器，再下载到本地)
		public static function get OUTPUTXML_SERVLET_URL():String {
			return _server_web_root+ "/servlet/outputXmlServlet/doPost";
		}
		//导出图片(先保存到服务器，再下载到本地)
		public static function get OUTPUTIMAGE_SERVLET_URL():String {
			return _server_web_root+ "/servlet/outputImageServlet/doPost";
		}
		//保存为图片到服务器
		public static function get SAVEIMAGE_SERVLET_URL():String {
			return _server_web_root+ "/servlet/saveImageServlet/doPost";
		}
		//获取所有已经存在的页面
		public static function get GETALLPAGES_SERVLET_URL():String {
			return Server_web_root+"/servlet/pagesServlet/doPost";
		}
		public static function get GETALLDEVICES_SERVLET_URL():String{
			return Server_web_root+"/servlet/deviceServlet/doPost";
		}
		public static  function set Server_web_root(value:String):void{
			_server_web_root = value;
		}
		public static function get Server_web_root():String{
			return _server_web_root+"/"+urlPrefix;
		}
	}
}