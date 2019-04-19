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
										<td class="xslt_fieldHeader" colspan="2">Purchase
											Details</td>
											<td class="trademust" align="right" colspan="2">
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td width="50%" valign="top">
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
						<td width="50%" valign="top">
							<table width="100%" cellspacing="5px">
								<tr>
									<td class="xslt_fieldHeading" width="120px">
										Invoice Amount :
									</td>
									<td class="xslt_fieldData" id="nTotalInvoiceAmount">
										<span class="rupeeSign">R  </span>
										<xsl:value-of select="PurchaseData/m_nRoundedTotalAmount" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Payment Amount :
									</td>
									<td class="xslt_fieldData" id="nTotalReceived">
										<span class="rupeeSign">R  </span>
										<xsl:value-of select="PurchaseData/m_nPaymentAmount" />
									</td>
								</tr>
								<tr>
									<td class="xslt_fieldHeading">
										Outstanding :
									</td>
									<td class="xslt_fieldData" id="nOutstandingAmount">
									<xsl:variable name="nOutstandingAmount" select="PurchaseData/m_nBalanceAmount" />
										<xsl:choose>
								          <xsl:when test="$nOutstandingAmount &gt; 0">
								           <span class="rupeeSign">R  </span><span style="color:red"><xsl:value-of select="format-number($nOutstandingAmount,'##,##,##,##0.00')" /></span>
								          </xsl:when>
								          <xsl:otherwise>
								            <span class="rupeeSign">R  </span><span style="color:green"><xsl:value-of select="format-number($nOutstandingAmount,'##,##,##,##0.00')" /></span>
								          </xsl:otherwise>
								        </xsl:choose>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="trademust" align="right" style="vertical-align:top;" colspan="2">
							<div id="div_dataGrid">
								<table id="purchaseDetails_table_purchaseDetailsDG" class="easyui-datagrid"
									style="height:250px;" title="List Of Articles"
									data-options="striped:true, pagesize:10, rownumbers:true, showFooter:true, fitColumns:true, singleSelect:true">
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td class="trademust" align="right" style="vertical-align:top;" colspan="2"> 
							<div id="div_dataGrid">
								<table id="purchaseDetails_table_paymentDetailsDG" class="easyui-datagrid"
									style="height:250px;" title="List Of Payments"
									data-options="striped:true, pagesize:10, rownumbers:true, showFooter:true, fitColumns:true, singleSelect:true">
								</table>
							</div>
						</td>
					</tr>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>