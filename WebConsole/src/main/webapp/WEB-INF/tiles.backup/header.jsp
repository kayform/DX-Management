<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<head>
	<title>D-Specto WebConsole</title>
	
	<!-- Meta -->
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	
	<LINK REL="SHORTCUT ICON" href="/css/D-Specto.ico">
	
	<!-- Bootstrap -->
	<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" />
	
	<!-- Bootstrap Extended -->
	<link href="bootstrap/extend/jasny-bootstrap/css/jasny-bootstrap.min.css" rel="stylesheet">
	<link href="bootstrap/extend/jasny-bootstrap/css/jasny-bootstrap-responsive.min.css" rel="stylesheet">
	<!-- 
	<link href="bootstrap/extend/bootstrap-wysihtml5/css/bootstrap-wysihtml5-0.0.2.css" rel="stylesheet">
	 -->
	<!-- JQueryUI v1.9.2 -->
	<link rel="stylesheet" href="theme/scripts/jquery-ui-1.9.2.custom/css/smoothness/jquery-ui-1.9.2.custom.min.css" />
	
	<!-- Glyphicons -->
	<link rel="stylesheet" href="theme/css/glyphicons.css" />
	
	<!-- Bootstrap Extended -->
	<link rel="stylesheet" href="bootstrap/extend/bootstrap-select/bootstrap-select.css" />
	<link rel="stylesheet" href="bootstrap/extend/bootstrap-toggle-buttons/static/stylesheets/bootstrap-toggle-buttons.css" />
	
	<!-- Uniform -->
	<link rel="stylesheet" media="screen" href="theme/scripts/pixelmatrix-uniform/css/uniform.default.css" />

	<!-- JQuery v1.8.2 -->
	<script src="theme/scripts/jquery-1.8.2.min.js"></script>
	
	<!-- Modernizr -->
	<script src="theme/scripts/modernizr.custom.76094.js"></script>
	
	<!-- MiniColors -->
	<link rel="stylesheet" media="screen" href="theme/scripts/jquery-miniColors/jquery.miniColors.css" />
	
	<!-- google-code-prettify -->
	<link href="theme/scripts/google-code-prettify/prettify.css" type="text/css" rel="stylesheet" />
	
	<!-- Theme -->
	<link rel="stylesheet" href="theme/css/style.min.css?1361543640" />
	
	<!-- Loading anmation css -->
	<link rel="stylesheet" href="css/loading.css" />
	
	<!-- Google Analytics -->
	<script type="text/javascript">

	  //var _gaq = _gaq || [];
	 // _gaq.push(['_setAccount', 'UA-36057737-1']);
	  //_gaq.push(['_trackPageview']);
	
	  /*
	  (function() {
	    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
	    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
	    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
	  })();//*/
	
	</script>
	
	<!-- LESS 2 CSS -->
	<script src="theme/scripts/less-1.3.3.min.js"></script>
	<style type="text/css">
		/* 전체 화면 사용 */
		.container {width : 90%; min-width: 1200px;}/**/
		
		/* datepicker 요일 텍스트 스타일 */
		.ui-datepicker-calendar th {color:#797979;}
		
		/* 웹사이트 최소 높이*/
		#content { min-height:700px;}
		
		/* 텍스트 박스 글자색*/
		input[type=text],input[type=password],select,textarea{color:#484c50;}
		.filter-bar form input, .filter-bar form select {color:#484c50;}
		input[type=number]{height:22px;}
		
		.color-10{background:#54709f;}
		.color-5{background:#fe9c1d;}
		.color-6{background:#bd2220;}
		
		.btn-action{border-radius: 6px;}
		
		/* 검색조건 css */
		select,input[type="file"]{*margin-top:4px;font-size:13px;height:28px;}
		.filter-bar{padding-bottom: 5px;}
		.filter-bar div div.input-append .add-on{padding:4px 6px;border-radius:0 4px 4px 0;}.filter-bar div div.input-append .add-on i:before{top:7px;left:7px;font-size:13px;}
		.filter-bar div div.input-append input[type=text]{margin-top: 0px;}
		.filter-bar div div.input-append select{margin-top: 0px;margin-bottom: 0px; font-size: 13px;}
		.filter-bar div div.input-append span.calendar{margin-top: 0px;}
		.filter-bar form input,.filter-bar form select{padding:4px 6px;}
		.filter-bar form select{padding:4px 6px;height:28px;  margin-top: 3px;}
		.filter-bar {padding-top: 5px; padding-bottom: 5px;}
		.filter-bar form label {line-height: 28px;}
		.filter-bar div {line-height: 28px;}
		
		/* 스크롤바 css */
		/* .color-3 rgb(2, 158, 198); */
		.color-3-scrollbar::-webkit-scrollbar {width: 12px;}
		.color-3-scrollbar::-webkit-scrollbar-track {background-color: rgba(0, 0, 0, 0.1);}
		.color-3-scrollbar::-webkit-scrollbar-thumb {background-color: rgba(2, 158, 198, 0.8);border: 1px solid rgba(2, 158, 198, 0.5);}
		/* .color-10 rgb(2, 158, 198); */
		.color-10-scrollbar::-webkit-scrollbar {width: 12px;}
		.color-10-scrollbar::-webkit-scrollbar-track {background-color: rgba(0, 0, 0, 0.1);}
		.color-10-scrollbar::-webkit-scrollbar-thumb {background-color: rgba(84, 112, 159, 0.8);border: 1px solid rgba(84, 112, 159, 0.5);}
		/* .color-5 rgb(254, 156, 29); */
		.color-5-scrollbar::-webkit-scrollbar {width: 12px;}
		.color-5-scrollbar::-webkit-scrollbar-track {background-color: rgba(0, 0, 0, 0.1);}
		.color-5-scrollbar::-webkit-scrollbar-thumb {background-color: rgba(254, 156, 29, 0.8);border: 1px solid rgba(254, 156, 29, 0.5);}
		/* .color-6 rgb(189, 34, 32); */
		.color-6-scrollbar::-webkit-scrollbar {width: 12px;}
		.color-6-scrollbar::-webkit-scrollbar-track {background-color: rgba(0, 0, 0, 0.3);}
		.color-6-scrollbar::-webkit-scrollbar-thumb {background-color: rgba(189, 34, 32, 0.7);border: 1px solid rgba(189, 34, 32, 0.5);}
		
		/* 오류메시지 */
		.error {color: red;}
	</style>
	
	<!-- zephyros.js  -->
	<script src="js/zephyros.js" type="text/javascript"></script>
	
</head>