<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="cell auto-size padding20 bg-white" id="cell-content">
    <p>Kafka Connection : <input type="text" id="connectName" name="connectName" readonly="readonly" value="${connectName}" style="width:30%;">
    <p>* 연계 테이블 리스트
    <table class="dataTable border bordered" data-role="datatable" data-searching="false" data-auto-width="t">
        <thead>
        <tr>
            <td class="sortable-column sort-asc" style="width: 150px">스키마 명</td>
            <td class="sortable-column">테이블 명</td>
        </tr>
        </thead>
        <tbody>
			<c:choose>
				<c:when test="${tableList.size() < 1}">
					<tr>
						<td colspan="5" style="text-align: center;">No Data.</td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach items="${tableList}" var="item">
						<tr>
							<td>${item.table_schema}</td>
							<td>${item.table_name}</td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
        </tbody>
    </table>
</div> 

<script>    
    
    

</script>