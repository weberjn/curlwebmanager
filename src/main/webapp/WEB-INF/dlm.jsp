<jsp:useBean id="systemProperties" class="dlm.util.SystemProperties" scope="application" />

<html>
<head>

<title>manager</title>

<link href="${contextPath}/dlm.css" type="text/css" rel="stylesheet">
</head>

<body bgcolor="#FFFFFF">

	<h1>JVM</h1>
	<p>Free memory: 37.06 MB Total memory: 75.00 MB Max memory: 843.00
		MB</p>


	<hr size="1" noshade="noshade">
	<table border="">
		<tr>
			<td class="page-title" bordercolor="#000000" align="left" nowrap>
				<font size="+2">Server Status</font>
			</td>
		</tr>
	</table>
	<br>

	<table border="1" cellpadding="3">
		<tr>
			<td colspan="2" class="title">Submit</td>
		</tr>
		<tr>
			<td colspan="2" class="header-left"><small>Submit curl command</small></td>
		</tr>
		<tr>
			<td colspan="2">
				<form method="post" action="${contextPath}/dlm">
					<table cellspacing="0" cellpadding="3">
						<tr>
							<td class="row-right"><small>curl:</small></td>
							<td class="row-left"><input type="text" name="curl"
								size="80"></td>
						</tr>
						<tr>
							<td class="row-right">&nbsp;</td>
							<td class="row-left"><input type="submit" name="action" value="Submit">
							</td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
	</table>
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
				<form method="post" action="${contextPath}/dlm">
					<table cellspacing="0" cellpadding="3">
						<tr>
							<td class="row-left"><input type="submit" name="action" value="curl -V">
							</td>
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
			<td class="header-center"><small>Server</small></td>
			<td class="header-center"><small>JVM Version</small></td>
			<td class="header-center"><small>JVM Vendor</small></td>
			<td class="header-center"><small>OS Name</small></td>
			<td class="header-center"><small>OS Version</small></td>
			<td class="header-center"><small>OS Architecture</small></td>
			<td class="header-center"><small>Hostname</small></td>
			<td class="header-center"><small>IP Address</small></td>
		</tr>
		<tr>
			<td class="row-center"><small>${serverInfo}</small></td>
			<td class="row-center"><small>${systemProperties['java.version']}</small></td>
			<td class="row-center"><small>${systemProperties['java.vendor']}</small></td>
			<td class="row-center"><small>${systemProperties['os.name']}</small></td>
			<td class="row-center"><small>${systemProperties['os.version']}</small></td>
			<td class="row-center"><small>${systemProperties['os.arch']}</small></td>
			<td class="row-center"><small>${computerName}</small></td>
			<td class="row-center"><small>${hostAddress}</small></td>
		</tr>
			<tr>
			<td colspan="8" class="title">JVM</td>
			</tr>
			<tr>
			<td colspan="8" class="row-left">
			<small>Free memory: ${freeMemory} MB Total memory: ${totalMemory} MB Max memory: ${maxMemory} MB</small></td>
			</tr>
			<tr>
			<td colspan="8" class="row-left">
			<small>Start time: ${startTime}</small></td>
			</tr>			
	</table>
	<br>

</body>
</html>