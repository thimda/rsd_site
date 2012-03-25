package service
{
	import exception.HttpFaultUtil;
	
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.mxml.HTTPService;
	
	import servlet.ServletConst;

	public class ProdefService 
	{
		private var http:HTTPService  = new HTTPService();
		private var _prodefs:XMLList ;
		
		[Bindable]
		public var mainApp:Object = null;  
		[Bindable]
		public var callbackFunction:Function;  //回调函数  
		
		public function ProdefService(mainApp:Object,callbaclFunction:Function)
		{
			this.mainApp = mainApp;
			this.callbackFunction = callbaclFunction;
			init();
		}
		private function init():void{
			http.url = ServletConst.PROCESS_SERVLET_URL;
			http.resultFormat = "text";
			http.showBusyCursor = true;
			http.addEventListener(ResultEvent.RESULT,resultHandler);
			http.addEventListener(FaultEvent.FAULT,HttpFaultUtil.faultHandler);
		}
		
		private function resultHandler(event:ResultEvent):void{
			_prodefs = new XMLList(event.result.toString());
			callbackFunction.call(mainApp,_prodefs);
		}
		
		public function getAllProdefs():void{
			http.send();
			//return forms;
		}
		public function getProdefsByPks(pks:String):void{
			http.url = ServletConst.PROCESS_SERVLET_URL+"?pk_prodef="+pks;
			http.send(pks);
			//return forms;
		}
		public function get Prodefs():XMLList{
			return _prodefs;
		}
	}	
}