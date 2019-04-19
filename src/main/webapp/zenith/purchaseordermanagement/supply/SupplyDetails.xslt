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
										<td class="xslt_fieldHeader" colspan="2">Supply
											Details</td>
										<td class="trademust" align="right" colspan="2">
											<table>
												<tr>
													<td>
														
													</td>
													<td>
														
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td class="trademust">
							<table class="trademust">
								<tr>
									<td class="trademust" style="vertical-align:top;">
										<table class="trademust">
											<tr>
												<td class="xslt_fieldHeading">From :</td>
												<td class="xslt_fieldData" id="m_strFrom">
													<xsl:value-of select="PurchaseData/m_strFrom" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Invoice Number :</td>
												<td class="xslt_fieldData" id="m_strInvoiceNo">
													<xsl:value-of select="PurchaseData/m_strInvoiceNo" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Date :</td>
												<td class="xslt_fieldData" id="m_strDate">
													<xsl:value-of select="PurchaseData/m_strDate" />
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td class="trademust" align="right" style="vertical-align:top;">
										<div id="div_dataGrid">
										  	<table id="supplyDetails_table_supplyDetailsDG"
												class="easyui-datagrid" style="height:200px;" title="List Of Articles" 
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