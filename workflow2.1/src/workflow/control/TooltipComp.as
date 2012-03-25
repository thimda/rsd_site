package workflow.control
{
	import mx.containers.Canvas;
	import mx.controls.Text;

	/**
	 * 提示信息控件
	 * */
	public class TooltipComp extends Canvas
	{
		private var _tooltip_text:String = "测试";
		private var lab:Text = new Text();
		
		public function TooltipComp()
		{
			this.setStyle("backgroundColor",0xabcdef);
			lab.text = Tooltip_text;
			lab.x = 0;
			lab.y = 0;
			this.addChild(lab);
		}
		
		public function set Tooltip_text(value:String):void{
			this._tooltip_text = value;
		}
		public function get Tooltip_text():String{
			return _tooltip_text;
		}
	}
}