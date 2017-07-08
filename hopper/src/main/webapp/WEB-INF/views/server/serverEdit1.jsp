<%@ include file="/WEB-INF/views/meta.jsp" %>


<article class="cl pd-20">
	<form action="/server/add.json" method="post" class="form form-horizontal" id="form-member-add">
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>端口标识：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" value="${server.port}" placeholder="" id="port" name="port">
				<input type="hidden"  class="input-text" value="${server.id}" placeholder="" id="sid" name="sid">
			</div>
		</div>
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>服务名称：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" value="${server.name}" placeholder="" id="name" name="name">
			</div>
			
			
		</div>
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">WEB容器选择：</label>
			<div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
				<select name="container" class="select">
					<option value="2">apache-tomcat-6.0.45</option>
					<option value="0">apache-tomcat-7.0.73</option>
					<option value="1">apache-tomcat-8.0.38</option>
				</select>
				</span> </div>
		</div>
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>服务器路径：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<a style="text-decoration:none" onClick="add()" href="javascript:;" title="启用"><i class="Hui-iconfont">&#xe600;</i></a>
				
			</div>
			
			<div class=" col-xs-8 col-sm-9" id="pathinput">
				<div class="row cl">
					<div class="col-sm-5">
						<input id="path1" type="text" class="input-text"  placeholder="相对路径" />
					</div>
					<div class="col-sm-5">
						<input id="path2" type="text" class="input-text"  placeholder="绝对路径" />
					</div>
					<div class="col-sm-1">
						<a style="text-decoration:none" onClick="save()" href="javascript:;" title="保存"><i class="Hui-iconfont">&#xe632;</i></a>
					</div>
					
					<div class="col-sm-1">
						<a style="text-decoration:none" onClick="cancel()" href="javascript:;" title="取消"><i class="Hui-iconfont">&#xe6a6;</i></a>
					</div>
				</div>
			</div>
			
			<div id="pathcash" class="col-xs-offset-r col-sm-offset-3 col-xs-8 col-sm-9 ">
				<div class="row cl">
					<div class="text-l col-sm-4"><span>相对路径:</span><span class="pipe">|</span><span>sadfasdf</span></div>
					<div class="text-l col-sm-6"><span>绝对路径:</span><span class="pipe">|</span><span>ssfsdfsdf</span></div>
					<div class="text-l col-sm-2 text-r"><a style="text-decoration:none" onClick="add()" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6a6;</i></a></div>
				</div>
				
				<div class="row cl">
					<div class="text-l col-sm-4"><span>相对路径:</span><span class="pipe">|</span><span>sadfasdf</span></div>
					<div class="text-l col-sm-6"><span>绝对路径:</span><span class="pipe">|</span><span>ssfsdfsdf</span></div>
					<div class="text-l col-sm-2 text-r"><a style="text-decoration:none" onClick="add()" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6a6;</i></a></div>
				</div>
				
			</div>
			
		</div>
		
		
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">JDK选择：</label>
			<div class="formControls col-xs-8 col-sm-9"> <span class="select-box">
				<select name="jdk" class="select">
					<option value="0">jdk1.5.0_22</option>
					<option value="1">jdk1.6.0_45</option>
					<option value="2">jdk1.7.0_71</option>
					<option value="3">jdk1.8.0_73</option>
				</select>
				</span> </div>
		</div>
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">JAVA_OPTS参数：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" placeholder="" name="args" id="args" value="${server.args}" />
			</div>
		</div>
		
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">备注：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<textarea name="beizhu" cols="" rows="" class="textarea"  placeholder="简要描述" ></textarea>
				<p class="textarea-numberbar"><em class="textarea-length">0</em>/100</p>
			</div>
		</div>
		<div class="row cl">
			<div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
				<input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
			</div>
		</div>
	</form>
</article>

<script id="template" type="x-tmpl-mustache">
	<div class="row cl" id="loadpath-{{count}}">
		<div class="text-l col-sm-4"><span>相对路径:</span><span class="pipe">|</span><span>{{context}}</span></div>
		<div class="text-l col-sm-6"><span>绝对路径:</span><span class="pipe">|</span><span>{{local}}</span></div>
		<div class="text-l col-sm-2 text-r"><a style="text-decoration:none" onClick="add()" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6a6;</i></a></div>
	</div>
</script>

<%@ include file="/WEB-INF/views/footer.jsp" %>
<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="/lib/My97DatePicker/4.8/WdatePicker.js"></script>
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/jquery.validate.js"></script> 
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/validate-methods.js"></script> 
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/messages_zh.js"></script> 
<script type="text/javascript" src="/lib/jquery.tabledit.1.2.3/jquery.tabledit.js"></script> 
<script type="text/javascript">

var count=0;
function add(){
	console.log("增加");
	
	$('#pathinput').show();
}


function save() {
	var path1=$('#path1').value;
	var path2=$('#path1').value;
	var localpath = {
		context:path1,
		local:path2
	};
	var tbl_body =Mustache.to_html(template, localpath);
	$('#pathcash').append(tbl_body);
}

$(function(){
	
	$('#pathinput').hide();
	
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	
	function showResponse(responseText,statusText) {
		    console.log(responseText);
		}
	
	 $("#form-member-add").validate({
		rules:{
			servername:{
				required:true,
				minlength:3,
				maxlength:50
			},
			platform:{
				required:true
			},
			args:{
				required:true
			},
			localpath:{
				required:true,
				minlength:3,
				maxlength:200
			}
			
		},
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		//submitHandler:submit
	});
	
	 var options = {
				success:function(data){
							var index = parent.layer.getFrameIndex(window.name);
							parent.layer.msg('添加成功!',{icon:1,time:1000});
							parent.layer.close(index);
						}
	 }
	 $("#form-member-add").ajaxForm(options);
});
</script> 
<!--/请在上方写此页面业务相关的脚本-->

</body>
</html>