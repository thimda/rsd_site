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
		//http://localhost 需要替换为服务器地址
		//加载group
		public static function get GROUP_SERVLET_URL():String {
			return _server_web_root+"/servlet/groupServlet";
		}
		//加载group-org 树
		public static function get GROUP_ORG_TREE_SERVLET_URL():String {
			return _server_web_root+"/servlet/grouporgTreeServlet";
		}
		//加载person
		public static function get PERSON_SERVLET_URL():String {
			return _server_web_root+"/servlet/userServlet";
		}
		//加载角色组树(第一级是组织，第二级是角色组)
		public static function get ROLEGROUP_SERVLET_URL():String{
			return _server_web_root+"/servlet/rolegroupTreeServlet";
		}
		//加载role
		public static function get ROLE_SERVLET_URL():String{
			return _server_web_root+"/servlet/roleServlet";
		}
		//加载subprocess
		public static function get PROCESS_SERVLET_URL():String{
			return _server_web_root+"/servlet/processServlet";
		}
		//加载org
		public static function get  ORG_SERVLET_URL():String {
			return _server_web_root+"/servlet/orgServlet";
		}
		//加载dept
		public static function get  DEPT_SERVLET_URL():String{
			return _server_web_root+"/servlet/deptServlet";
		}
		//加载form
		public static function get  RELATEFORM_SERVLET_URL():String {
			return _server_web_root+"/servlet/formServlet";
		}
		
		//xml文件导入到服务器上地址由该servlet决定
		public static function get UPLOAD_SERVLET_URL():String{
			return _server_web_root+"/servlet/uploadController";
		}
		
		//导入的xml文件可以从这个地址加载到内存(这个地址与uploadController中的地址一致)
		public static function get LOAD_URL():String {
			return _server_web_root+ "/processxml/WorkFlow.xml";
		}
		
		//保存为xml到服务器
		public static function get SAVEXML_SERVLET_URL():String {
			return _server_web_root+"/servlet/saveXmlToFileServlet";
		}
		//保存为图片到服务器
		public static function get SAVEIMAGE_SERVLET_URL():String {
			return _server_web_root+ "/servlet/saveImageServlet";
		}
		
		//导出xml(先到服务器，再下载到本地)
		public static function get OUTPUTXML_SERVLET_URL():String {
			return _server_web_root+ "/servlet/outputXmlServlet";
		}
		//导出图片(先保存到服务器，再下载到本地)
		public static function get OUTPUTIMAGE_SERVLET_URL():String {
			return _server_web_root+ "/servlet/outputImageServlet";
		}
		
		//加载表单元素
		public static function get GET_FORMFIELD_SERVLET_URL():String {
			return _server_web_root+ "/servlet/formFieldServlet";
		}
		
		//加载流程定义xml
		public static function get LOAD_WFXML_SERVLET_URL():String{
			return _server_web_root+"/servlet/workflowService";
		}		
		//从后台加载流程实例
		public static function get LOAD_INS_SERVLET_RUL():String {
			return _server_web_root+ "/servlet/runStateServlet";
		}	
		//消息提醒方式servlet
		public static function get REMINDERTYPE_SERVLET_URL():String{
			return _server_web_root+ "/servlet/reminderServlet";
		}
		
		public static  function set Server_web_root(value:String):void{
			_server_web_root = value;
		}
		public static function get Server_web_root():String{
			return _server_web_root;
		}
		
	}
}