<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

    <div class="page-content">
        <div class="flex-grid no-responsive-future" style="height: 100%;">
            <div class="row" style="height: 100%">
                <div class="cell size-x200" id="cell-sidebar" style="background-color: #71b1d1; height: 100%">
                </div>            
                <div class="cell auto-size padding20 bg-white" id="cell-content">
                    <h1 class="text-light">Cloudera 관리</h1> 
                    <h5 class="sub-alt-header">* Cloudera-Manager 서버 상태 및 관리를 수행합니다.</h5>
                    <hr class="thin bg-grayLighter">
                    서버명 <input type="text"  id="searchSystemNameText" name="searchSystemNameText"  value="${searchSystemName}"> 
                    <button class="button primary" type="submit" form="serverListForm" id="searchServerBtn" name="searchServerBtn" onclick="javascript:searchServerList();" ><span class="mif-search"></span>조회</button>
                    <span id="message"></span>
                    <hr class="thin bg-grayLighter">
                    <table class="dataTable border bordered" data-role="datatable" data-searching="false" data-auto-width="false">
                        <thead>
                        <tr>
                            <td class="sortable-column sort-asc" style="width: 120px">서버명</td>
                            <td class="sortable-column">서비스항목</td>
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
										<td>${item.services}</td>
										<td>${item.servicesCount > 0 ? "Running" : "Stop" }</td>
										<td>
											<button style="margin:0;height:20px;width:50px;" class="button" onclick="javascript:showServerForm('V', '${item.sys_nm}');"><span class="icon mif-search"></span></button>
											<c:choose>
												<c:when test="${item.servicesCount > 0 }">
													<button style="margin:0;height:20px;width:50px;" class="button" onclick="javascript:showClouderaServicesListForm('${item.sys_nm}');"><span class="icon mif-list"></span></button>
												</c:when>
												<c:otherwise>
													
												</c:otherwise>
											</c:choose>												
											<button style="margin:0;height:20px;width:50px;" class="button" onclick="javascript:openClouderaManager('${item.ip}','${item.port}');"><span class="icon mif-link"></span></button>
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

    
<form id="serverListForm" name="serverListForm" method="post">
	<input type="hidden" id="searchSystemName" name="searchSystemName"> 
</form>

<div id="dialog_serverForm">
  <form>
  </form>
</div> 

<div id="dialog_clouderaServiceList">
  <form>
  </form>
</div> 


<script>    

	function searchServerList() {
		var searchSystemName ='';
		searchSystemName = document.getElementById('searchSystemNameText').value;

    	document.getElementById('searchSystemName').value = searchSystemName;

    	document.getElementById('serverListForm').action = 'cloudera';
    	document.getElementById('serverListForm').submit();
    }

	function openClouderaManager(ip, port){
		window.open('http://'+ip+':'+port, 'newWindow');
	}
	
	
	
	function showClouderaServicesListForm(searchSystemName) {
		zephyros.loading.show();
		var url = '/clouderaServicesListForm?searchSystemName='+searchSystemName;
		var width = 1000;
		var height = 560;
		var title = "CLOUDERA 서비스 리스트";
		var button = null;
		
		button =  {
			    "확인": function() {
			    	dialog_clouderaServiceList.dialog("close");
			      $("#dialog_clouderaServiceList").empty();
			    }};
		    
		dialog_clouderaServiceList = $("#dialog_clouderaServiceList").dialog({
			  autoOpen: false,
			  height: height,
			  width: width,
			  modal: true,
			  title: title,
			  resizable: false,
			  buttons: button,
			  close: function() {
				$("#dialog_clouderaServiceList").empty();
			  }
			});

		
		var data = null;
		
		
	 	zephyros.callAjax({
			url : url,
			type : 'post',
			data : data,
			success : function(data, status, xhr) {
				zephyros.loading.hide();
				zephyros.showDialog(dialog_clouderaServiceList, data);
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