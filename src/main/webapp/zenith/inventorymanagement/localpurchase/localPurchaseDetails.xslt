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
										<td class="xslt_fieldHeader" colspan="2">Local Purchase Details</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td class="trademust">
							<table class="trademust">
								<tr>
									<td class="trademust" style="width:100%;vertical-align:top;">
										<table class="trademust">
											<tr>
												<td class="xslt_fieldHeading">From :</td>
												<td class="xslt_fieldData" id="m_strCompanyName">
													<xsl:value-of
														select="PurchaseOrderData/ClientData/m_strCompanyName" />
												</td>
												<td class="xslt_fieldHeading">Site :</td>
												<td class="xslt_fieldData" id="m_strSiteName">
													<xsl:value-of select="PurchaseOrderData/SiteData/m_strSiteName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">PO Number :</td>
												<td class="xslt_fieldData" id="m_strPurchaseOrderNumber">
													<xsl:value-of select="PurchaseOrderData/m_strPurchaseOrderNumber" />
												</td>
												<td class="xslt_fieldHeading">Date :</td>
												<td class="xslt_fieldData" id="m_strPurchaseOrderDate">
													<xsl:value-of select="PurchaseOrderData/m_dPurchaseOrderDate" />
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td class="trademust" align="left" style="vertical-align:top;width:99%">
										<div id="div_dataGrid">
											<table id="localPurchaseDetails_table_PODetailsDG"
												class="easyui-datagrid" style="height:300px;" title="Line Items"
												data-options="striped:true, pagesize:10, rownumbers:true, fitColumns:true, showFooter:true, singleSelect:true">
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