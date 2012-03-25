package context
{
	public class WfContext
	{
		private static var _pk_group:String;
		private static var _pk_formdefinition:String;
		
		public static function set Pk_group(value:String):void{
			_pk_group = value;
		}
		public static function get Pk_group():String{
			return _pk_group;
		}
		public static function set Pk_formdefinition(value:String):void{
			_pk_formdefinition = value;
		}
		public static function get Pk_formdefinition():String{
			return _pk_formdefinition;
		}
	}
}