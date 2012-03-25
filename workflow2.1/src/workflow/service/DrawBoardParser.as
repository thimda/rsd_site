package workflow.service
{
	import service.FormService;
	import service.OrgService;
	import service.RoleService;
	import service.UserService;
	import service.ProdefService;
	
	import workflow.DrawBoard;
	import workflow.UseractivitiesNode;
	import workflow.utils.HashMap;
	
	public class DrawBoardParser
	{
		private var drawBoard:DrawBoard;
		public function DrawBoardParser(drawBoard:DrawBoard)
		{
			this.drawBoard = drawBoard;
		}
		//解析表单，执行组织，执行角色，执行人,可发起子流程的中文显示
		public  function parseCN():void{
			var usernodes:Array = drawBoard.getAllUserActivities(true);
			
			var formarr:Array = new Array();
			var orgarr:Array = new Array();
			var rolearr:Array = new Array();
			var userarr:Array = new Array();
			var prodefarr:Array = new Array();
			
			for(var i:int=0;i<usernodes.length;i++){
				var node:UseractivitiesNode = usernodes[i];
				var pk_forms:String = node.form.pk_formdefinition;
				var pk_orgs:String = node.excuterTrategy.excuteOrg;
				var pk_roles:String = node.excuterTrategy.excuteRole;
				var pk_users:String = node.excuterTrategy.excuteUser;
				var pk_prodefs:String = node.startProdefs.pk_prodef;
								
				if(pk_forms!=null&&pk_forms!="")formarr.push(pk_forms.split(","));
				if(pk_orgs!=null&&pk_orgs!="")orgarr.push(pk_orgs.split(","));
				if(pk_roles!=null&&pk_roles!="")rolearr.push(pk_roles.split(","));
				if(pk_users!=null&&pk_users!="")userarr.push(pk_users.split(","));
				if(pk_prodefs!=null&&pk_prodefs!="")prodefarr.push(pk_prodefs.split(","));
			}
			if(formarr.length>0)
				new FormService(this,formCallBack).getFormsByPk(formarr.join(","));
			if(orgarr.length>0)
				new OrgService(this,orgCallBack).getOrgsByPks(orgarr.join(","));
			if(rolearr.length>0)
				new RoleService(this,roleCallBack).getRolesByPks(rolearr.join(","));
			if(userarr.length>0)
				new UserService(this,userCallBack).getUsersByPks(userarr.join(","));
			if(prodefarr.length>0)
				new ProdefService(this,prodefCallBack).getProdefsByPks(prodefarr.join(","));
		}
		
		/**
		 * 回调设置表单名字
		 * */
		public function formCallBack(forms:XMLList):void{
			var elements:XMLList = forms.Form;
			var map:HashMap = new HashMap();
			for each (var form:XML in elements) {
				var formName:String = form.@name;
				var pk_form:String = form.@pk_formdefinition;
				map.put(pk_form,formName);
			}
			var usernodes:Array = drawBoard.getAllUserActivities(true);
			for(var i:int=0;i<usernodes.length;i++){
				var node:UseractivitiesNode = usernodes[i];
				var pk_formdefinition:String = node.form.pk_formdefinition;
				if(pk_formdefinition!=null&&pk_formdefinition!="")
					node.form.formName = map.get(pk_formdefinition);
			}
			
		}
		/**
		 * 回调设置组织名字
		 * */
		public function orgCallBack(orgs:XML):void{
			var elements:XMLList = orgs.Org;
			var map:HashMap = new HashMap();
			for each (var org:XML in elements) {
				var name:String = org.@name;
				var pk_org:String = org.@pk_org;
				map.put(pk_org,name);
			}
			var usernodes:Array = drawBoard.getAllUserActivities(true);
			for(var i:int=0;i<usernodes.length;i++){
				var node:UseractivitiesNode = usernodes[i];
				var excuteOrg:String = node.excuterTrategy.excuteOrg;
				var pk_orgs:Array =null;
				if(excuteOrg!=null)pk_orgs= excuteOrg.split(",");
				var orginfo:String = "";
				if(pk_orgs!=null&&pk_orgs.length>0&&pk_orgs[0]!=""){					
					for (var k:int=0;k<pk_orgs.length;k++){
						orginfo +=map.get(pk_orgs[k]);
						if(k!=pk_orgs.length-1)orginfo+=",";
					}
				}
				node.excuterTrategy.excuteOrgName = orginfo;
			}
		}
		/**
		 * 回调设置角色名字
		 * */
		public function roleCallBack(roles:XMLList):void{
			var elements:XMLList = roles.Role;
			var map:HashMap = new HashMap();
			for each (var role:XML in elements) {
				var name:String = role.@name;
				var pk_role:String = role.@pk_role;
				map.put(pk_role,name);
			}
			var usernodes:Array = drawBoard.getAllUserActivities(true);
			for(var i:int=0;i<usernodes.length;i++){
				var node:UseractivitiesNode = usernodes[i];
				var excuteRole:String = node.excuterTrategy.excuteRole;
				var pk_roles:Array =null;
				if(excuteRole!=null)pk_roles = excuteRole.split(",");
				var roleinfo:String = "";
				if(pk_roles!=null&&pk_roles.length>0&&pk_roles[0]!=""){					
					for (var k:int=0;k<pk_roles.length;k++){
						roleinfo +=map.get(pk_roles[k]);
						if(k!=pk_roles.length-1)roleinfo+=",";
					}
				}
				node.excuterTrategy.excuteRoleName = roleinfo;
			}
		}
		/**
		 * 回调设置用户名字
		 * */
		public function userCallBack(users:XMLList):void{
			var elements:XMLList = users.User;
			var map:HashMap = new HashMap();
			for each (var user:XML in elements) {
				var name:String = user.@name;
				var pk_user:String = user.@pk_user;
				map.put(pk_user,name);
			}
			var usernodes:Array = drawBoard.getAllUserActivities(true);
			for(var i:int=0;i<usernodes.length;i++){
				var node:UseractivitiesNode = usernodes[i];
				var excuteUser:String = node.excuterTrategy.excuteUser;
				var pk_users:Array =null;
				if(excuteUser!=null)pk_users= excuteUser.split(",");
				var userinfo:String = "";
				if(pk_users!=null&&pk_users.length>0&&pk_users[0]!=""){					
					for (var k:int=0;k<pk_users.length;k++){
						userinfo +=map.get(pk_users[k]);
						if(k!=pk_users.length-1)userinfo+=",";
					}
				}
				node.excuterTrategy.excuteUserName = userinfo;
			}		
		}
		/**
		 * 回调设置可发起子流程名字
		 * */
		public function prodefCallBack(prodefs:XMLList):void{
			var elements:XMLList = prodefs.Prodef;
			var map:HashMap = new HashMap();
			for each (var prodef:XML in elements) {
				var name:String = prodef.@name;
				var pk_prodef:String = prodef.@pk_prodef;
				map.put(pk_prodef,name);
			}
			var usernodes:Array = drawBoard.getAllUserActivities(true);
			for(var i:int=0;i<usernodes.length;i++){
				var node:UseractivitiesNode = usernodes[i];
				var subprodef:String = node.startProdefs.pk_prodef;
				var pk_prodefs:Array =null;
				if(subprodef!=null)pk_prodefs= subprodef.split(",");
				var names:String = "";
				if(pk_prodefs!=null&&pk_prodefs.length>0&&pk_prodefs[0]!=""){					
					for (var k:int=0;k<pk_prodefs.length;k++){
						names +=map.get(pk_prodefs[k]);
						if(k!=pk_prodefs.length-1)names+=",";
					}
				}
				node.startProdefs.prodefName = names;
			}		
		}
	}
}