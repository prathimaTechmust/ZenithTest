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
										<td class="xslt_fieldHeader" colspan="2">Invoice
											Details
										</td>
										<td class="trademust" align="right" colspan="2">
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
									<td class="trademust" style="width:50%;vertical-align:top;">
										<table class="trademust">
											<tr>
												<td class="xslt_fieldHeading">To :</td>
												<td class="xslt_fieldData" id="m_strTo">
													<xsl:value-of select="InvoiceData/m_oClientData/ClientData/m_strCompanyName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Invoice Number :</td>
												<td class="xslt_fieldData" id="m_strInvoiceNo">
													<xsl:value-of select="InvoiceData/m_strInvoiceNumber" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Date :</td>
												<td class="xslt_fieldData" id="m_strDate">
													<xsl:value-of select="InvoiceData/m_strDate" />
												</td>
											</tr>
										</table>
									</td>
									<td width="50%" valign="top">
										<table width="100%" cellspacing="5px">
											<tr>
												<td class="xslt_fieldHeading" width="120px">
													Invoice Amount :
												</td>
												<td class="xslt_fieldData" id="nTotalBusiness">
													<span class="rupeeSign">R  </span><span id="invoiceDetails_span_InvoiceAmount"></span>
														<xsl:value-of select="InvoiceData/m_nTotalAmount" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Received Amount :
												</td>
												<td class="xslt_fieldData" id="nTotalReceived">
													<span class="rupeeSign">R  </span><span id="invoiceDetails_span_ReceivedAmount"></span>
													<xsl:value-of select="InvoiceData/m_nReceiptAmount" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Outstanding :
												</td>
												<td class="xslt_fieldData" id="nOutstandingAmount">
											           <span class="rupeeSign">R  </span><span id="invoiceDetails_span_OutstandingAmount"></span>
											           <xsl:value-of select="InvoiceData/m_nBalanceAmount" />
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td class="trademust" align="left" style="vertical-align:top;" colspan="2">
										<div id="div_dataGrid">
											<table id="invoicedetails_table_salesDetailsDG" class="easyui-datagrid"
												style="height:200px;" title="List Of Items"
												data-options="striped:true, pagesize:10, rownumbers:true, fitColumns:true, showFooter:true, singleSelect:true">
											</table>
										</div>
									</td>
								</tr>
								<tr>
									<td class="trademust" align="left" style="vertical-align:top;" colspan="2">
										<div id="div_dataGrid">
											<table id="invoicedetails_table_recepitDetailsDG" class="easyui-datagrid"
												style="height:200px;" title="List Of Recepits"
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