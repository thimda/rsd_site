package workflow
{
	/**
	 * 活动节点基类，所有的具体活动继承此类
	 * */
	public class ActivitiesNode extends Node
	{
		public function ActivitiesNode(iDrawBoard:DrawBoard=null, iXML:XML=null)
		{
			super(iDrawBoard, iXML);
		}
	}
}