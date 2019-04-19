<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/">
		<html>
			<body>
				<table class="trademust" width="100%">
					<tr>
						<td class="trademust">
							<div>
								<table class="trademust">
									<tr>
										<td class="xslt_fieldHeader" colspan="2">Role Details
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td class="trademust" align="left" style="width:50%">
							<table class="trademust">
								<tr>
									<td class="xslt_fieldHeading">
										Role Name :
									</td>
									<td class="xslt_fieldData" id="m_strRoleName">
										<xsl:value-of select="RoleData/m_strRoleName" />
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="trademust" style="width:100%">
							<table id="roleDetails_table_roleDetailsDG" class="easyui-datagrid"
								style="height:200px;" title="List Of Actions"
								data-options="striped:true, pagesize:10, rownumbers:true, fitColumns:true, showFooter:true, singleSelect:true">
							</table>
						</td>
					</tr>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>