package workflow
{
	/**
	 * 子流程基类
	 * */
	public class ProcessNode extends DrawBoard
	{
		public function ProcessNode(iDrawBoard:DrawBoard=null, iXML:XML=null)
		{
			super(iDrawBoard, iXML);
		}
		//产生界面元素id
		override public function GetNewElementID():String{
			var maxID:String = super.GetNewElementID();
			return this.ID+"_"+maxID;
		}
	}
}