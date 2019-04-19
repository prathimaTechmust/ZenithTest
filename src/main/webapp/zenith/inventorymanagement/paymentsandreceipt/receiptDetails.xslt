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
										<td class="xslt_fieldHeader" colspan="2">Receipt Details</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td class="trademust">
							<table class="trademust">
								<tr>
									<td class="trademust" style="width:30%;vertical-align:top;">
										<table class="trademust">
											<tr>
												<td class="xslt_fieldHeading">Receipt Number :</td>
												<td class="xslt_fieldData" id="m_strReceiptNumber">
													<xsl:value-of select="ReceiptData/m_strReceiptNumber" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Client Name :</td>
												<td class="xslt_fieldData" id="m_strCompanyName">
													<xsl:value-of select="ReceiptData/ClientData/m_strCompanyName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Mode :</td>
												<td class="xslt_fieldData" id="m_strModeName">
													<xsl:value-of select="ReceiptData/TransactionMode/m_strModeName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Date :</td>
												<td class="xslt_fieldData" id="m_strDate">
													<xsl:value-of select="ReceiptData/m_strDate" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Amount :</td>
												<td class="xslt_fieldData" id="m_nAmount">
													<span class="rupeeSign" style="font-weight: normal;">R </span> <xsl:value-of select="ReceiptData/m_nAmount" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Details :</td>
												<td class="xslt_fieldData" id="m_strDetails">
													<xsl:value-of select="ReceiptData/m_strDetails" />
												</td>
											</tr>
										</table>
									</td>
									<td class="trademust" style="vertical-align: top; width:55%;">
										<div id="div_dataGrid">
											<table id="receiptListDetails_table_receiptDetailsDG" class="easyui-datagrid"
												style="height:200px;" title="List of Invoices"
												data-options="striped:true, pagesize:10, rownumbers:true, fitColumns:true, showFooter:true,singleSelect:true">
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