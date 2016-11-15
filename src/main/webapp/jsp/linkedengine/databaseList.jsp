<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="cell auto-size padding20 bg-white" id="cell-content">
    <p>* DB 리스트
    <table class="dataTable border bordered" data-role="datatable" data-searching="false" data-auto-width="t">
        <thead>
        <tr>
            <td class="sortable-column sort-asc" style="width: 150px">Database 명</td>
            <td class="sortable-column">연계 상태</td>
            <td class="sortable-column">Connection 명</td>
            <td style="width: 100px">연계설정</td>
            <td style="width: 80px">연계동작</td>
        </tr>
        </thead>
        <tbody>
			<c:choose>
				<c:when test="${databaseList.size() < 1}">
					<tr>
						<td colspan="5" style="text-align: center;">No Data.</td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach items="${databaseList}" var="item">
						<tr>
							<td>${item.database_name}</td>
							<td>${item.pg_get_status_ingest}</td>
							<td>${item.connect_name}</td>
							<td>
								<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:tableList('${searchSysNm}','${item.database_name}','${item.connect_name}');"><span class="icon mif-search"></span></button>
							</td>
							<td>
								<c:choose>
									<c:when test="${item.pg_get_status_ingest == 'Process stopped(0)'}">
										<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:runProcessToggle('${searchSysNm}','${item.database_name}','RUN');"><span class="icon mif-play"></span></button>
									</c:when>
									<c:when test="${item.pg_get_status_ingest == 'Process is running(0)'}">
										<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:runProcessToggle('${searchSysNm}','${item.database_name}','STOP');"><span class="icon mif-stop"></span></button>
									</c:when>
									<c:otherwise>
										프로세스상태 오류
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

<div id="dialog_tableList">
  <form>
  </form>
</div> 

<script>    
	function tableList(systemName, databaseName, connectName) {
    	zephyros.loading.show();
    	
    	var url = '';
    	var success = null;
    	var error = null;
    	var titleTxt = '';
    	var successTxt = '';
    	var heightVal = 650;
      	var widthVal = 700;

		url = '/tableList?systemName='+systemName+'&databaseName=' + databaseName+'&connectName=' + connectName;
		
   		titleTxt = '연계 Table 리스트';
   		dialog_tableList = $("#dialog_tableList").dialog({
          	title: titleTxt,
           	height: heightVal,
            width: widthVal,
   	    	buttons: {
   	    	  	"닫기": function() {
 						zephyros.loading.hide();
 						dialog_tableList.dialog("close");
   	    	  	    $("#dialog_tableList").empty();
   	    	  	}
   	    	},
   			close: function() {
				zephyros.loading.hide();
   				$("#dialog_tableList").empty();
   	    	}
           });   
   		
     	zephyros.callAjax({
    		url : url,
    		type : 'post',
    		data : null,
    		success : function(data, status, xhr) {
    			zephyros.loading.show();
    			zephyros.showDialog(dialog_tableList, data);
    		}
    	});		
	}


    function runProcessToggle(systemName, databaseName, command) {
    	var url = '';
    	var success = null;
    	var error = null;

    	var titleTxt = '';
    	var heightVal = 570;
      	var widthVal = 1250;

		url = '/runProcess?systemName='+systemName+'&databaseName=' + databaseName+'&command=' + command;

    	//...
		if (command == 'RUN' || command == 'STOP') {
	     	zephyros.callAjax({
	    		url : url,
	    		type : 'post',
	    		data : null,
	    		success : function(data, status, xhr) {
	    			zephyros.loading.show();
	    			zephyros.checkAjaxDialogResult(dialog_info,  data);
	    		}
	    	});
		}
    	else{
    		alert("잘못된 값이 입력되었습니다. 모드는 RUN, STOP중에 하나여야합니다.");
    	}
    }
    
    
    

</script>