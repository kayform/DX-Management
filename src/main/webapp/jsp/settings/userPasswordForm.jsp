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
	<form id="passwordForm" name="passwordForm"  method="post">
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
						<input type="password" id="password2" name="password2"  value="" style="width:95%;">						
					</td>					
				</tr>
			</tbody>
		</table>
	</form>	
</div>

<script>

</script>

<script type="text/javascript">
	$(document).ready(function() {
		
		jQuery.validator.addMethod("passwordCheck", function(value, element) {
			var password1 = $("#password1").val();
			var password2 = value;
			if(password1 != password2) {
				return false;
			}
			return true;
		}, "패스워드가 맞지 않습니다.");

		$("#passwordForm").validate({
			rules: {
				password1: {required : true},
				password2: {required : true, passwordCheck: true},
			}, messages: {
				password1: {required : "이 필드는 필수입니다."},
				password2: {required : "이 필드는 필수입니다."},
			}
		});
	});
	
</script>
