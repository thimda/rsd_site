<style>

.a100{ width:100px; display:inline-block; font-size:12px}
.a40{ width:60px; display:inline-block; font-size:12px}

</style>
<b>当前集成系统:</b>
<br/>

<#if providers??>
<ul>
    <li><a class="a100">名称</a><a class="a40">状态</a><a class="a40">操作</a></li>

    <#list providers  as provider>
    
        <li><a class="a100">${provider.systemName}  
            <#if slots[provider.systemCode]??> 
                <#assign slot = slots[provider.systemCode]>
                <#if slot.active != 0>
                	<a class="a40">已关联</a> <a class="a40" href="/portal/pt/integr/setting/renew?id=${slot.pk_slot}" target="ajaxFrame">取消关联</a>
                 <#else>
                	  <a class="a40">已忽略  <a class="a40" href="/portal/pt/integr/setting/renew?id=${slot.pk_slot}" target="ajaxFrame">重新关联</a>
                </#if>
            <#else>
                 <a class="a40">未关联 </a><a class="a40">---</a>
            </#if> 
        </li>
    </#list>
</ul>
<iframe id="ajaxFrame" name="ajaxFrame" frameborder="0" width="0" height="0" marginheight="0" marginwidth="0" scrolling="no"></iframe>
</#if>