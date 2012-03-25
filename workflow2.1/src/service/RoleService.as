package service
{
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.mxml.HTTPService;
	import mx.rpc.events.FaultEvent;
	import exception.HttpFaultUtil;
	
	import servlet.ServletConst;

	public class RoleService
	{
		private var http:HTTPService  = new HTTPService();
		private var _roles:XMLList ;
		
		[Bindable]
		public var mainApp:Object = null;  
		[Bindable]
		public var callbackFunction:Function;  //回调函数  
		
		public function RoleService(mainApp:Object,callbaclFunction:Function)
		{
			this.mainApp = mainApp;
			this.callbackFunction = callbaclFunction;
			init();
		}
		private function init():void{
			http.url = ServletConst.ROLE_SERVLET_URL;
			http.resultFormat = "text";
			http.showBusyCursor = true;
			http.addEventListener(ResultEvent.RESULT,resultHandler);
			http.addEventListener(FaultEvent.FAULT,HttpFaultUtil.faultHandler);
		}
		
		private function resultHandler(event:ResultEvent):void{
			_roles = new XMLList(event.result.toString());
			callbackFunction.call(mainApp,_roles);
		}
		
		public function getAllRoles():void{
			http.send();
		}
		public function getRolesByPks(pks:String):void{
			http.url = ServletConst.ROLE_SERVLET_URL+"?pk_role="+pks;
			http.send();
		}
		public function getRolesByPk(pk:String):void{
			http.url = ServletConst.ROLE_SERVLET_URL+"?pk_role="+pk;
			http.send();
		}
		public function get Roles():XMLList{
			return _roles;
		}
	}	
}