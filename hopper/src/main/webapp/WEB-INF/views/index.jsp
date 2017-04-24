<%@ include file="/WEB-INF/views/meta.jsp" %>

<title>H-ui.admin v3.0</title>
<meta name="keywords" content="H-ui.admin v3.0,H-ui网站后台模版,后台模版下载,后台管理系统模版,HTML后台模版下载">
<meta name="description" content="H-ui.admin v3.0，是一款由国人开发的轻量级扁平化网站后台模板，完全免费开源的网站后台管理系统模版，适合中小型CMS后台系统。">
</head>
<body>

<%@ include file="/WEB-INF/views/header.jsp" %>

<%@ include file="/WEB-INF/views/menu.jsp" %>

<section class="Hui-article-box">
	<nav class="breadcrumb"><i class="Hui-iconfont"></i> <a href="/" class="maincolor">首页</a> <span class="c-999 en">&gt;</span><span class="c-666">系统设置</span></nav>
	<div class="Hui-article">
		<article class="cl pd-20">
		      <div id="tab_demo" class="HuiTab">
		        <div class="tabBar cl"> <span>WEB容器配置</span> <span>JDK配置</span> <span>其他配置</span> </div>
		        <div class="tabCon">内容一</div>
		        <div class="tabCon">内容二</div>
		        <div class="tabCon">内容三</div>
		      </div>
		</article>
	</div>
</section>

<%@ include file="/WEB-INF/views/footer.jsp" %>
<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript">
$.Huitab("#tab_demo .tabBar span","#tab_demo .tabCon","current","click","1");
</script>
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>