package service
{
	import mx.rpc.events.ResultEvent;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.http.HTTPService;
	
	import exception.HttpFaultUtil;
	
	import servlet.ServletConst;

	public class OrgService
	{
		private var http:HTTPService  = new HTTPService();
		private var _orgs:XML ;
		
		[Bindable]
		public var mainApp:Object = null;  
		[Bindable]
		public var callbackFunction:Function;  //回调函数  
		
		public function OrgService(mainApp:Object,callbaclFunction:Function)
		{
			this.mainApp = mainApp;
			this.callbackFunction = callbaclFunction;
			init();
		}
		private function init():void{
			http.url = ServletConst.ORG_SERVLET_URL;
			http.resultFormat = "text";
			http.showBusyCursor = true;
			http.addEventListener(ResultEvent.RESULT,resultHandler);
			http.addEventListener(FaultEvent.FAULT,HttpFaultUtil.faultHandler);
		}
		
		private function resultHandler(event:ResultEvent):void{
			_orgs = new XML(event.result.toString());
			callbackFunction.call(mainApp,_orgs);
		}
		
		public function getAllOrgs():void{
			http.send();
		}
		public function getOrgsByPk(pk:String):void{
			http.url = ServletConst.ORG_SERVLET_URL+"?pk_org="+pk;
			http.send();
		}
		public function getOrgsByPks(pks:String):void{
			http.url = ServletConst.ORG_SERVLET_URL+"?pk_org="+pks;
			http.send();
		}
		public function get Orgs():XML{
			return _orgs;
		}
	}	
}