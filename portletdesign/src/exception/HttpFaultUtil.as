package exception
{
	import mx.rpc.events.FaultEvent;
	import mx.controls.Alert;
	
	/**
	 * HttpService请求失败提示
	 * */
	public class HttpFaultUtil
	{
		public function HttpFaultUtil()
		{
		}
		public static function faultHandler(event:FaultEvent):void{
			Alert.show("服务请求失败:\n"+event.fault.faultDetail.substring(event.fault.faultDetail.lastIndexOf("URL"),event.fault.faultDetail.length));
		}
	}
}