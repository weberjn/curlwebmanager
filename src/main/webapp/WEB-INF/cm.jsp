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

<link href="${contextPath}/dlm.css" type="text/css" rel="stylesheet">
<link rel="shortcut icon" href="${contextPath}/img/down.gif"
	type="image/gif" />
</head>

<body bgcolor="#FFFFFF">

	<h1>curl Manager</h1>

	<%@include file="submitarea.jsp" %>

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

				<td class="header-center" style="width: 5%;"><script>
					function doChkAll(oChkBox) {
						var bChecked = oChkBox.checked;
						var docFrmChk = document.forms['downloads'].index;
						for (var i = 0; i < docFrmChk.length; i++) {
							docFrmChk[i].checked = bChecked;
						}
					}
				</script> <small> Check all <input type="checkbox" name="chkAll"
						onclick="doChkAll(this);">
				</small></td>

				<th class="header-center"><small>Start Time</small></th>
				<th class="header-center"><small>End Time</small></th>
				<th class="header-center"><small>File</small></th>
				<th class="header-center"><small>Referer</small></th>
				<th class="header-center"><small>Status</small></th>
				<th class="mono"><small> % Total % Received % Xferd
						Average Speed Time Time Time Current Dload Upload Total Spent Left
						Speed</small>
				</th>
			</tr>

			<c:forEach items="${managedProcesses}" var="managedProcess">

				<tr>
					<td><small><input type="checkbox" name="index"
							value="${managedProcess.id}"></small></td>

					<td class="row-center"><small><fmt:formatDate
								type="BOTH" value="${managedProcess.startDate}" /></small></td>
					<td class="row-center"><small><fmt:formatDate
								type="BOTH" value="${managedProcess.endDate}" /></small></td>								
					<td class="row-center" title="${managedProcess.commandLine}"><small>${managedProcess.filename}</small></td>
					<td class="row-center"><small>${managedProcess.referer}</small></td>
					<td class="row-center"><small>${managedProcess.status}</small></td>
					<td class="mono"><small>${managedProcess.lastLine}</small></td>
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
						value="clean all"></td>
				</tr>
			</tbody>
		</table>
	</form>

	<br>
	
	<%@include file="submitarea.jsp" %>
	
	<br>

	<table border="1" cellpadding="3">
		<tr>
			<td colspan="2" class="title">curl Version</td>
		</tr>
		<tr>
			<td colspan="2" class="header-left"><small>run curl -V</small></td>
			<td colspan="2" class="header-left"><small>${curlV}</small></td>
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
			<th class="header-center"><small>Server</small>
			</td>
			<th class="header-center"><small>JVM Version</small>
			</td>
			<th class="header-center"><small>JVM Vendor</small>
			</td>
			<th class="header-center"><small>OS Name</small>
			</td>
			<th class="header-center"><small>OS Version</small>
			</td>
			<th class="header-center"><small>OS Architecture</small>
			</td>
			<th class="header-center"><small>Hostname</small>
			</td>
			<th class="header-center"><small>IP Address</small>
			</td>
		</tr>
		<tr>
			<td class="row-center"><small>${serverInfo}</small></td>
			<td class="row-center"><small>${systemProperties['java.version']}</small></td>
			<td class="row-center"><small>${systemProperties['java.vendor']}</small></td>
			<td class="row-center"><small>${systemProperties['os.name']}</small></td>
			<td class="row-center"><small>${systemProperties['os.version']}</small></td>
			<td class="row-center"><small>${systemProperties['os.arch']}</small></td>
			<td class="row-center"><small>${hostname}</small></td>
			<td class="row-center"><small>${hostAddress}</small></td>
		</tr>
		<tr>
			<td colspan="8" class="title">JVM</td>
		</tr>
		<tr>
			<td colspan="8" class="row-left"><small>Current time: <fmt:formatDate
						type="BOTH" value="${currentTime}" /> JVM Start time: <fmt:formatDate
						type="BOTH" value="${startTime}" /></small></td>
		</tr>
		<tr>
			<td colspan="8" class="row-left"><small>Free memory:
					${freeMemory} MB Total memory: ${totalMemory} MB Max memory:
					${maxMemory} MB</small></td>
		</tr>
		<c:if test="${systemProperties['os.name']=='Linux'}">
			<tr>
				<td colspan="8" class="row-left"><small>System uptime:
						${uptime} System load: ${load}</small></td>
			</tr>

		</c:if>
		<tr>
			<td colspan="8" class="title">curl Manager</td>
		</tr>
		<tr>
			<td colspan="8" class="row-left"><small>Version:
					${version} builddate: ${builddate} Z</small></td>
		</tr>
		<tr>
			<td colspan="8" class="row-left"><small>Download dir:
					${downloaddir} ( ${freeSpace} free)</small></td>
		</tr>
	</table>
	<br>

</body>
</html>