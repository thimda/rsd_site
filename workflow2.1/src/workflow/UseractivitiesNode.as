package workflow
{
	import context.WfContext;
	
	import flash.display.Graphics;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.controls.Image;
	import mx.managers.ToolTipManager;
	
	import service.FormService;
	import service.OrgService;
	import service.RoleService;
	import service.UserService;
	
	import workflow.control.HTMLToolTip;
	import workflow.decorator.DrawLineDecorator;
	import workflow.decorator.SelectDecorator;
	import workflow.utils.HashMap;
	
	/**
	 * 用户活动
	 * */
	public class UseractivitiesNode extends ActivitiesNode
	{
		//用户活动属性
		//表单(pk,name)
		public var form:Object = {pk_formdefinition:null,formName:null};
		//描述
		public var description:String;
		//执行策略
		public var excuterTrategy:UserActivitiesExcuter = new UserActivitiesExcuter();
		//分配策略
		public var assignTrategy:UserActivitiesAssignTrategy = new UserActivitiesAssignTrategy();
		//完成策略
		public var completeTrategy:UserActivitiesCompleteStrategy = new UserActivitiesCompleteStrategy();
		//回退策略
		public var backTrategy:UserActivitiesBackTrategy = new UserActivitiesBackTrategy();
		//消息策略
		public var messageStrategy:MessageStrategy = new MessageStrategy();
		//活动类型，默认是0:Normal,1,Audit,2,Cirulated
		public var actionType:int = 1;
		//自定义事件处理类
		public var delegatorClass:String;
		//public var afterClass:String;
		//是否允许加签,0，是，1，否
		public var allowAddSign:String = "1";
		//是否允许转发,0，是，1，否
		public var allowTransmit:String = "1";
		//是否由上一步指派,0，是，1，否
		public var allowPreAssign:String = "1";
		//是否允许传阅,0，是，1，否
		public var allowDeliver:String = "1";
		//是否允许打印，默认允许
		public var allowPrint:String = "0";
		//是否同部门限定,0，是，1，否
		public var allowResDept:String = "1";
		//是否可以编辑意见,0，是，1，否
		public var allowEditOpinion:String = "0";
		//是否可以催办,0，是，1，否
		public var allowUrge:String = "0";
		
		//可发起子流程(pk,name)
		public var startProdefs:Object = {pk_prodef:null,prodefName:null};
		
		//以{column1:fieldname,column2:ext1}的形式
		[Bindable]   
		public var extArr:ArrayCollection = new ArrayCollection();   
		
		public function UseractivitiesNode(iDrawBoard:DrawBoard, iXML:XML=null)
		{
			super(iDrawBoard, iXML);
			this.width=100;
			this.height=50;
			this.Name="人工活动";
			decorator = new SelectDecorator(this);
			drawLineDec = new DrawLineDecorator(this);
		}
		
		private function setImage():void{
			var image:Image = new Image();
			image.source = "images/logo/user.swf";
			image.width = 6;
			image.height = 6;
			image.x = image.width+5;
			image.y = image.height+5;
			this.addChild(image);
		}
		
		override public function set Name(value: String): void{
			this._name = value;
			Lable.text=value;
		}
		
		override public function Draw(): void{	
			Lable.width=this.width;
			//Lable.height=Lable.height==0?20:Lable.height;
			Lable.maxHeight = this.height;
			Lable.x=0;
			if(Lable.height==0)Lable.y=(this.height-20)/2;
			else Lable.y=(this.height-Lable.height)/2;
			setImage();
			
			var gc:Graphics = this.graphics;
			gc.clear();
			this.SetLineColor();		
			//			this.Tooltip_text = "受理经办人:胡静<br>签收时间:2011-09-54 12:35:56<br>结束时间:2011-09-54 12:35:56<br>" +
			//				"执行状态：已经办理"
			
			//gc.beginFill(0x00ff00,0.2); 
			gc.beginFill(0xffffff,1); 
			gc.moveTo(x,y);
			gc.drawRoundRect(0,0,width,height,10,10);			
			gc.endFill();
			
			//if(myDrawBoard.simpleMode!=true)drawLineDec.decorate();
			
			this.myDrawBoard.ReDrawRelationElement(this);
		}
		
		public function drawAnglePoint():void{
			var points:Array = new Array();
			points.push(new Point(0,0));	
			points.push(new Point(this.width/2,0));	
			points.push(new Point(this.width,0));	
			points.push(new Point(0,this.height/2));	
			points.push(new Point(this.width,this.height/2));	
			points.push(new Point(0,this.height));	
			points.push(new Point(this.width/2,this.height));	
			points.push(new Point(this.width,this.height));	
			
			for(var i:int=0;i<points.length;i++){
				drawFork(points[i]);
			}			
		}
		
		public function drawFork(point:Point):void{
			var lineX:int = 2;
			var lineY:int = 2;			
			var gc:Graphics = this.graphics;
			//gc.clear();
			gc.lineStyle(1,0x0000ff);
			
			gc.moveTo(point.x-lineX,point.y-lineY);
			gc.lineTo(point.x+lineX,point.y+lineY);
			gc.moveTo(point.x-lineX,point.y+lineY);
			gc.lineTo(point.x+lineX,point.y-lineY);
		}
		
		override public function BuildXml(): XML{
			var xml:XML=new XML("<UserTask/>");
			xml.@id=this.ID;
			xml.@name=this.Name;
			BuildAttributeXML(xml);
			return xml;
		}	
		
		override public function BuildDIXMl(): XML{
			var xml:XML=new XML("<TaskShape/>");
			xml.@id="Shape_"+this.ID;
			xml.@name = "Shape_"+this.Name;
			addBaseDIAttribute(xml);
			xml.@activityRef=this.ID;
			return xml;
		}
		
		override public function ParseFromXml(iXML:XML): int{
			this.ID=iXML.@id;
			this.Name=iXML.@name;
			this.description = iXML.@description;
			//先把xml存储起来，点击节点时才发送请求，为了减少与数据库的交互
			//this.ixml = iXML;
			ParseAttributeFromXML(iXML);
			return 0;
		}	
		
		//设置表单名字
		//		public function formCallBack(forms:XMLList):void{
		//			var formName:String = forms.Form.@name;
		//			this.form.formName = formName;
		//		}
		//		public function orgCallBack(orgs:XML):void{
		//			var orginfo:String = "";
		//			var elements:XMLList = orgs.Org;
		//			for each (var elementXml:XML in elements){
		//				orginfo +=elementXml.@name+",";
		//			}
		//			if(orginfo.lastIndexOf(",")==orginfo.length-1)orginfo = orginfo.substr(0,orginfo.length-1);
		//			this.excuterTrategy.excuteOrgName = orginfo;
		//		}
		//		public function roleCallBack(roles:XMLList):void{
		//			var roleinfo:String = "";
		//			var elements:XMLList = roles.Role;
		//			for each (var elementXml:XML in elements){
		//				roleinfo +=elementXml.@name+ ",";
		//				}
		//			if(roleinfo.lastIndexOf(",")==roleinfo.length-1)roleinfo = roleinfo.substr(0,roleinfo.length-1);
		//			this.excuterTrategy.excuteRoleName = roleinfo;
		//		}
		//		public function userCallBack(users:XMLList):void{
		//			var userinfo:String = "";
		//			var elements:XMLList = users.User;
		//			for each (var elementXml:XML in elements){
		//				userinfo +=elementXml.@name+",";
		//			}
		//			if(userinfo.lastIndexOf(",")==userinfo.length-1)userinfo = userinfo.substr(0,userinfo.length-1);
		//			this.excuterTrategy.excuteUserName = userinfo;				
		//		}
		
		public function ParseAttributeFromXML(iXML:XML):int{
			var action:String = iXML.@actionType;
			if(action=="Normal")this.actionType = 0;
			else if(action=="Audit")this.actionType = 1;
			else if(action=="Cirulated")this.actionType = 2;
			else if(action=="Leader_Audit")this.actionType = 3;
			//form表单
			var pk_formdefinition:String = iXML.@pk_formdefinition;
			if(pk_formdefinition!=null&&pk_formdefinition!=""){
				this.form.pk_formdefinition = pk_formdefinition;
				//设置表单名称
				//new FormService(this,formCallBack).getFormsByPk(pk_formdefinition);
			}
			else {
				this.form.pk_formdefinition = null;
				this.form.formName = null;
			}
			
			
			//是否允许打印
			var allowPrintStr:String = iXML.@allowPrint;
			if(allowPrintStr=="true")this.allowPrint = "0";
			else if(allowPrintStr=="false")this.allowPrint = "1";
			//是否加签
			var isAddSignStr:String = iXML.@allowAddSign;
			if(isAddSignStr=="true")this.allowAddSign = "0";
			else if(isAddSignStr=="false")this.allowAddSign = "1";
			//是否转发
			var isTransmitStr:String = iXML.@allowTransmit;
			if(isTransmitStr=="true")this.allowTransmit = "0";
			else if(isTransmitStr=="false")this.allowTransmit = "1";
			//是否由上一步指派
			var allowPreSignStr:String = iXML.@allowPreAssign;
			if(allowPreSignStr=="true")this.allowPreAssign = "0";
			else if(allowPreSignStr=="false")this.allowPreAssign = "1";
			//是否传阅
			var allowDeliverStr:String = iXML.@allowDeliver;
			if(allowDeliverStr=="true")this.allowDeliver = "0";
			else if(allowDeliverStr=="false")this.allowDeliver = "1";
			//是否同部门限制
			var allowResDeptStr:String = iXML.@allowResDept;
			if(allowResDeptStr=="true")this.allowResDept = "0";
			else if(allowResDeptStr=="false")this.allowResDept = "1";
			//是否可以编辑意见
			var allowEditOpinionStr:String = iXML.@allowEditOpinion;
			if(allowEditOpinionStr=="true")this.allowEditOpinion = "0";
			else if(allowEditOpinionStr=="false")this.allowEditOpinion = "1";
			//是否允许催办
			var allowUrgeStr:String = iXML.@allowUrge;
			if(allowUrgeStr=="true")this.allowUrge = "0";
			else if(allowUrgeStr=="false")this.allowUrge = "1";
			
			//可发起子流程
			var startprodefsStr:String = iXML.@subProDefPks;
			if(startprodefsStr!=null&&startprodefsStr!=""){
				this.startProdefs.pk_prodef = startprodefsStr;
			}
			
			//扩展字段映射
			var attArrStr:String = iXML.@extArr;
			if(attArrStr!=null&&attArrStr!=""){
				var extArray:Array = attArrStr.split(",");
				for each(var str:String in extArray){
					var item:Object = {column1:str.split(":")[0],column2:str.split(":")[1]};
					this.extArr.addItem(item);
				}
			}
			
			var excuteUser:String = iXML.ActorStrategy.@users;
			var excuteRole:String = iXML.ActorStrategy.@roles;
			var excuteOrg:String = iXML.ActorStrategy.@orgs;
			var virtualRole:String = iXML.ActorStrategy.@virtualRole;
			var asignByOthers:String = iXML.ActorStrategy.@asignByOthers;
			var sameAsOthers:String = iXML.ActorStrategy.@sameAsOthers;
			var selfDefClass:String = iXML.ActorStrategy.@selfDefClass;
			
			if(excuteUser!=null&&excuteUser!="")this.excuterTrategy.excuteUser = excuteUser;
			if(excuteRole!=null&&excuteRole!="")this.excuterTrategy.excuteRole = excuteRole;
			if(excuteOrg!=null&&excuteOrg!="")this.excuterTrategy.excuteOrg = excuteOrg;
			if(virtualRole!=null&&virtualRole!="")this.excuterTrategy.virtualRole = virtualRole;
			if(asignByOthers!=null&&asignByOthers!="")this.excuterTrategy.asignByOthers = asignByOthers;
			if(sameAsOthers!=null&&sameAsOthers!="")this.excuterTrategy.sameAsOthers = sameAsOthers;
			if(selfDefClass!=null&&selfDefClass!="")this.excuterTrategy.selfDefClass = selfDefClass;
			
			//完成策略
			var completetype:String = iXML.CompleteStrategy.@type;
			this.completeTrategy.taskFinishedStrategy = completetype;
			if(completetype=="1"){
				var isnotbunch:String = iXML.CompleteStrategy.@isNotBunch;
				if(isnotbunch=="true")
					this.completeTrategy.countersign_type = "1";
				else this.completeTrategy.countersign_type = "0";
			}
			if(completetype=="2"){
				var completevalue:String = iXML.CompleteStrategy.@value;
				var completvalues:Array = completevalue.split(":");
				if(completvalues.length==2){
					if(completvalues[0]=="ByCount")
						this.completeTrategy.taskFinishCount = completvalues[1];
					else if(completvalues[0]=="ByPercent"){
						this.completeTrategy.taskFinishePercent = completvalues[1];
					}
				}
			}
			//回退策略
			var backable:String = iXML.RejectStrategy.@isNotReject;
			if(backable=="false")this.backTrategy.backable = "0";
			else this.backTrategy.backable = "1";
			var backvalue:String = iXML.RejectStrategy.@value;
			if(backvalue=="PreviousHumAct")this.backTrategy.backScope = "1";
			else if(backvalue=="AllHumAct")this.backTrategy.backScope = "2";
			else if(backvalue=="StartHumAct")this.backTrategy.backScope = "4";
			else if(backvalue.indexOf("AppointHumAct")>-1){
				this.backTrategy.backScope = "3";
				if(backvalue.split(":").length==2)
					this.backTrategy.backActivites = backvalue.split(":")[1];
			} 
			//超时策略
			var allowControl:String = iXML.MessageStrategy.@allowControl;
			if(allowControl=="true")this.messageStrategy.allowControl = true;
			else this.messageStrategy.allowControl = false;
			var unit:String = iXML.MessageStrategy.@unit;
			if(unit=="minute")this.messageStrategy.unit = "1";
			else if(unit=="hour")this.messageStrategy.unit = "2";
			else if(unit=="day")this.messageStrategy.unit = "3";
			else if(unit=="week")this.messageStrategy.unit = "4";	
			var aheadTime:String = iXML.MessageStrategy.@aheadTime;
			if(aheadTime!=null&&aheadTime!="")this.messageStrategy.aheadTime = aheadTime;
			var worktime:String = iXML.MessageStrategy.@workTime;
			if(worktime!=null&&worktime!="")this.messageStrategy.workTime = worktime;
			
			//			var remindtype:String = iXML.MessageStrategy.@remind;
			//			if(remindtype=="message")this.messageStrategy.remindType = "1";
			//			else if(remindtype=="email")this.messageStrategy.remindType = "2";
			//			var handlertype:String = iXML.messageStrategy.@handler;
			//			if(handlertype=="continue")this.messageStrategy.handlerType = "1";
			//			else if(handlertype=="interrupt")this.messageStrategy.handlerType = "2";
			
			//任务创建提醒
			var createdMsgStr:String = iXML.MessageStrategy.@createdMsg;
			if(createdMsgStr!=null&&createdMsgStr!="")this.messageStrategy.createdMsg = createdMsgStr.split(",");
			//任务完成提醒
			var completedMsgStr:String = iXML.MessageStrategy.@completedMsg;
			if(completedMsgStr!=null&&completedMsgStr!="")this.messageStrategy.completedMsg = completedMsgStr.split(",");
			//任务超时提醒
			var overtimeMsgStr:String = iXML.MessageStrategy.@overtimeMsg;
			if(overtimeMsgStr!=null&&overtimeMsgStr!="")this.messageStrategy.overtimeMsg = overtimeMsgStr.split(",");
			//任务超时动作
			var overtimeActStr:String = iXML.MessageStrategy.@overtimeAct;
			if(overtimeActStr!=null&&overtimeActStr!="")this.messageStrategy.overtimeAct = overtimeActStr.split(","); 
			
			//处理类
			this.delegatorClass = iXML.@delegatorClass;
			//this.afterClass = iXML.@afterClass;	
			
			return 0;
		}
		
		public function BuildAttributeXML(xml:XML):XML{
			if(getPk_formdefinition()!=null)xml.@pk_formdefinition = getPk_formdefinition();
			if(description!=null&&description!="")xml.@description = description;	
			
			if(delegatorClass!=null&&delegatorClass!=""){
				xml.@delegatorClass = delegatorClass;
			}
			//			if(afterClass!=null&&afterClass!=""){
			//				xml.@afterClass = afterClass;
			//			}
			//是否允许打印
			if(allowPrint=="0")xml.@allowPrint = "true";
			else if(allowPrint=="1")xml.@allowPrint = "false";
			//是否允许加签
			if(allowAddSign=="0")xml.@allowAddSign = "true";
			else if(allowAddSign=="1")xml.@allowAddSign = "false";
			//是否允许转发
			if(allowTransmit=="0")xml.@allowTransmit = "true";
			else if(allowTransmit=="1")xml.@allowTransmit = "false";
			//是否由上一步指派
			if(allowPreAssign=="0")xml.@allowPreAssign = "true";
			else if(allowPreAssign=="1")xml.@allowPreAssign = "false";
			//是否允许传阅
			if(allowDeliver=="0")xml.@allowDeliver = "true";
			else if(allowDeliver=="1")xml.@allowDeliver = "false";
			//是否同部门限定
			if(allowResDept=="0")xml.@allowResDept = "true";
			else if(allowResDept=="1")xml.@allowResDept = "false";
			//是否允许编辑意见
			if(allowEditOpinion=="0")xml.@allowEditOpinion = "true";
			else if(allowEditOpinion=="1")xml.@allowEditOpinion = "false";
			//是否允许催办
			if(allowUrge=="0")xml.@allowUrge = "true";
			else if(allowUrge=="1")xml.@allowUrge = "false";
			//活动类型
			if(actionType>-1){
				if(actionType==0)
					xml.@actionType = "Normal";
				else if(actionType==1)
					xml.@actionType = "Audit";
				else if(actionType==2)
					xml.@actionType = "Cirulated";
				else if(actionType==3)
					xml.@actionType = "Leader_Audit";
			}
			//可发起的子流程
			if(startProdefs.pk_prodef!=null&&startProdefs.pk_prodef!=""){
				xml.@subProDefPks = startProdefs.pk_prodef;
			}
			//扩展字段映射
			if(extArr.length>0){
				var extArrStr:String = "";
				for(var m:int=0;m<extArr.length;m++){
					var item:Object = extArr.getItemAt(m);
					extArrStr+=item["column1"]+":"+item["column2"];
					if(m!=extArr.length-1)extArrStr+=",";
				} 
				xml.@extArr = extArrStr;
			}
			
			//执行策略
			var excuterXml:XML = <ActorStrategy/>;
			
			//excuterXml.@type = excuterTrategy.excuter;
			
			if(excuterTrategy.excuteUser!=null)excuterXml.@users = excuterTrategy.excuteUser;
			if(excuterTrategy.excuteRole!=null)excuterXml.@roles = excuterTrategy.excuteRole;
			if(excuterTrategy.excuteOrg!=null)excuterXml.@orgs = excuterTrategy.excuteOrg;				
			if(excuterTrategy.virtualRole!=null)excuterXml.@virtualRole = excuterTrategy.virtualRole;	
			if(excuterTrategy.asignByOthers!=null)excuterXml.@asignByOthers = excuterTrategy.asignByOthers;
			if(excuterTrategy.sameAsOthers!=null)excuterXml.@sameAsOthers = excuterTrategy.sameAsOthers;
			if(excuterTrategy.selfDefClass!=null&&excuterTrategy.selfDefClass!="")
				excuterXml.@selfDefClass = excuterTrategy.selfDefClass;
			
			
			//任务分配策略
			var taskAssignStrategyXml:XML = <AllotStrategy/>;
			if(assignTrategy.taskAssignStrategy!=null){
				if(assignTrategy.taskAssignStrategy=="0")
					taskAssignStrategyXml.@value="All";
				if(assignTrategy.taskAssignStrategy=="1")
					taskAssignStrategyXml.@value="count";
				if(assignTrategy.taskAssignStrategy=="2")
					taskAssignStrategyXml.@value="Any";
			}
			
			//任务完成策略
			var taskFinishStrategyXml:XML =  <CompleteStrategy/>; 
			if(completeTrategy.taskFinishedStrategy!=null){
				taskFinishStrategyXml.@type = completeTrategy.taskFinishedStrategy;
				if(completeTrategy.taskFinishedStrategy=="0")
					taskFinishStrategyXml.@value = "Occupy";
				else if(completeTrategy.taskFinishedStrategy=="1"){
					taskFinishStrategyXml.@value = "Countersign";
					if(completeTrategy.countersign_type=="0"){
						taskFinishStrategyXml.@isNotBunch = "false";
					}
					else if(completeTrategy.countersign_type=="1"){
						taskFinishStrategyXml.@isNotBunch = "true";
					}
				}
				else if(completeTrategy.taskFinishedStrategy=="2"){
					if(completeTrategy.taskFinishCount!=null)taskFinishStrategyXml.@value = "ByCount:"+completeTrategy.taskFinishCount;
					if(completeTrategy.taskFinishePercent!=null&&completeTrategy.taskFinishePercent!="")taskFinishStrategyXml.@value = "ByPercent:"+completeTrategy.taskFinishePercent;
				}
			}
			
			//回退策略
			var backStrategyXml:XML = <RejectStrategy/>;
			if(backTrategy.backable=="0")
				backStrategyXml.@isNotReject="false";
			if(backTrategy.backable=="1"){
				backStrategyXml.@isNotReject="true";
				if(backTrategy.backScope=="1")
					backStrategyXml.@value="PreviousHumAct";
				if(backTrategy.backScope=="2")
					backStrategyXml.@value="AllHumAct";
				if(backTrategy.backScope=="3")
					backStrategyXml.@value="AppointHumAct:"+backTrategy.backActivites;
				if(backTrategy.backScope=="4")
					backStrategyXml.@value="StartHumAct";
			}
			//超时处理
			var messageStrategyXml:XML = <MessageStrategy/>;
			//任务创建提醒方式
			if(messageStrategy.createdMsg!=null&&messageStrategy.createdMsg.length>0)messageStrategyXml.@createdMsg = messageStrategy.createdMsg.join(",");
			//任务完成提醒方式
			if(messageStrategy.completedMsg!=null&&messageStrategy.completedMsg.length>0)messageStrategyXml.@completedMsg = messageStrategy.completedMsg.join(",");
			//任务超时提醒
			if(messageStrategy.overtimeMsg!=null&&messageStrategy.overtimeMsg.length>0)messageStrategyXml.@overtimeMsg = messageStrategy.overtimeMsg.join(",");			
			//任务超时动作
			if(messageStrategy.overtimeAct!=null&&messageStrategy.overtimeAct.length>0)messageStrategyXml.@overtimeAct = messageStrategy.overtimeAct.join(",");
			
			
			messageStrategyXml.@allowControl = messageStrategy.allowControl;
			if(messageStrategy.allowControl==true){
				//				if(messageStrategy.remindType!=null){
				//					if(messageStrategy.remindType=="1")
				//						messageStrategyXml.@remind="message";
				//					if(messageStrategy.remindType=="2")
				//						messageStrategyXml.@remind="email";
				//				}
				//				if(messageStrategy.handlerType!=null){
				//					if(messageStrategy.handlerType=="1")
				//						messageStrategyXml.@handler="continue";
				//					if(messageStrategy.handlerType=="2")
				//						messageStrategyXml.@handler="interrupt";
				//				}
				if(messageStrategy.unit!=null){
					if(messageStrategy.unit == "1")messageStrategyXml.@unit = "minute";
					if(messageStrategy.unit == "2")messageStrategyXml.@unit = "hour";
					if(messageStrategy.unit == "3")messageStrategyXml.@unit = "day";
					if(messageStrategy.unit == "4")messageStrategyXml.@unit = "week";
					if(messageStrategy.unit == "5")messageStrategyXml.@unit = "month";
				}
				if(messageStrategy.aheadTime!=null){
					messageStrategyXml.@aheadTime = messageStrategy.aheadTime;
				}
				if(messageStrategy.workTime!=null){
					messageStrategyXml.@workTime = messageStrategy.workTime;
				}
			}
			
			xml.appendChild(excuterXml);
			//xml.appendChild(taskAssignStrategyXml);
			xml.appendChild(taskFinishStrategyXml);
			xml.appendChild(backStrategyXml);
			xml.appendChild(messageStrategyXml);
			return xml;
		}		
		public function getPk_formdefinition():String{
			if(form.pk_formdefinition!=null)return form.pk_formdefinition;
			else return WfContext.Pk_formdefinition;
		}
		//活动提示信息
		//		override public function set Tooltip_text(value:String):void{
		//			ToolTipManager.toolTipClass = HTMLToolTip;
		//			this._tooltip_text = value;
		//			this.toolTip = _tooltip_text;
		//		}
	}
}