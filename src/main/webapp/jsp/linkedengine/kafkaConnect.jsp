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
                    <h1 class="text-light">KAFKA CONNECT 관리</h1> 
                    <h5 class="sub-alt-header">* KAFKA CONNECT 관련.리소스들을 관리합니다.</h5>
                    <hr class="thin bg-grayLighter">
                    서버명 <input type="text"  id="searchSystemNameText" name="searchSystemNameText"  value="${searchSystemName}"> 
                    <button class="button primary" type="submit" form="serverListForm" id="searchServerBtn" name="searchServerBtn" onclick="javascript:searchServerList();" ><span class="mif-search"></span>조회</button>
                    <span id="message"></span>
                    <hr class="thin bg-grayLighter">
                    <table class="dataTable border bordered" data-role="datatable" data-searching="false" data-auto-width="false">
                        <thead>
                        <tr>
                            <td class="sortable-column sort-asc" style="width: 120px">서버명</td>
                            <td class="sortable-column">CONNECT 수</td>
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
										<td>${item.count_connect > 0 ? item.count_connect : "오류" }</td>
										<td>${item.count_connect > 0 ? "Running" : "Stop" }</td>
										<td>
											<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:showServerForm('V', '${item.sys_nm}');"><span class="icon mif-search"></span></button>
											<c:choose>
												<c:when test="${item.count_connect > 0 }">
													<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:showConnectListForm('${item.ip}','${item.port}');"><span class="icon mif-list"></span></button>
												</c:when>
												<c:otherwise>
													
												</c:otherwise>
											</c:choose>												
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
</form>

<div id="dialog_serverForm">
  <form>
  </form>
</div> 

<div id="dialog_connectList">
  <form>
  </form>
</div> 


<script>    

	function searchServerList() {
		var searchSystemName ='';
		searchSystemName = document.getElementById('searchSystemNameText').value;

    	document.getElementById("searchSystemName").value = searchSystemName;
    	document.forms["serverListForm"].action.value = "kafkaconnect";
    	return true;
    }

	
	function showConnectListForm(ip, port) {
		zephyros.loading.show();
		var url = '/kafkaConnectorConfigListForm?ip='+ip+'&port='+port;
		var width = 1000;
		var height = 800;
		var title = "KAFKA CONNECT 리스트";
		var button = null;
		
		button =  {
			    "확인": function() {
			    	dialog_connectList.dialog("close");
			      $("#dialog_connectList").empty();
			    }};
		    
		dialog_connectList = $("#dialog_connectList").dialog({
			  autoOpen: false,
			  height: height,
			  width: width,
			  modal: true,
			  title: title,
			  resizable: false,
			  buttons: button,
			  close: function() {
				$("#dialog_connectList").empty();
			  }
			});

		
		var data = null;
		
		
	 	zephyros.callAjax({
			url : url,
			type : 'post',
			data : data,
			success : function(data, status, xhr) {
				zephyros.loading.hide();
				zephyros.showDialog(dialog_connectList, data);
			}
		}); 
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