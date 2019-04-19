<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:variable name="m_nVendorId" select="VendorData/m_nVendorId" />
	<xsl:template match="/">
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="trademust">
							<div>
								<table class="trademust">
									<tr>
										<td class="xslt_fieldHeader" colspan="2">Vendor Group
											Details</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td valign="top">
							<table class="trademust">
								<tr>
									<td valign="top">
										<table class="trademust">
											<tr>
												<td class="xslt_fieldHeading">
													Group Name :
												</td>
												<td class="xslt_fieldData" id="m_strGroupName">
													<xsl:value-of select="VendorGroupData/m_strGroupName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Created By :
												</td>
												<td class="xslt_fieldData" id="m_strUserName">
													<xsl:value-of select="VendorGroupData/m_strUserName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Date :
												</td>
												<td class="xslt_fieldData" id="m_strDate">
													<xsl:value-of select="VendorGroupData/m_strDate" />
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td class="trademust" style="vertical-align:top;">
										<div id="div_dataGrid">
											<table id="vendorGroupDetails_table_vendorGroupDetailsDG"
												class="easyui-datagrid" style="height:200px;"
												title="List Of Vendors"
												data-options="striped:true, pagesize:10, rownumbers:true, showFooter:true, fitColumns:true, singleSelect:true">
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