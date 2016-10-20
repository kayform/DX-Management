<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<!DOCTYPE html>
<html>

<tiles:insertAttribute name="header"/>
	
	<!-- Start Content -->
	<div class="container">
		
		
		<div id="wrapper">
			<div id="titlebar" style="padding: 15 15 15 15; margin-top: 15px;">
			<!-- <div id="titlebar" style="padding: 15 15 15 15;"> -->
				<!-- <img src="/img/9.png" width="160px;" style="padding-left: 10px;"> -->
				<!-- <font size="5">D-Specto</font> -->
			</div>
		</div>
		
		<div id="wrapper">
		
			<div id="menu" class="hidden-phone">
				<tiles:insertAttribute name="menu"/>
			</div>
			
			
			<tiles:insertAttribute name="content"/>
			
			<div id="footer">
				<tiles:insertAttribute name="footer"/>
			</div>
		</div>
		
	</div>
	
	<!-- JQueryUI v1.9.2 -->
	<script src="theme/scripts/jquery-ui-1.9.2.custom/js/jquery-ui-1.9.2.custom.min.js"></script>
	
	<!-- JQueryUI Touch Punch -->
	<!-- small hack that enables the use of touch events on sites using the jQuery UI user interface library -->
	<script src="theme/scripts/jquery-ui-touch-punch/jquery.ui.touch-punch.min.js"></script>
	
	<!-- MiniColors -->
	<script src="theme/scripts/jquery-miniColors/jquery.miniColors.js"></script>
	
	<!-- Themer -->
	<script>
	var themerPrimaryColor = '#00aba9';
	var themerSelectedTheme = '5';
	// 스크롤바 CSS
	$("html").addClass($("#content").attr("class")+"-scrollbar");
	</script>
	<script src="theme/scripts/jquery.cookie.js"></script>
	<script src="theme/scripts/themer.js"></script>
	
	<!-- jQuery Validate -->
	<script src="theme/scripts/jquery-validation/dist/jquery.validate.min.js" type="text/javascript"></script>
	<script src="theme/scripts/jquery-validation/dist/additional-methods.min.js" type="text/javascript"></script>
	<!-- <script src="theme/scripts/form_validator.js" type="text/javascript"></script> -->
	
	<!-- Resize Script -->
	<script src="theme/scripts/jquery.ba-resize.js"></script>
	
	<!-- Uniform -->
	<script src="theme/scripts/pixelmatrix-uniform/jquery.uniform.min.js"></script>
	
	<!-- DataTables -->
	<script src="theme/scripts/DataTables/media/js/jquery.dataTables.min.js"></script>
	<script src="theme/scripts/DataTables/media/js/DT_bootstrap.js"></script>
	
	<!-- Bootstrap Script -->
	<script src="bootstrap/js/bootstrap.min.js"></script>
	
	<!-- Bootstrap Extended -->
	<script src="bootstrap/extend/bootstrap-select/bootstrap-select.js"></script>
	<script src="bootstrap/extend/bootstrap-toggle-buttons/static/js/jquery.toggle.buttons.js"></script>
	<script src="bootstrap/extend/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js"></script>
	<script src="bootstrap/extend/jasny-bootstrap/js/jasny-bootstrap.min.js" type="text/javascript"></script>
	<script src="bootstrap/extend/jasny-bootstrap/js/bootstrap-fileupload.js" type="text/javascript"></script>
	<script src="bootstrap/extend/bootbox.js" type="text/javascript"></script>
	<!-- 
	<script src="bootstrap/extend/bootstrap-wysihtml5/js/wysihtml5-0.3.0_rc2.min.js" type="text/javascript"></script>
	<script src="bootstrap/extend/bootstrap-wysihtml5/js/bootstrap-wysihtml5-0.0.2.js" type="text/javascript"></script>
	 -->
	<!-- Custom Onload Script -->
	<script src="theme/scripts/load.js"></script>

	<script src="/js/jquery.fileDownload.js"></script>

	
</body>
</html>