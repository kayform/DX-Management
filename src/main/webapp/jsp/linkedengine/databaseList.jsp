<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="cell auto-size padding20 bg-white" id="cell-content">
    <p>* DB 리스트
    <table id="databaseListTable" class="dataTable border bordered" data-role="datatable" data-searching="false" data-auto-width="t">
        <thead>
        <tr>
            <td class="sortable-column sort-asc" style="width: 150px">Database 명</td>
            <td class="sortable-column">연계 상태</td>
            <td class="sortable-column">Connection 명</td>
            <td style="width: 100px">연계설정</td>
            <td style="width: 80px">연계동작</td>
        </tr>
        </thead>
    </table>
</div> 

<div id="dialog_tableList">
  <form>
  </form>
</div> 

<script>    

	$(document).ready(function() {
		
		//TODO 아래 임시방편 수정해야함
		//호출페이지에서 다이얼로그 오픈하면서 초기화하고, 문서가 로드 되면서 초기화 또다시 되어 에러 메시지 발생.
		//defalut 값인 alert로하면 얼럿창 뜬다.
		$.fn.dataTable.ext.errMode = 'throw';
		var databaseColumns = null;
		databaseColumns =  [
						{ data: 'database_name' },
						{ data: 'pg_get_status_ingest'},
						{ data: 'connect_name' },
						{ data: 'setting', defaultContent : "<button id=\"viewTableListBtn\" style=\"margin:0;height:20px;width:50px;\" class=\"button\" \"><span class=\"icon mif-search\"></span></button>"},
						{ data: 'action', defaultContent : "<button id=\"\" style=\"margin:0;height:20px;width:50px;\" class=\"button\" \"><span class=\"icon mif-play\"></span></button>"}
					];
		
		databaseTable = $("#databaseListTable").DataTable({
		    "paging" : false,
		    "destroy": true,
		    'ajax': {
				url : '/databaseListData',
				type : 'post',
				data : { searchSystemName : "${searchSystemName}"},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log('에러 발생 = '+errorThrown); 
				},
				dataSrc : ""
			},
		    "columns": databaseColumns,
		    "createdRow": function( row, data, dataIndex ) {
			       
		    	   if(data.pg_get_status_ingest == "Process stopped(0)") {
		    		   $(row)["0"].cells[4].childNodes["0"].id = 'runProcessBtn' 
		    		   $(row)["0"].cells[4].childNodes["0"].childNodes["0"].className = "icon mif-play";
		    	   }
		    	   else if(data.pg_get_status_ingest == "Process is running(0)"){
		    		   $(row)["0"].cells[4].childNodes["0"].id = 'stopProcessBtn' 
		    		   $(row)["0"].cells[4].childNodes["0"].childNodes["0"].className = "icon mif-stop";
		    	   }
		    	   else{
		    		   alert('pg_get_status_ingest 값에 오류가 있습니다. 값='+data.pg_get_status_ingest);
		    		}
		      }
		});
	
		
		$('#databaseListTable tbody').on('click', 'button', function() {
			var data = databaseTable.row( $(this).parents('tr') ).data();
			if ($(this)[0].id == "viewTableListBtn"){
				tableList("${searchSystemName}", data.database_name, data.connect_name);
			}else if ($(this)[0].id == "runProcessBtn"){
				runProcessToggle("${searchSystemName}", data.database_name, 'RUN');
			}else if ($(this)[0].id == "stopProcessBtn"){
				runProcessToggle("${searchSystemName}", data.database_name, 'STOP');
			}else {
				alert('error');
			}		
		});
		
	});

	
	function tableList(systemName, databaseName, connectName) {
    	zephyros.loading.show();
    	
    	var url = '';
    	var success = null;
    	var error = null;
    	var titleTxt = '';
    	var successTxt = '';
    	var heightVal = 650;
      	var widthVal = 1250;

		url = '/tableList';
		
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
				dialog_tableList.dialog("close");
				$("#dialog_tableList").empty();
   	    	}
           });   
   		
     	zephyros.callAjax({
    		url : url,
    		type : 'post',
    		data : 'systemName='+systemName+'&databaseName=' + databaseName+'&connectName=' + connectName,
    		success : function(data, status, xhr) {
    			zephyros.loading.show();
    			zephyros.showDialog(dialog_tableList, data);
    			zephyros.loading.hide();
    		}
    	});		
	}


    function runProcessToggle(systemName, databaseName, command) {
		zephyros.loading.show();
    	var url = '';
    	var success = null;
    	var error = null;

		url = '/runProcess';

		if (command == 'RUN' || command == 'STOP') {
	     	zephyros.callAjax({
	    		url : url,
	    		type : 'post',
	    		data : 'systemName='+systemName+'&databaseName=' + databaseName+'&command=' + command,
	    		success : function(data, status, xhr) {
	    			databaseTable.ajax.reload();
	    			zephyros.loading.hide();
	    		},
	    		error : function(){
	    			alert('error issued');
	    			zephyros.loading.hide();
	    		}
	    	});
		}
    	else{
    		alert("잘못된 값이 입력되었습니다. 모드는 RUN, STOP중에 하나여야합니다.");
    	}
    }


</script>