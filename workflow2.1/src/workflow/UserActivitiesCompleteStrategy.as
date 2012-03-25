package workflow
{
	public class UserActivitiesCompleteStrategy
	{
		//默认值为0:任意完成 一个
		public var taskFinishedStrategy:String = "0";
		//会签方式，0，并行，1,串行
		public var countersign_type:String = "0";
		public var taskFinishCount:String;
		public var taskFinishePercent:String;
		
		public function UserActivitiesCompleteStrategy()
		{
		}
	}
}