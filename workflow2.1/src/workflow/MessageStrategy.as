package workflow
{
	public class MessageStrategy 
	{
		public var allowControl:Boolean;
		public var unit:String;
		public var aheadTime:String;
		public var workTime:String;
		
		public var remindType:String;
		public var handlerType:String;
		
		//任务创建提醒方式,如：0:邮件，1：手机,2，...
		public var createdMsg:Array = new Array();		
		//任务完成提醒方式,如：0:邮件，1：手机,2，...
		public var completedMsg:Array = new Array();
		//超时提醒方式,如：0:邮件，1：手机,2，...
		public var overtimeMsg:Array = new Array();		
		//超时动作,如：0:邮件，1：手机,2，...
		public var overtimeAct:Array = new Array();
		
		public function MessageStrategy()
		{
		}
	}
}