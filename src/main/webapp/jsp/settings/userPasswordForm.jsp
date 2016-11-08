<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<style>
	h1,h2,h3,h4,h5,h6{margin:0 0 10px;font-weight:400;color:#ffffff;}h1.glyphicons,h2.glyphicons,h3.glyphicons,h4.glyphicons,h5.glyphicons,h6.glyphicons{color:#ffffff;}
	select,textarea,input[type="text"],input[type="password"],input[type="datetime"],input[type="datetime-local"],input[type="date"],input[type="month"],input[type="time"],input[type="week"],input[type="number"],input[type="email"],input[type="url"],input[type="search"],input[type="tel"],input[type="color"],.uneditable-input{margin-bottom:0px;}
	
	
	.table-condensed th {background-color: rgba(0, 0, 0, 0.5); text-align: left;}
	.table-condensed td {vertical-align: middle;}
	#pinfoData label {float: left;vertical-align: middle;padding-right:20px;}
	.input-append .add-on{padding:4px;} 
	.input-append {
		margin-bottom: 0px;
	}
</style>
<!-- 내용 -->
<div class="widget">
	<form data-role="validator" data-on-before-submit="no_submit" data-on-submit="return false" data-hint-mode="hint" data-hint-easing="easeOutBounce" id="passwordForm" name="passwordForm"  method="post">
		<table summary="사용자정보등록/수정" style="width: 100%;" class="table table-bordered table-condensed">
			<colgroup>
				<col width="30%">
				<col width="70%">				
			</colgroup>
			<tbody>				
				<tr>
					<th scope="row">비밀번호</th>
					<td>			
						<input type="password" id="password1" name="password1"  value="" style="width:95%;">
					</td>										
				</tr>
				<tr>				
					<th scope="row">비밀번호 확인</th>
					<td>
						<input type="password" id="password2" name="password2"  value="" style="width:95%;" data-validate-func="required, custom" data-validate-arg=",checkPassword" data-validate-hint="패스워드가 비워있거나, 맞지 않습니다." data-validate-hint-position="top">
					</td>			
				</tr>
			</tbody>
		</table>

	</form>	
</div>

<script type="text/javascript">
	function checkPassword(value) {
		var password1 = $("#password1").val();
		var password2 = value;
		if(password1 != password2) {
			return false;
		}
		return true;
	}
</script>
