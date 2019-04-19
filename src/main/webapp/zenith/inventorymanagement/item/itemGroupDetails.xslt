<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="trademust">
							<div>
								<table class="trademust">
									<tr>
										<td class="xslt_fieldHeader" colspan="2">Item Group Details</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td class="trademust">
							<table class="trademust">
								<tr>
									<td class="trademust" style="width:30% ;vertical-align: top;" >
										<table class="trademust">
											<tr>
												<td class="xslt_fieldHeading">Group Name :</td>
												<td class="xslt_fieldData" id="m_strGroupName">
													<xsl:value-of select="GroupData/m_strGroupName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Created By :</td>
												<td class="xslt_fieldData" id="m_strUserName">
													<xsl:value-of select="GroupData/m_strUserName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Date :</td>
												<td class="xslt_fieldData" id="m_strDate">
													<xsl:value-of select="GroupData/m_strDate" />
												</td>
											</tr>
										</table>
									</td>
									<td class="trademust" style="vertical-align: top; width:55%;">
										<div id="div_dataGrid">
											<table id="ItemGroupListDetails_table_itemDetailsDG" class="easyui-datagrid"
												style="height:200px;" title="List of Group Items"
												data-options="striped:true, rownumbers:true, fitColumns:true, showFooter:true,singleSelect:true">
											</table>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>