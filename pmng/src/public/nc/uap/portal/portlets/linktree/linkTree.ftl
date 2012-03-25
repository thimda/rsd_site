<#if renderType??>
	<#if renderType == "head">
		<style>
		.miniTree
		{
			
			margin:0;
			padding:0;
			/*
			overflow:auto;
			width: 250px;
			height:350px;
			overflow:auto;
			border: 1px solid #444444;
			*/
		}
		.miniTree li
		{
			list-style: none;
			margin:0;
			padding:0 0 0 34px;
			line-height: 14px;
		}
		.miniTree li span
		{
			display:inline;
			clear: left;
			white-space: nowrap;
		}
		.miniTree ul
		{
			margin:0; 
			padding:0;
		}
		.miniTree .root
		{
			margin-left:16px;
			
		}
		.miniTree .line
		{
			margin:0 0 0 -16px;
			padding:0;
			line-height: 3px;
			height:3px;
			font-size:3px;
			background: url(/portal/images/line_bg.gif) 0 0 no-repeat transparent;
		}
		.miniTree .line-last
		{
			margin:0 0 0 -16px;
			padding:0;
			line-height: 3px;
			height:3px;
			font-size:3px;
			background: url(/portal/images/spacer.gif) 0 0 no-repeat transparent;
		}
		.miniTree .line-over
		{
			margin:0 0 0 -16px;
			padding:0;
			line-height: 3px;
			height:3px;
			font-size:3px;
			background: url(/portal/images/line_bg_over.gif) 0 0 no-repeat transparent;
		}
		.miniTree .line-over-last
		{
			margin:0 0 0 -16px;
			padding:0;
			line-height: 3px;
			height:3px;
			font-size:3px;
			background: url(/portal/images/line_bg_over_last.gif) 0 0 no-repeat transparent;
		}
		.miniTree .folder-open
		{
			margin-left:-16px;
			background: url(/portal/images/collapsable.gif) 0 -2px no-repeat #fff;
		}
		.miniTree .folder-open-last
		{
			margin-left:-16px;
			background: url(/portal/images/collapsable-last.gif) 0 -2px no-repeat #fff;
		}
		.miniTree .folder-close
		{
			margin-left:-16px;
			background: url(/portal/images/expandable.gif) 0 -2px no-repeat #fff;
		}
		.miniTree .folder-close-last
		{
			margin-left:-16px;
			background: url(/portal/images/expandable-last.gif) 0 -2px no-repeat #fff;
		}
		.miniTree .doc
		{
			margin-left:-16px;
			background: url(/portal/images/leaf.gif) 0 -1px no-repeat #fff;
		}
		.miniTree .doc-last
		{
			margin-left:-16px;
			background: url(/portal/images/leaf-last.gif) 0 -1px no-repeat #fff;
		}
		.miniTree .ajax
		{
			background: url(/portal/images/spinner.gif) no-repeat 0 0 #ffffff;
			height: 16px;
			display:none;
		}
		.miniTree .ajax li
		{
			display:none;
			margin:0; 
			padding:0;
		}
		.miniTree .trigger
		{
			display:inline;
			margin-left:-32px;
			width: 28px;
			height: 11px;
			cursor:pointer;
		}
		.miniTree .text
		{
			cursor: default;
		}
		.miniTree .active
		{
			cursor: default;
		}
		#drag_container
		{
			background:#ffffff;
			color:#000;
			font: normal 11px arial, tahoma, helvetica, sans-serif;
			border: 1px dashed #767676;
		}
		#drag_container ul
		{
			list-style: none;
			padding:0;
			margin:0;
		}
		
		#drag_container li
		{
			list-style: none;
			background-color:#ffffff;
			line-height:18px;
			white-space: nowrap;
			padding:1px 1px 0px 16px;
			margin:0;
		}
		#drag_container li a
		{
			padding:0;
		}
		
		#drag_container li.doc, #drag_container li.doc-last
		{
			background: url(images/leaf.gif) no-repeat -17px 0 #ffffff;
		}
		#drag_container .folder-close, #drag_container .folder-close-last
		{
			background: url(images/expandable.gif) no-repeat -17px 0 #ffffff;
		}
		
		#drag_container .folder-open, #drag_container .folder-open-last
		{
			background: url(images/collapsable.gif) no-repeat -17px 0 #ffffff;
		}
		.contextMenu
		{
			display:none;
		}
		.blindwindow{overflow:auto;}
		.blindwindow dt { background:url(/portal/images/funbg.png) left top repeat-x; height:27px; font:bold 12px/27px "SimSun"; color:#034D71; padding-left:20px;}
		.blindwindow dt span {display:block; height:27px; background:url(/portal/images/1.gif) left center no-repeat; padding-left:20px;}
		.blindwindow dt.active span{background:url(/portal/images/2.gif) left center no-repeat;}
		.blindwindow dd { display:none;}
		 
		</style>
		<script type="text/javascript" src="/portal/js/jquery.mini.tree.js"></script>
		<script type="text/javascript">
		var miniTreeCollection;
		$(document).ready(function(){
			if(!miniTreeCollection){
				miniTreeCollection = $('.miniTree').miniTree({
					autoclose: true,
					animate:true
				});
			}
			
			 $(".blindwindow dt").each(function(i){
				$(this).click(function(){
					$(".blindwindow dt").eq(i).toggleClass("active").next("dd").toggle();		
				})
			})
		});
		 
		$(function(){
			$(window).resize(function(){
				 resizeIframe('${windowId}_dl',0);
			});
		})
		document.getCurrentOutLookContainer = function(){
			return getContainer("#${windowId}");
		}
		function resizeOutLookContainer(){
			initIframeArea('${windowId}_dl',0);
		}
		</script>
		<dl tp="blindwindow" class="blindwindow" id="${windowId}_dl">
		
	</#if>

	<#if renderType == "tree">
	<dt><span>${rootName}</span></dt>
	<dd> 
		<ul class="miniTree">
			<div class="root"> 
				<ul>
					<@rendTree nodes = ROOT_NODE></@rendTree>
				</ul>
		 	</div>
		</ul>
	</dd>
	</#if>

	<#if renderType == "node">
		<@rendTree nodes = ROOT_NODE></@rendTree>
	</#if>

	<#if renderType == "foot">
		</dl>
		<script>
			resizeOutLookContainer();
		</script>
	</#if>
</#if>
<#macro rendTree nodes>
	<#if nodes??>
		<#list nodes as node>
			<#if node.pk_funnode??>
				<#if node.funnode??>
					<li id='${node.pk_menuitem}'><a style="" onClick="document.getCurrentOutLookContainer().doAction('${node.funnode.url}');return false;">${node.title}</a></li>
				</#if>
			<#else>
				<li id='${node.pk_menuitem}'><a>${node.title}</a>
					<ul class="ajax">
						<li id='4'>{url:/portal/pt/link/tree/category?id=${node.pk_menuitem}&tp=outlook}</li>
					</ul>
				</li>
				
			</#if>
		</#list>
	 </#if>
</#macro>
