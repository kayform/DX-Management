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
                    <h1 class="text-light">BottledWater 관리</h1>  
                    <h5 class="sub-alt-header">* BottledWater을 관리합니다.</h5>
                    <P>디버그 정보 : userId=${sessionScope.userId}  userAuth=${sessionScope.userAuth} 
                    <hr class="thin bg-grayLighter">
                    <p>* PostgreSQL 서버 리스트
                    <table class="dataTable border bordered" data-role="datatable" data-searching="false" data-auto-width="t">
                        <thead>
                        <tr>
                            <td class="sortable-column sort-asc" style="width: 120px">서버명</td>
                            <td class="sortable-column">아이피</td>
                            <td class="sortable-column">포트</td>
                            <td style="width: 50px">관리</td>
                        </tr>
                        </thead>
                        <tbody>
						<c:choose>
							<c:when test="${serverList.size() < 1}">
								<tr>
									<td colspan="6" style="text-align: center;">No Data.</td>
								</tr>
							</c:when>
							<c:otherwise>
								<c:forEach items="${serverList}" var="item">
									<tr>
										<td>${item.sys_nm}</td>
										<td>${item.ip}</td>
										<td>${item.port}</td>
										<td>
											<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:databaseList('${item.sys_nm}');" ><span class="icon mif-search"></span></button>
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

<div id="dialog_databaseList">
  <form>
  </form>
</div> 


<script>    

	
    function databaseList(searchSysNm) {

    	zephyros.loading.show();
    	
    	var url = '';
    	var success = null;
    	var error = null;
    	var titleTxt = '';
    	var successTxt = '';
    	var heightVal = 650;
      	var widthVal = 1250;

		url = '/databaseList?searchSysNm='+searchSysNm;
		
   		titleTxt = 'DB 리스트';
   		dialog_databaseList = $("#dialog_databaseList").dialog({
          	title: titleTxt,
           	height: heightVal,
            width: widthVal,
   	    	buttons: {
   	    	  	"닫기": function() {
 						zephyros.loading.hide();
 						dialog_databaseList.dialog("close");
   	    	  	    $("#dialog_databaseList").empty();
   	    	  	}
   	    	},
   			close: function() {
				zephyros.loading.hide();
   				$("#dialog_databaseList").empty();
   	    	}
           });   
   		
     	zephyros.callAjax({
    		url : url,
    		type : 'post',
    		data : null,
    		success : function(data, status, xhr) {
    			zephyros.loading.show();
    			zephyros.showDialog(dialog_databaseList, data);
    		}
    	});
    }

</script>