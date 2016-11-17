<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="cell auto-size padding20 bg-white" id="cell-content">
    <p>* KAFKA CONNECT CONFIG 정보
    <table class="dataTable border bordered" data-role="datatable" data-searching="false" data-auto-width="t">
        <thead>
        <tr>
            <td class="column" style="width: 150px">항목</td>
            <td class="column">값</td>
        </tr>
        </thead>
        <tbody>
			<tr>
				<td>connector.class</td>
				<td>${kafkaConnectorConfig.connectorClass}</td>
			</tr>
			<tr>
				<td>flush.size</td>
				<td>${kafkaConnectorConfig.flushSize}</td>
			</tr>
			<tr>
				<td>hadoop.conf.dir</td>
				<td>${kafkaConnectorConfig.hadoopConfDir}</td>
			</tr>
			<tr>
				<td>topics</td>
				<td>${kafkaConnectorConfig.topics}</td>
			</tr>
			<tr>
				<td>tasks.max</td>
				<td>${kafkaConnectorConfig.tasksMax}</td>
			</tr>
			<tr>
				<td>hdfs.url</td>
				<td>${kafkaConnectorConfig.hdfsURL}</td>
			</tr>
			<tr>
				<td>name</td>
				<td>${kafkaConnectorConfig.name}</td>
			</tr>
			<tr>
				<td>rotate.interval.ms</td>
				<td>${kafkaConnectorConfig.rotateIntervalMS}</td>
			</tr>
        </tbody>
    </table>
</div> 


<script>    


    

</script>