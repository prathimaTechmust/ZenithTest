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
										<td class="xslt_fieldHeader" colspan="2">Client Group
											Details</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td class="trademust">
							<table class="trademust">
								<tr>
									<td class="trademust" style="vertical-align: top" colspan="2">
										<table class="trademust">
											<tr>
												<td class="xslt_fieldHeading" style="width:15%">Group Name :
												</td>
												<td class="xslt_fieldData" id="m_strGroupName" style="width:30%">
													<xsl:value-of select="ClientGroupData/m_strGroupName" />
												</td>
												<td class="xslt_fieldHeading" style="width:10%">Created By :
												</td>
												<td class="xslt_fieldData" id="m_strUserName" style="width:30%">
													<xsl:value-of select="ClientGroupData/m_strUserName" />
												</td>
												<td class="xslt_fieldHeading" style="width:5%">Date :</td>
												<td class="xslt_fieldData" id="m_strDate" style="width:10%">
													<xsl:value-of select="ClientGroupData/m_strDate" />
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td class="trademust" style="vertical-align: top; width:60%; height:250px">
										<div id="div_dataGrid">
											<table id="clientGroupDetails_table_clientGroupDetailsDG"
												class="easyui-datagrid" style="height:200px;" title="List of Client"
												data-options="striped:true, rownumbers:true, fitColumns:true">
											</table>
										</div>
									</td>
									<td class="trademust" style="vertical-align: top; width:40%; height:250px">
										<div id="div_dataGrid">
											<table id="clientGroupDetails_table_itemsGroupDetailsDG"
												class="easyui-datagrid" style="height:200px;" title="List of Items Groups"
												data-options="striped:true, rownumbers:true, fitColumns:true">
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