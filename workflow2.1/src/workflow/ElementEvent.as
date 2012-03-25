package workflow
{
	import flash.events.Event;

	public class ElementEvent extends Event
	{
		public var srcElement:Element;
		public static  const ELEMENT_SELECT_CHANGED:String="ElementSelectChanged";
		public function ElementEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}