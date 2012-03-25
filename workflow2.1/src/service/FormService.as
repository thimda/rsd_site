package service
{
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.mxml.HTTPService;
	import mx.rpc.events.FaultEvent;
	import exception.HttpFaultUtil;
	
	import servlet.ServletConst;

	public class FormService
	{
		private var http:HTTPService  = new HTTPService();
		private var _forms:XMLList ;
		
		[Bindable]
		public var mainApp:Object = null;  
		[Bindable]
		public var callbackFunction:Function;  //回调函数  
		
		public function FormService(mainApp:Object,callbaclFunction:Function)
		{
			this.mainApp = mainApp;
			this.callbackFunction = callbaclFunction;
			init();
		}
		private function init():void{
			http.url = ServletConst.RELATEFORM_SERVLET_URL;
			http.resultFormat = "text";
			http.showBusyCursor = true;
			http.addEventListener(ResultEvent.RESULT,resultHandler);
			http.addEventListener(FaultEvent.FAULT,HttpFaultUtil.faultHandler);
		}
		
		private function resultHandler(event:ResultEvent):void{
			_forms = new XMLList(event.result.toString());
			callbackFunction.call(mainApp,_forms);
		}
		
		public function getAllForms():void{
			http.send();
			//return forms;
		}
		public function getFormsByPk(pk:String):void{
			http.url = ServletConst.RELATEFORM_SERVLET_URL+"?pk_form="+pk;
			http.send(pk);
			//return forms;
		}
		public function get Forms():XMLList{
			return _forms;
		}
	}	
}