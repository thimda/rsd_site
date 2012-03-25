package workflow
{
	public class UserActivitiesExcuter
	{
		//活动执行者,0:指定参与者，1:虚拟角色，2:同其他活动节点，3:由其他活动节点指派
		//public var excuter:String=""; 
		//pk
		public var excuteUser:String;
		public var excuteRole:String;
		public var excuteOrg:String;
		public var excuteDept:String;
		//名称,前台显示
		public var excuteUserName:String;
		public var excuteRoleName:String;
		public var excuteDeptName:String;
		public var excuteOrgName:String;
			
		public var virtualRole:String;
		
		public var sameAsOthers:String;
		
		public var asignByOthers:String;
		
		public var selfDefClass:String;
		
		public function UserActivitiesExcuter()
		{
		}
	}
}