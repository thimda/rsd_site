<style>
	.extend{}
	.extend li{
		border-bottom:1px dotted #333333;
		height: 29px;
    	line-height: 30px;
    	width: 100%;
    	background: url("${RES_PATH}/images/to_do_indicator.png") no-repeat scroll 0 11px transparent;
    	
	}
	.extend li a{
		margin-left: 8px;
		display:inline-block;
		float:left;
		color: #4D4D4D;
    	font-size: 12px;
    	height: 15px;
		
	}
	.extend li span{
		float:right;
	}
</style>
<div>
	<ul class="extend">
		<#if TASK_LIST??>
			<#list TASK_LIST as ta>
				<li <#if ta_index%2 != 0> class="fuscous_background" </#if> ><a href="${env().web}/pt/task/process?id=${ta.pk_task}"><#if ta.flowtypename??>[${ta.flowtypename}]</#if>${ta.humactname}</a> <span>${ta.startdate}</span></li>
			</#list>
		</#if>
	</ul>
</div>