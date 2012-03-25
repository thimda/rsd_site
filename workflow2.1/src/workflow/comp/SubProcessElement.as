package workflow.comp
{
	/**
	 * 子流程图元类
	 * */
	public class SubProcessElement extends BaseElement
	{
		public function SubProcessElement()
		{
			super();
			image.source = "images/unfold.png";
			this.toolTip = "子流程";	
		}
	}
}