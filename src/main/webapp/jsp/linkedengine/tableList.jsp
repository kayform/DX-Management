<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="cell auto-size padding20 bg-white" id="cell-content">
    <p>Kafka Connection : <input type="text" id="connectName" name="connectName" readonly="readonly" value="${connectName}" style="width:30%;">
    <p>* 연계 테이블 리스트
    <table id="tableListTable" class="dataTable border bordered" data-role="datatable" data-searching="false" data-auto-width="t">
        <thead>
        <tr>
            <td class="sortable-column sort-asc" style="width: 150px">스키마 명</td>
            <td class="sortable-column">테이블 명</td>
        </tr>
        </thead>
    </table>
</div> 

<script>    

$(document).ready(function() {
	var tableColumns = null;
	tableColumns =  [
					{ data: 'table_schema' },
					{ data: 'table_name'}
				];
	
	tableListTable = $("#tableListTable").DataTable({
	    "paging" : true,
	    'ajax': {
			url : '/tableListData',
			type : 'post',
			data : { systemName : "${systemName}",databaseName : "${databaseName}",connectName : "${connectName}"},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log('에러 발생 = '+errorThrown); 
			},
			dataSrc : ""
		},
	    "columns": tableColumns
	});
	
});    
    

</script>