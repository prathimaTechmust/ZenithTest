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
										<td class="xslt_fieldHeader" colspan="2">Payment Details</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td class="trademust">
							<table class="trademust">
								<tr>
									<td class="trademust" style="width:30%;vertical-align: top">
										<table class="trademust">
											<tr>
												<td class="xslt_fieldHeading">Payment Number :</td>
												<td class="xslt_fieldData" id="m_strPaymentNumber">
													<xsl:value-of select="PaymentData/m_strPaymentNumber" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Vendor Name :</td>
												<td class="xslt_fieldData" id="m_strCompanyName">
													<xsl:value-of select="PaymentData/VendorData/m_strVendorCompanyName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Mode :</td>
												<td class="xslt_fieldData" id="m_strModeName">
													<xsl:value-of select="PaymentData/TransactionMode/m_strModeName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Date :</td>
												<td class="xslt_fieldData" id="m_strDate">
													<xsl:value-of select="PaymentData/m_strDate" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Amount :</td>
												<td class="xslt_fieldData" id="m_nAmount">
													<span class="rupeeSign">R </span>
													<xsl:value-of select="PaymentData/m_nAmount" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Details :</td>
												<td class="xslt_fieldData" id="m_strDetails">
													<xsl:value-of select="PaymentData/m_strDetails" />
												</td>
											</tr>
										</table>
									</td>
									<td class="trademust" style="vertical-align: top; width:55%;">
										<div id="div_dataGrid">
											<table id="paymentListDetails_table_paymentDetailsDG" class="easyui-datagrid"
												style="height:200px;" title="List of Purchases"
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