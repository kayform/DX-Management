<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

    <div class="page-content">
        <div class="flex-grid no-responsive-future" style="height: 100%;">
            <div class="row" style="height: 100%">
                <div class="cell size-x200" id="cell-sidebar" style="background-color: #71b1d1; height: 100%">
                </div>            
                <div class="cell auto-size padding20 bg-white" id="cell-content">
                    <h1 class="text-light">BottledWater 관리</h1>  
                    <h5 class="sub-alt-header">* BottledWater을 관리합니다.</h5>
                    <hr class="thin bg-grayLighter">
                    <table  id="serverListTable" class="dataTable border bordered" data-role="datatable" data-searching="false" data-auto-width="false">
                        <thead>
                        <tr>
                            <td class="sortable-column sort-asc" style="width: 200px">서버명</td>
                            <td class="sortable-column">아이피</td>
                            <td class="sortable-column">포트</td>
                            <td style="width: 50px">관리</td>
                        </tr>
                        </thead>
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

$(document).ready(function() {
	var columns = null;
	columns =  [
					{ data: 'sys_nm' },
					{ data: 'ip'},
					{ data: 'port' },
					{ data: 'mng', defaultContent : "<button id=\"viewDatabaseListBtn\" style=\"margin:0;height:20px;width:50px;\" class=\"button\" \"><span class=\"icon mif-search\"></span></button>"}
				];
	//language 셋팅을 하면..... 껌뻑한다. ㅠ.ㅠ
	table = $("#serverListTable").DataTable({
        "language": {
//            "url": "//cdn.datatables.net/plug-ins/1.10.12/i18n/Korean.json"
        },
	    "destroy": true,
	    "paging" : false,
 		"ajax": {
			url : '/bottledwaterData',
			type : 'post',
			error : function(jqXHR, textStatus, errorThrown) {
				console.log('에러 발생 = '+errorThrown); 
			},
			dataSrc : ""
		},
	    "columns": columns
	});
	
	$('#serverListTable tbody').on('click', 'button', function() {
		var data = table.row( $(this).parents('tr') ).data();
		if ($(this)[0].id == "viewDatabaseListBtn"){
			databaseList(data.sys_nm);
		}else {
			alert('error');
		}		
	});
});


	
    function databaseList(searchSystemName) {

    	zephyros.loading.show();
    	
    	var url = '';
    	var success = null;
    	var error = null;
    	var titleTxt = '';
    	var successTxt = '';
    	var heightVal = 650;
      	var widthVal = 1250;

		url = '/databaseList?searchSystemName='+searchSystemName;
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
				dialog_databaseList.dialog("close");
   				$("#dialog_databaseList").empty();
   	    	}
           });   
   		
     	zephyros.callAjax({
    		url : url,
    		type : 'post',
    		data : null,
    		success : function(data, status, xhr) {
    			zephyros.showDialog(dialog_databaseList, data);
    		}
    	});
    }

</script>