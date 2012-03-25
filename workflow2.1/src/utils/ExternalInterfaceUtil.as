package utils
{
	import exception.RuntimeError;
	
	public class ExternalInterfaceUtil
	{
		public function ExternalInterfaceUtil()
		{
		}
		public function call(externalMethod:String):Object{
			if(ExternalInterface.available==false){
				throw new RuntimeError("当前环境不支持flex外部通信");
			}
			var value:Object =  ExternalInterface.call(externalMethod);
			return value;
		}
	}
}