<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<!DOCTYPE html>
<html>
<head>
	<tiles:insertAttribute name="header"/>
</head>
<body data-spy="scroll" data-target=".subnav" data-offset="50" data-twttr-rendered="true">
	<!-- Start Content -->	
	<tiles:insertAttribute name="top"/>
	<tiles:insertAttribute name="content"/>
	<tiles:insertAttribute name="footer"/>
	<!-- 
	<div class="container">
		<div id="top">
				<tiles:insertAttribute name="top"/>
		</div>
		<div id="page-content">
			<tiles:insertAttribute name="content"/>
		</div>

		<div id="footer">
			<tiles:insertAttribute name="footer"/>
		</div>
		
	</div>
	 --> 
</body>
</html>