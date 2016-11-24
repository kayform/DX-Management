<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="description" content="Metro, a sleek, intuitive, and powerful framework for faster and easier web development for Windows Metro Style.">
    <meta name="keywords" content="HTML, CSS, JS, JavaScript, framework, metro, front-end, frontend, web development">
    <meta name="author" content="Sergey Pimenov and Metro UI CSS contributors">
    
    <title>eXperdb Webconsole</title>
    
    <link rel='shortcut icon' type='image/x-icon' href='img/favicon.ico' />

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

	<!-- dataTables -->	
	//<link href="theme/script/jquery-ui/jquery-ui.css" rel="stylesheet">
    <script src="theme/js/jquery.dataTables.min.js"></script>
    <script src="theme/js/dataTables.select.min.js"></script>
    
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
	
		<!-- JQuery VAlidation -->
    <script src="theme/script/jquery-validation/jquery.validate.js"></script>
    <script src="theme/script/jquery-validation/additional-methods.js"></script>
	
	<!-- Themer -->
	<script>
	var themerPrimaryColor = '#00aba9';
	var themerSelectedTheme = '5';
	// 스크롤바 CSS
	$("html").addClass($("#content").attr("class")+"-scrollbar");
	</script>
	
	<style>
		h1,h2,h3,h4,h5,h6{margin:0 0 10px;font-weight:400;}
        html, body {
            height: 100%;
        }
        body {
        }
        .page-content {
            padding-top: 3.125rem;
            min-height: 100%;
            height: 100%;
        }
        .table .input-control.checkbox {
            line-height: 1;
            min-height: 0;
            height: auto;
        }

        @media screen and (max-width: 800px){
            #cell-sidebar {
                flex-basis: 52px;
            }
            #cell-content {
                flex-basis: calc(100% - 52px);
            }
        }
        
        .ui-progressbar {
    		position: relative;
    		background-color: #ccc;
  		}

  		.progress-label {
    		position: absolute;
    		left: 50%;
    		top: 4px;
    		font-weight: bold;
    		text-shadow: 1px 1px 0 #fff;
  		}
  		
  		input[readonly] {
  		    pointer-events: none;  
  		    background-color:#ebebe4;  		    
  		}
  		input[readonly].default-cursor {
    		cursor: default;  		
    	}
    </style>

    <script>
        $(function(){
            $('.sidebar').on('click', 'li', function(){
                if (!$(this).hasClass('active')) {
                    $('.sidebar li').removeClass('active');
                    $(this).addClass('active');
                }
            })
        })
         
    </script>
