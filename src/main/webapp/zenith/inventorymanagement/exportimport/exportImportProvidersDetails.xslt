<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="xslt_fieldHeader" colspan="2">Export Import
							Providers Details</td>
					</tr>
					<tr>
						<td class="trademust" style="width:50%; vertical-align:top" >
							<table class="trademust">
								<tr>
									<td class="xslt_fieldHeading">Provider Name :</td>
									<td class="xslt_fieldData">
										<xsl:value-of select="ExportImportProvider/m_strProviderName" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">Description :</td>
									<td class="xslt_fieldData">
										<xsl:value-of select="ExportImportProvider/m_strDescription" />
									</td>
								</tr>
							</table>
						</td>
						<td class="trademust"  style="width:50%">
							<table id="exportImportProviderDetails_table_classesDG"
								class="easyui-datagrid" style="height:200px;" title="DataExchange classes Details"
								data-options="striped:true, pagesize:10, rownumbers:true, fitColumns:true, showFooter:true, singleSelect:true">
							</table>
						</td>
					</tr>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>