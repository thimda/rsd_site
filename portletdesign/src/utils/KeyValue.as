package utils
{
	public class KeyValue
	{
		public var key:Object ;
		public var value:Object ;
		
		public 	function KeyValue(key:Object,value:Object){
				this.key = key;
				this.value = value;
			}
			public function toString():String{
				return key.toString()+":"+value.toString()
			} 
	}
}