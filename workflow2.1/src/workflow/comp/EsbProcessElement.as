package workflow.comp
{
	/**
	 * Esb子流程图元类
	 * */
	public class EsbProcessElement extends BaseElement
	{
		public function EsbProcessElement()
		{
			super();
			image.source = "images/unfold.png";
			this.toolTip = "ESB子流程";	
		}
	}
}