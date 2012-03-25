package service
{
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.mxml.HTTPService;
	import mx.rpc.events.FaultEvent;
	import exception.HttpFaultUtil;
	import servlet.ServletConst;

	public class UserService
	{
		private var http:HTTPService  = new HTTPService();
		private var _users:XMLList ;
		
		[Bindable]
		public var mainApp:Object = null;  
		[Bindable]
		public var callbackFunction:Function;  //回调函数  
		
		public function UserService(mainApp:Object,callbaclFunction:Function)
		{
			this.mainApp = mainApp;
			this.callbackFunction = callbaclFunction;
			init();
		}
		private function init():void{
			http.url = ServletConst.PERSON_SERVLET_URL;
			http.resultFormat = "text";
			http.showBusyCursor = true;
			http.addEventListener(ResultEvent.RESULT,resultHandler);
			http.addEventListener(FaultEvent.FAULT,HttpFaultUtil.faultHandler);
		}
		
		private function resultHandler(event:ResultEvent):void{
			_users = new XMLList(event.result.toString());
			callbackFunction.call(mainApp,_users);
		}
		
		public function getAllForms():void{
			http.send();
			//return forms;
		}
		//pk之间以逗号分隔
		public function getUsersByPks(pks:String):void{
			http.url = ServletConst.PERSON_SERVLET_URL+"?pk_user="+pks;
			http.send();
		}
		public function getUsersByPk(pk:String):void{
			http.url = ServletConst.PERSON_SERVLET_URL+"?pk_user="+pk;
			http.send();
			//return forms;
		}
		public function get Users():XMLList{
			return _users;
		}
	}	
}