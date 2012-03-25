package utils
{
	public class IDGenerator
	{
		public function IDGenerator()
		{
		}
		public static function addPrefix(pre:String,id:String):String{
			return pre+id;
		}
		public static function delPrefix(pre:String,id:String):String{
			var index:int = id.indexOf(pre);
			if(index==-1)return id;
			return id.substring(index+pre.length,id.length);
		}
	}
}