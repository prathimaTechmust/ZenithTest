<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<xsl:variable name="subTotal"
			select="sum(ReceiptData/m_oInvoiceReceiptsSet/InvoiceReceiptData/InvoiceData/m_nBalanceAmount)" />
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="trademust">
							<div>
								<table class="trademust">
									<tr>
										<td class="xslt_formHeading">Receipt
											Details
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
									<td class="trademust" style="width:100%;vertical-align:top;">
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
													<span class="rupeeSign" style="font-weight: normal;">R </span>
													<xsl:value-of select="ReceiptData/m_nAmount" />
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
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="trademust" colspan="2">
							<table class="print_tableBorder trademust">
								<tr>
									<td class="trademust print_tdBorder" colspan="2">
										<div style="min-height:450px">
											<table class="trademust">
												<tr>
													<td class="trademust" style="vertical-align:top;height:420px">
														<table class="print_tableBorder trademust">
															<tr>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:5%">
																	#
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:20%">
																	INV#
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:15%">
																	Date
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:20%;text-align:right">
																	Invoice Amount
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:20%;text-align:right">
																	Receipt Amount
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="width:20%;text-align:right">
																	Balance Amount
																</td>
															</tr>
															<xsl:for-each
																select="ReceiptData/m_oInvoiceReceiptsSet/InvoiceReceiptData">
																<tr>
																	<td class="print_fieldTableData print_tdBorder"
																		style="text-align:center">
																		<xsl:value-of select="position()" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder">
																		<xsl:value-of select="InvoiceData/m_strInvoiceNumber" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder">
																		<xsl:value-of select="InvoiceData/m_strDate" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder"
																		style="text-align:right">
																		<span class="rupeeSign" style="font-weight: normal;">R </span>
																		<xsl:value-of
																			select="format-number(InvoiceData/m_nTotalAmount, '##,##,##,##0.00')" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder"
																		style="text-align:right">
																		<span class="rupeeSign" style="font-weight: normal;">R </span>
																		<xsl:value-of
																			select="format-number(InvoiceData/m_nReceiptAmount, '##,##,##,##0.00')" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder"
																		style="text-align:right">
																		<span class="rupeeSign" style="font-weight: normal;">R </span>
																		<xsl:value-of
																			select="format-number(InvoiceData/m_nBalanceAmount, '##,##,##,##0.00')" />
																	</td>
																</tr>
															</xsl:for-each>
														</table>
													</td>
												</tr>
												<tr>
													<td class="trademust" style="vertical-align:bottom">
														<table class="print_tableBorder trademust">
															<tr>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="text-align:right; width:80%">
																	Total Balance
																</td>
																<td class="print_fieldTableHeading print_tdBorder"
																	style="text-align:right">
																	<span class="rupeeSign" style="font-weight: normal;">R </span>
																	<xsl:value-of
																		select="format-number($subTotal,'##,##,##,##0.00')" />
																</td>
															</tr>
														</table>
													</td>
												</tr>
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