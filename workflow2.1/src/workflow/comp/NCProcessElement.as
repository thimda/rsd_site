package workflow.comp
{
	/**
	 * NC子流程图元类
	 * */
	public class NCProcessElement extends BaseElement
	{
		public function NCProcessElement()
		{
			super();
			image.source = "images/fold.png";
			this.toolTip = "NC子流程";	
		}
	}
}