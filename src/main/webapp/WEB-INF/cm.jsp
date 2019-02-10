<!DOCTYPE HTML>

<html>
<head>


<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:useBean id="systemProperties" class="de.jwi.util.SystemProperties"
	scope="application" />


<title>curl Manager</title>

<link href="${contextPath}/curlmgr.css" type="text/css" rel="stylesheet">
<link rel="shortcut icon" href="${contextPath}/img/down.gif"
	type="image/gif" />
</head>

<body bgcolor="#FFFFFF">

	<h1>curl Manager</h1>

	<%@include file="submitarea.jsp"%>

	<br>

	<form name="downloads" action="${contextPath}/curl" method="post">

		<table border="1" cellpadding="3">
			<tr>
				<td class="title"><a href="${contextPath}/curl"><img
						src="${contextPath}/img/reload.gif" title="reload" width="24"
						height="24" alt="RELOAD" border="0"></a></td>
				<td colspan="7" class="title">Process Information</td>
			</tr>
			<tr>

				<td class="header-center smaller" style="width: 5%;"><script>
					function doChkAll(oChkBox) {
						var bChecked = oChkBox.checked;
						var docFrmChk = document.forms['downloads'].index;
						for (var i = 0; i < docFrmChk.length; i++) {
							docFrmChk[i].checked = bChecked;
						}
					}
				</script>Check all <input type="checkbox" name="chkAll"
					onclick="doChkAll(this);"></td>

				<th class="header-center smaller">Start Time</th>
				<th class="header-center smaller">End Time</th>
				<th class="header-center smaller">File</th>
				<th class="header-center smaller">Referer</th>
				<th class="header-center smaller">Status</th>
				<th class="mono smaller">% Total % Received % Xferd Average
					Speed Time Time Time Current Dload Upload Total Spent Left Speed</th>
			</tr>

			<c:forEach items="${managedProcesses}" var="managedProcess">

				<tr>
					<td class="smaller"><input type="checkbox" name="index"
						value="${managedProcess.id}"></td>

					<td class="row-center smaller"><fmt:formatDate type="BOTH"
							value="${managedProcess.startDate}" /></td>
					<td class="row-center smaller"><fmt:formatDate type="BOTH"
							value="${managedProcess.endDate}" /></td>
					<td class="row-center smaller"
						title="${managedProcess.commandLine}">${managedProcess.filename}</td>
					<td class="row-center smaller"><c:if
							test="${not empty managedProcess.referer}">
							<a href="${managedProcess.referer}">${managedProcess.referer}</a>
						</c:if></td>
					<td class="row-center smaller">${managedProcess.status}</td>

					<c:choose>
						<c:when test="${managedProcess.status == 'running'}">
							<c:set var="color" value="" />
						</c:when>
						<c:when test="${managedProcess.success}">
							<c:set var="color" value="green" />
						</c:when>
						<c:otherwise>
							<c:set var="color" value="red" />
						</c:otherwise>
					</c:choose>

					<td class="mono smaller ${color}">${managedProcess.lastLine}</td>
				</tr>
			</c:forEach>
		</table>

		<table>
			<tbody>

				<tr>
					<td colspan="4" class="header-left">Action on selected
						processes</td>
				</tr>

				<tr>
					<td class="row-center"><input type="submit" name="action"
						value="clean"></td>

					<td class="row-center"><input type="submit" name="action"
						value="clean and delete"></td>


					<td class="row-center"><input type="submit" name="action"
						value="clean all"></td>
				</tr>
			</tbody>
		</table>
	</form>

	<br>

	<%@include file="submitarea.jsp"%>

	<br>

	<table border="1" cellpadding="3">
		<tr>
			<td colspan="2" class="title">curl Version</td>
		</tr>
		<tr>
			<td colspan="2" class="header-left smaller">run curl -V</td>
			<td colspan="2" class="header-left smaller">${curlV}</td>
		</tr>
		<tr>
			<td colspan="2">
				<form method="post" action="${contextPath}/curl">
					<table cellspacing="0" cellpadding="3">
						<tr>
							<td class="row-left"><input type="submit" name="action"
								value="curl -V"></td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
	</table>
	<br>



	<br>

	<table border="1" cellpadding="3">
		<tr>
			<td colspan="8" class="title">Server Information</td>
		</tr>
		<tr>
			<th class="header-center smaller">Server</th>
			<th class="header-center smaller">JVM Version</th>
			<th class="header-center smaller">JVM Vendor</th>
			<th class="header-center smaller">OS Name</th>
			<th class="header-center smaller">OS Version</th>
			<th class="header-center smaller">OS Architecture</th>
			<th class="header-center smaller">Hostname</th>
			<th class="header-center smaller">IP Address</th>
		</tr>
		<tr>
			<td class="row-center smaller">${serverInfo}</td>
			<td class="row-center smaller">${systemProperties['java.version']}</td>
			<td class="row-center smaller">${systemProperties['java.vendor']}</td>
			<td class="row-center smaller">${systemProperties['os.name']}</td>
			<td class="row-center smaller">${systemProperties['os.version']}</td>
			<td class="row-center smaller">${systemProperties['os.arch']}</td>
			<td class="row-center smaller">${hostname}</td>
			<td class="row-center smaller">${hostAddress}</td>
		</tr>
		<tr>
			<td colspan="8" class="title">JVM</td>
		</tr>
		<tr>
			<td colspan="8" class="row-left smaller">Current time: <fmt:formatDate
					type="BOTH" value="${currentTime}" /> JVM Start time: <fmt:formatDate
					type="BOTH" value="${startTime}" /></td>
		</tr>
		<tr>
			<td colspan="8" class="row-left smaller">Free memory:
				${freeMemory} MB Total memory: ${totalMemory} MB Max memory:
				${maxMemory} MB</td>
		</tr>
		<c:if test="${systemProperties['os.name']=='Linux'}">
			<tr>
				<td colspan="8" class="row-left smaller">System uptime:
					${uptime} System load: ${load}</td>
			</tr>

		</c:if>
		<tr>
			<td colspan="8" class="title">curl Manager</td>
		</tr>
		<tr>
			<td colspan="8" class="row-left  smaller">Version: ${version}
				builddate: ${builddate} Z</td>
		</tr>
		<tr>
			<td colspan="8" class="row-left smaller">Download dir:
				${downloaddir} ( ${freeSpace} free)</td>
		</tr>
	</table>
	<br>

</body>
</html>