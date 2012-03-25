package workflow.comp
{	
	/**
	 * 任务图元类
	 * */
	public class WorkElement extends BaseElement
	{
		public function WorkElement()
		{
			super();
			image.source ="images/task.png";
			this.toolTip = "任务 ";
		}
	}
}