<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="description" content="Metro, a sleek, intuitive, and powerful framework for faster and easier web development for Windows Metro Style.">
    <meta name="keywords" content="HTML, CSS, JS, JavaScript, framework, metro, front-end, frontend, web development">
    <meta name="author" content="Sergey Pimenov and Metro UI CSS contributors">

    <link rel='shortcut icon' type='image/x-icon' href='img/favicon.ico' />

    <title>Login form :: Metro UI CSS - The front-end framework for developing projects on the web in Windows Metro Style</title>

    <link href="theme/css/metro.css" rel="stylesheet">
    <link href="theme/css/metro-icons.css" rel="stylesheet">
    <link href="theme/css/metro-responsive.css" rel="stylesheet">
    <link href="theme/css/metro-schemes.css" rel="stylesheet">
    <link href="theme/css/docs.css" rel="stylesheet">
 	<link href="css/dialog.css" rel="stylesheet">

    <link href="theme/css/jquery.dataTables.min.css" rel="stylesheet">
    <link href="theme/css/select.dataTables.min.css" rel="stylesheet">
	
	<!-- JQuery UI -->
	<link href="theme/script/jquery-ui/jquery-ui.css" rel="stylesheet">
	<link href="theme/script/jquery-ui/jquery-ui.structure.css" rel="stylesheet">
	<link href="theme/script/jquery-ui/jquery-ui.theme.css" rel="stylesheet">

	
	<!-- JQuery v2.1.3 -->
    <script src="theme/js/jquery-2.1.3.min.js"></script>
    <script src="theme/js/jquery.dataTables.min.js"></script>
    <!-- metro V3 -->
    <script src="theme/js/metro.js"></script>
    <!-- JQuery cookie -->
	<script src="theme/script/jquery.cookie.js"></script>
	<!-- Resize Script -->
	<script src="theme/script/jquery.ba-resize.js"></script>
	<!-- zephyros.js  -->
	<script src="js/zephyros.js" type="text/javascript"></script>
    <script src="theme/js/docs.js"></script>
    <script src="theme/js/prettify/run_prettify.js"></script>
    <script src="theme/js/ga.js"></script>
	<!--  JQuery UI -->
	<script src="theme/script/jquery-ui/jquery-ui.js"></script>	
    	
    <style>
        .login-form {
            width: 25rem;
            height: 18.75rem;
            position: fixed;
            top: 50%;
            margin-top: -9.375rem;
            left: 50%;
            margin-left: -12.5rem;
            background-color: #ffffff;
            opacity: 0;
            -webkit-transform: scale(.8);
            transform: scale(.8);
        }
    </style>
    <div id="dialog_info">
  		<form>
  		</form>		
	</div>     
    <script>

        /*
        * Do not use this is a google analytics fro Metro UI CSS
        * */
        /*
        if (window.location.hostname !== 'localhost') {

            (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
                (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                    m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
            })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

            ga('create', 'UA-58849249-3', 'auto');
            ga('send', 'pageview');

        }
  */
        $(function(){
            var form = $(".login-form");

            form.css({
                opacity: 1,
                "-webkit-transform": "scale(1)",
                "transform": "scale(1)",
                "-webkit-transition": ".5s",
                "transition": ".5s"
            });
        });
        
    dialog_info = $("#dialog_info").dialog({
	  autoOpen: false,
	  modal: true,
	  width : 500,
	  title: "메시지",
	  close: function() {
		$("#dialog_info").empty();
	  }
	});
        
        $(document).ready(function() {
        	var status = '${param.fail}';
        	
        	if(status == 'true') {
        		//zephyros.alert({contents:"${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message}"});
        		//showMetroDialog('#dialog', 'default', ($('<div>').addClass('padding20').html('hi! i am jquery content for dialog ')));        		
        		//showDialog('dialog-alert', "${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message}", 'alert');
        		//showMetroDialog('#dialog-alert', 'default', ($('<div>').addClass('padding20').html("${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message}")));
        		//pushMessage('info', "${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message}");
        		zephyros.showDialog(dialog_info, "${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message}");
        	}
        	
        	// 쿠키 검사
        	if($.cookie("zephyrosId") != null && $.cookie("zephyrosId") != "" ) {
        		$("#txtId").val($.cookie("zephyrosId"));
        		$("#isIdSave").attr("checked", true); // 체크 박스 true 값 넣기
        		$("#isIdSave").parent().attr("class", "checked"); // 체크 박스 true 표시
        		//$("#txtPasswd").focus();
        		//document.getElementById("txtPasswd").focus();
        		setTimeout(function(){$("#txtPasswd").focus();},100);
        	} else {
        		//$("#txtId").focus();
        		setTimeout(function(){$("#txtId").focus();},100);
        	}        	
        });
        
        
        $("#loginBtn").click(function (e) {
        	var userId = document.getElementsByName('txtId')[0].value;
        	var password = document.getElementsByName('txtPasswd')[0].value;
        	
        	if(userId == '') {
        		$('#loginMessage font').html('아이디를 입력하세요.');
        		$('#loginMessage font').css("color", "#771717");
        		return false;
        	} else if(password == '') {
        		$('#loginMessage font').html('비밀번호를 입력하세요.');
        		$('#loginMessage font').css("color", "#771717");
        		return false;
        	}
        	
        	if($("#isIdSave")[0].checked) {
        		$.cookie("zephyrosId", userId, {
        			expires : 30
        		});
        	} else {
        		$.removeCookie("zephyrosId");
        	}
        	
        	return true;
        });
    </script>
</head>
<body class="bg-darkTeal">
    <div class="login-form padding20 block-shadow">
        <form name="loginForm" method="post" action="loginProcess">
            <h1 class="text-light">Login to service</h1>
            <hr class="thin"/>
            <br />
            <div class="input-control text full-size" data-role="input">
                <label for="user_login">User email:</label>
                <input type="text" name="txtId" id="txtId">
                <button class="button helper-button clear"><span class="mif-cross"></span></button>
            </div>
            <br />
            <br />
            <div class="input-control password full-size" data-role="input">
                <label for="user_password">User password:</label>
                <input type="password" name="txtPasswd" id="txtPasswd">
                <button class="button helper-button reveal"><span class="mif-looks"></span></button>
            </div>
            <br />
            <br />
            <div class="form-actions">
                <button type="submit" class="button primary" id="loginBtn" name="loginBtn">Login to...</button>
                <button type="button" class="button link">Cancel</button>
            </div>
        </form>
    </div>
</body>
</html>