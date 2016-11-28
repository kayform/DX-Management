<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="cell auto-size padding20 bg-white" id="cell-content">
    <p>Kafka Connection : <input type="text" id="connectName" name="connectName" readonly="readonly" value="${connectName}" style="width:30%;">
    <p>* 연계 테이블 등록 리스트
    <table id="tableRegistrationList" class="dataTable border bordered" data-role="datatable" data-searching="false" data-auto-width="t">
        <thead>
        <tr>
            <td class="sortable-column sort-asc" style="width: 150px">스키마 명</td>
            <td class="sortable-column">테이블 명'${table_schema}'</td>
            <td class="sortable-column">선택</td>
        </tr>
        </thead>
    </table>
</div> 

<script>    

$(document).ready(function() {
	var tableColumns = null;
	tableColumns =  [
					{ "data": 'table_schema' },
					{ "data": 'table_name'},
					{ "data": 'checked', defaultContent : "<label class=\"checkbox\"><input type=\"checkbox\" class=\"checkbox\" value=\"10\" id=\'data.table_schema\' name=\"\" onchange=\"setCheckBox(this)\"></label>"}
				];
	
	tableRegistrationList = $("#tableRegistrationList").DataTable({
	    "paging" : true,
	    "serverSide" : true,
	    //"stateSave" : true,	    
	    'ajax': {
			url : '/tableRegistrationListData',
			type : 'post',
			data : { systemName : "${systemName}",databaseName : "${databaseName}",connectName : "${connectName}"},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log('에러 발생 = '+errorThrown); 
			}
		},
        "columns": tableColumns,
	    "createdRow": function( row, data, dataIndex ) {
	    	   if(data.is_checked == "0") {
	    		   $(row)["0"].cells[2].childNodes["0"].childNodes["0"].checked = false;
	    	   }
	    	   else if(data.is_checked == "1"){
	    		   $(row)["0"].cells[2].childNodes["0"].childNodes["0"].checked = true;
	    	   }
	    	   else{
	    		   alert('is_checked  값에 오류가 있습니다. 값='+data.is_checked );
	    		}
	      }
        
	});
	
	function setCheckBox(o) { 
		alert("setCheckBox fun");
		
	}
	

	
	
});    
    

</script>