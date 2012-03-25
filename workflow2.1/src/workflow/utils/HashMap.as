package workflow.utils
{
	import mx.utils.object_proxy;

	public class HashMap
	{
		private var array:Array = new Array();
		
		public function HashMap()
		{
		}
		
		public function put(key:Object,value:Object):void{
			var existValue:Object = get(key);
			if(existValue!=null){
				for each(var kv:KeyValue in array){
					if(kv.key==key){
						kv.value = value;
						return;
					}
				}
			}
			array.push(new KeyValue(key,value));
		}
		
		public function get(key:Object):Object{
			for each(var kv:KeyValue in array){
				if(kv.key==key){
					return kv.value;
				}
			}
			return null;
			}
		
		public function remove(key:Object):Object{
			for(var i:int;i<array.length;i++){
				var kv:KeyValue = array[i];
				if(kv.key==key){
					array.splice(i);
					return kv.value;
				}
			}
			return null;
		}
		
		public function entry():Array{
			var keys:Array = new Array();
			for each(var kv:KeyValue in array){
				keys.push(kv.key);
			}
			return keys;
		}
		
		public function clear():void{
			array.splice(0,array.length);
		}
	}		
}