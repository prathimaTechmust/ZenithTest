<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="print_formHeading" style="text-decoration:underline;" colspan="2">
							STOCK TRANSFER MEMO
          				</td>
					</tr>
					<tr>
						<td style="width:70%; text-align:left">
							<img src="images/defaultLogo.PNG" title="Logo"/>
						</td>
						<td class="trademust" style="line-height:30%; width:30%;">
							<p>Phone:##########</p>
							<p>Fax:########</p>
							<p>E-mail:default@default.com</p>
							<p>Website:www.default.com</p>
							<p>Address</p>
							<p>City-pincode</p>
						</td>
					</tr>
					<tr>
						<td class="trademust" style="vertical-align: top" colspan="2">
							<table class="trademust print_tableBorder">
								<tr>
									<td class ="print_tdBorder" style="width:40%; vertical-align: top;" >
										<b>From: </b>
										<xsl:value-of select="StockTransferMemoData/TransferredFrom/LocationData/m_strName" /><br/>
										<xsl:value-of select="StockTransferMemoData/TransferredFrom/LocationData/m_strAddress" /><br/>
									</td>
									<td class ="print_tdBorder" style="width:40%; vertical-align: top;" >
										<b>To: </b>
										<xsl:value-of select="StockTransferMemoData/TransferredTo/LocationData/m_strName" /><br/>
										<xsl:value-of select="StockTransferMemoData/TransferredTo/LocationData/m_strAddress" /><br/>
									</td>
									<td class ="print_tdBorder" style="width:20%; padding-left:0px; padding-right:0px">
										<table class="trademust print_tableBorderBottom">
											<tr>
												<td class="print_fieldData" style="text-align:left" id="m_strDateTime">
													<table  class="trademust">
														<tr>
															<td style="width:40%;">Date<br/>Time</td>
															<td style="width:5%;">:<br/>:</td>
															<td style="width:55%;"><xsl:value-of select="StockTransferMemoData/m_strDate"/><br/><xsl:value-of select="StockTransferMemoData/m_strTime"/></td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td colspan="3">
							          	<div style="min-height:880px">
											<table class="print_tableBorder trademust">
												<tr>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:5%, text-align:center">
														#
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:15%">
														Article #
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="30%">
														Name
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="15%">
														Brand
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:25%;">
														Details
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:10%">
														Qty
													</td>
												</tr>
												<xsl:for-each select="StockTransferMemoData/m_oStockTransferSet/StockTransferData">
													<tr>
														<td class="print_fieldTableData print_tdBorder" style="text-align:center">
															<xsl:value-of select="position()"/>
														</td>
														<td class="print_fieldTableData print_tdBorder">
															<xsl:value-of select="ItemData/m_strArticleNumber" />
														</td>
														<td class="print_fieldTableData print_tdBorder">
															<xsl:value-of select="ItemData/m_strItemName" />
														</td>
														<td class="print_fieldTableData print_tdBorder">
															<xsl:value-of select="ItemData/m_strBrand" />
														</td>
														<td class="print_fieldTableData print_tdBorder">
															<xsl:value-of select="ItemData/m_strDetail" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<xsl:value-of select="format-number(m_nQuantity, '##,##,##,##0.00')" />
														</td>
													</tr>
												</xsl:for-each> 
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