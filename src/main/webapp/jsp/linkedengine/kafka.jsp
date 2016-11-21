<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

    <div class="page-content">
        <div class="flex-grid no-responsive-future" style="height: 100%;">
            <div class="row" style="height: 100%">
                <div class="cell size-x200" id="cell-sidebar" style="background-color: #71b1d1; height: 100%">
                </div>            
                <div class="cell auto-size padding20 bg-white" id="cell-content">
                    <h1 class="text-light">KAFKA 관리</h1> 
                    <h5 class="sub-alt-header">* KAFKA 관련.리소스들을 관리합니다.</h5>
                    <hr class="thin bg-grayLighter">
                    서버명 <input type="text"  id="searchSystemNameText" name="searchSystemNameText"  value="${searchSystemName}"> 
                    유형
					<select id="searchTypeText" name="searchTypeText" style="width: 250px;"  selected="selected">			
						<option value="%">전체</option>
						<option value="KAFKA" <c:if test="${searchType == 'KAFKA'}">selected="selected"</c:if> >KAFKA</option>
						<option value="KAFKA-SCHEMA-REGISTR" <c:if test="${searchType == 'KAFKA-SCHEMA-REGISTR'}">selected="selected"</c:if>>KAFKA-SCHEMA-REGISTR</option>
					</select>
                    <button class="button primary" type="submit" form="serverListForm" id="searchServerBtn" name="searchServerBtn" onclick="javascript:searchServerList();" ><span class="mif-search"></span>조회</button>
                    <span id="message"></span>
                    <hr class="thin bg-grayLighter">
                    <table class="dataTable border bordered" data-role="datatable" data-searching="false" data-auto-width="false">
                        <thead>
                        <tr>
                            <td class="sortable-column sort-asc" style="width: 200px">서버명</td>
                            <td class="sortable-column">유형</td>
                            <td class="sortable-column">상태</td>
                            <td style="width: 200px">관리</td>
                        </tr>
                        </thead>
                        <tbody>
						<c:choose>
							<c:when test="${serverList.size() < 1}">
								<tr>
									<td colspan="4" style="text-align: center;">No Data.</td>
								</tr>
							</c:when>
							<c:otherwise>
								<c:forEach items="${serverList}" var="item">
									<tr>
										<td>${item.sys_nm}</td>
										<td>${item.type}</td>
										<td>${item.status==1 ? "Running" : "Stop" }</td>
										<td>
											<button style="margin:0;height:20px;width:50px;" class="button" onclick="javascript:showServerForm('V', '${item.sys_nm}');"><span class="icon mif-search"></span></button>
<!-- 											<button style="margin:0;height:20px;width:50px;" class="button" onclick="javascript:alert('구현 요함');"><span class="icon mif-pencil"></span></button>
											<button style="margin:0;height:20px;width:50px;" class="button" onclick="javascript:alert('구현 요함');"><span class="icon mif-cancel"></span></button> -->
										</td>
									</tr>
								</c:forEach>
							</c:otherwise>
						</c:choose>
                        </tbody>
                    </table>
                </div> 
            </div>
        </div>
    </div>

    
<form id="serverListForm" name="serverListForm" method="post" >
	<input type="hidden" id="searchSystemName" name="searchSystemName"> 
	<input type="hidden" id="searchType" name="searchType"> 
</form>

<div id="dialog_serverForm">
  <form>
  </form>
</div> 


<script>    

	function searchServerList() {
		var searchSystemName ='';
		var searchType = '';
		searchSystemName = document.getElementById('searchSystemNameText').value;
		searchType = document.getElementById('searchTypeText').value;


    	document.getElementById("searchSystemName").value = searchSystemName;
    	document.getElementById("searchType").value = searchType;
    	document.forms["serverListForm"].action.value = "kafka";
    	return true;
    }    
	function showServerForm(mode, sys_nm) {
		zephyros.loading.show();
		var url = '/serverForm?mode='+mode;
		var width = 840;
		var height = 420;
		var title = "서버 조회";
		var button = null;
		
		if (mode == "V") {

			button =  {
				    "확인": function() {
				    	dialog_serverForm.dialog("close");
				      $("#dialog_serverForm").empty();
				    }};
		}
		    
		dialog_serverForm = $("#dialog_serverForm").dialog({
			  autoOpen: false,
			  height: height,
			  width: width,
			  modal: true,
			  title: title,
			  resizable: false,
			  buttons: button,
			  close: function() {
				$("#dialog_serverForm").empty();
			  }
			});

		
		var data = null;
		
		if (mode != 'I'){
			data = {
				sys_nm : sys_nm
			}
		}
		
	 	zephyros.callAjax({
			url : url,
			type : 'post',
			data : data,
			success : function(data, status, xhr) {
				zephyros.loading.hide();
				zephyros.showDialog(dialog_serverForm, data);
			}
		}); 
	}    
    

</script>