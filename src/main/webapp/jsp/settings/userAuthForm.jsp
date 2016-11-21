<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
</style>
<div class="widget">
	<form id="form03" name="form03"  method="post">
		<table summary="사용자정보등록/수정" style="width: 100%;" id="userAuthList" class="table table-bordered table-condensed">
			<colgroup>
				<col width="90%">
				<col width="10%">
			</colgroup>			
			<tbody>
				<c:forEach var="item" items="${menuList}">
				
					<c:if test="${item.sub_order == '1' }">
						<tr style="height: 30px;">
							<td>${item.name}</td>
							<td>
								<c:choose>										
									<c:when test="${item.id == 'DSB000'}">
										<label class="checkbox"><input type="checkbox" class="checkbox" value="10" checked="checked" id="${item.id}" name="${item.id}" disabled = "true"></label>
									</c:when>
									<c:when test="${item.is_cheack == 0}">
										<label class="checkbox"><input type="checkbox" class="checkbox" value="10" id="${item.id}" name="${item.id}" onchange="setAuthMain(this, ${item.id}, ${item.sub_count})"></label>
									</c:when>
									<c:otherwise>
										<label class="checkbox"><input type="checkbox" class="checkbox" value="10" checked="checked" id="${item.id}" name="${item.id}" onchange="setAuthMain(this, ${item.id}, ${item.sub_count})"></label>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<c:forEach var="subItem" items="${menuList}">
							<c:if test="${subItem.parent_id == item.item_order}">
								<c:choose>										
									<c:when test="${subItem.use_yn =='Y' }">
										<tr style="height: 30px;">								
											<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ▷${subItem.name }</td>
											<td>
												<c:if test="${subItem.is_cheack == 0}">
													<label class="checkbox"><input type="checkbox" class="checkbox" value="10" id="${subItem.id }" name="chkSub-${item.id}-${subItem.sub_order -1}" menu-parent-id="${item.id}" onchange="setAuthParent(this, '${item.id}');"></label>
												</c:if>
												<c:if test="${subItem.is_cheack != 0}">
													<label class="checkbox"><input type="checkbox" class="checkbox" value="10" checked="checked" id="${subItem.id }" name="chkSub-${item.id}-${subItem.sub_order -1}" menu-parent-id="${item.id}" onchange="setAuthParent(this, '${item.id}');"></label>
												</c:if>
											</td>
										</tr>
									</c:when>
									<c:otherwise>
										<tr style="display: none;">
											<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ▷${subItem.name }</td>
											<td>
												<label class="checkbox"><input type="checkbox" class="checkbox" value="10" id="${subItem.id }" name="chkSub-${item.id}-${subItem.sub_order -1}" onchange="" readonly="readonly"></label>
											</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</c:if>				
						</c:forEach>						
					</c:if> 
				</c:forEach>
			</tbody>
		</table>
	</form>	
</div>
<script type="text/javascript">
	$(document).ready(function() {

	});
	
	function setAuthMain(o, id, sub_count) { 
		//alert(o.id);
		//alert($('#' + $('input[name=chkSub-' + o.id + ']').attr("id")).length);
		for(var i = 1; i <= sub_count; i++) {
			if (o.checked == true) {
				$('#' + $('input[name=chkSub-' + o.id + '-' + i +']').attr("id"))[0].checked = true;	
			} else {
				$('#' + $('input[name=chkSub-' + o.id + '-' + i +']').attr("id"))[0].checked = false;	
			}
		}
	}
	
	function setAuthParent(o, id) { 
		// console.log(id);
		if (o.checked == true) {
			$('#' + id)[0].checked = true;
		} else {
			var checked = false;
			for(var i=0;i<$('input[menu-parent-id=' + id).length;i++) {
				if($('input[menu-parent-id=' + id)[i].checked == true) {
					checked = true;
					break;
				}
			}
			
			if(!checked) {
				$('#' + id)[0].checked = false;
			}
		}
	}
	
	function setAuth(o, id) {
		var userId = '${userId}';
		var mode;
		if (o.checked == true) {
			mode = 'I';
		} else {
			mode = 'D';
		}
		var reqData = {
			mode: mode,
			userId: userId,
			menuId: id
		};
		
		
		$.ajax({
			url: '/userAuthProcess',
			type: 'post',
			data: reqData,
			dataType: 'json',
			success : function (data, status, xhr) {
				
			}, error: function (e) { 
				ajaxErrorHandler(e);
			}
		});		
	} 
	/*
	function saveAuth() {
		var rowCount = $('#userAuthList tbody tr').length;
		var root = $('#userAuthList tbody tr td input');
		var value = "";
		
		var userId = '${userId}';
		var reqData = "";
		
		for (var i = 0; i < rowCount; i++) {
			if(i!=0) reqData += "&";
			reqData += "mode=" + (root[i].checked ? "I" : "D");
			reqData += "&userId=" + userId;
			reqData += "&menuId=" + root[i].id;
		}
		// console.log(reqData);
		
		$.ajax({
			url: '/userAuthProcess',
			type: 'post',
			data: reqData,
			//dataType: 'json',
			success : function (data, status, xhr) {
				// console.log(status);
				// console.log(data);
			}, error: function (e) { 
				ajaxErrorHandler(e);
			}
		});
	}
	*/
</script>