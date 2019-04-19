<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
		  <head>
		    <style>
		    	#print_form_id
			    {
			    	border-style: none;
			    }
			  	table.main-print
				{
					margin-top:3%;
					margin-left:7%;
					margin-right:19%;
					padding:0;
					border: 1px solid black;
					width:580px;
				}
				td.title-print
				{
					padding:0;
					margin:0;
					text-align:center;
					font-family:sans-serif;
					font-weight: bold;
					font-size: 13px;
					line-height: normal;
					text-decoration:underline;
				}
				td.fieldData-print
				{
					font-family:sans-serif;
					font-weight: bold;
					font-size: 12px;
					line-height: normal;
				}
				td.fieldHeader-print
				{
					font-family:sans-serif;
					font-weight: normal;
					font-size: 11px;
					line-height: normal;
				}
				table.nil-spacing
				{
					padding:0;
					margin:0;
					width:100%;
					border-spacing: 0;
				}
				td.nil-spacing
				{
					padding:0;
					margin:0;
				}
				table.border-collapse
				{
					border-collapse: collapse;
				}
				td.border-all, td.border-bottom, td.border-right-bottom
				{
					border: 1px solid black;
				}
				td.border-no-right
				{
					border-right-style: none;
				}
				td.border-no-left
				{
					border-left-style: none;
				}
				td.border-bottom
				{	
					border-style: none;
					border-bottom-style: solid;
				}
				td.border-right-bottom
				{	
					border-style: none;
					border-right-style: solid;
					border-bottom-style: solid;
				}
				tr.blank-row
				{
				    height: 15px !important;
				}
				td.padding-left-right
				{
					padding-left:5px;
					padding-right:5px;
				}
		    </style>
		  </head>
		  <body>
			<table class="main-print border-collapse">
			  <tr>
			    <td class="title-print">
			      RECEIPT
			    </td>
			  </tr>
			  <tr>
			  	<td class="nil-spacing padding-left-right">
			  		<table class="nil-spacing">
			  			<tr>
			  				<td class="fieldHeader-print" style="width:60%;vertical-align:top;">
			  					Receipt Number&#160;:&#160;<b><xsl:value-of select="ReceiptData/m_strReceiptNumber"/></b>
			  				</td>
			  				<td class="fieldHeader-print" style="width:40%;vertical-align:top;text-align:right">
			  					Dated&#160;:&#160;<b><xsl:value-of select="ReceiptData/m_strDate"/></b>
			  				</td>
			  			</tr>
			  		</table>
			  	</td>
			  </tr>
			  <tr>
			  	<td class="nil-spacing padding-left-right" style="vertical-align:top;">
  					<table class="nil-spacing">
	  					<tr>
							<td class="fieldHeader-print">Received From</td>
						</tr>
						<tr>
							<td class="fieldData-print">
						 		<xsl:value-of select="ReceiptData/ClientData/m_strCompanyName"/><br/>
								<xsl:value-of select="ReceiptData/ClientData/m_strAddress" /><br/>
								<xsl:value-of select="ReceiptData/ClientData/m_strCity" /> - <xsl:value-of select="ReceiptData/ClientData/m_strPinCode" />
							</td>
						</tr>
  					</table>
  				</td>
			  </tr>
			  <tr>
			  	<td class="nil-spacing padding-left-right" style="vertical-align:top;">
  					<table class="nil-spacing">
	  					<tr>
							<td class="fieldHeader-print">Mode<br/>
								<b><xsl:value-of select="ReceiptData/TransactionMode/m_strModeName" /></b>
							</td>
						</tr>
						<tr>
							<td class="fieldHeader-print">Details<br/>
						 		<b><xsl:value-of select="ReceiptData/m_strDetails" /></b>
							</td>
						</tr>
  					</table>
  				</td>
			  </tr>
			  <tr>
				  <td class="nil-spacing" style="vertical-align:top">
				  	<table class="nil-spacing border-collapse">
						<tr>
							<td class="fieldHeader-print border-all border-no-left" style="width:5%;text-align:center">
								#
							</td>
							<td class="fieldHeader-print border-all" style="width:20%;text-align:center">
								Invoice No.
							</td>
							<td class="fieldHeader-print border-all" style="width:15%;text-align:center">
								Invoice Date
							</td>
							<td class="fieldHeader-print border-all" style="width:20%;text-align:center">
								Invoice Amount
							</td>
							<td class="fieldHeader-print border-all" style="width:20%;text-align:center">
								Receipt Amount
							</td>
							<td class="fieldHeader-print border-all border-no-right" style="width:20%;text-align:center">
								Balance Amount
							</td>
						</tr>
						<xsl:for-each
							select="ReceiptData/m_oInvoiceReceiptsSet/InvoiceReceiptData">
							<tr>
								<td class="fieldData-print border-right-bottom" style="text-align:center">
									<xsl:value-of select="position()" />
								</td>
								<td class="fieldData-print border-right-bottom">
									<xsl:value-of select="InvoiceData/m_strInvoiceNumber" />
								</td>
								<td class="fieldData-print border-right-bottom" style="text-align:center">
									<xsl:value-of select="InvoiceData/m_strDate" />
								</td>
								<td class="fieldData-print border-right-bottom" style="text-align:right">
									<xsl:value-of select="format-number(InvoiceData/m_nTotalAmount, '##,##,##,##0.00')" />
								</td>
								<td class="fieldData-print border-right-bottom" style="text-align:right">
									<xsl:value-of select="format-number(m_nAmount, '##,##,##,##0.00')" />
								</td>
								<td class="fieldData-print border-bottom" style="text-align:right">
									<xsl:value-of select="format-number(InvoiceData/m_nBalanceAmount, '##,##,##,##0.00')" />
								</td>
							</tr>
						</xsl:for-each>
				  	</table>
				  </td>
			  </tr>
			  <tr class="blank-row">
			  	<td></td>
			  </tr>
			  <tr>
		   	  	<td class="padding-left-right">
	   	  			<table class="nil-spacing">
			   	  		<tr>
			   	  			<td class="nil-spacing" style="width:65%;vertical-align:top;">
				               <table class="nil-spacing">
		   						<tr>
		   							<td class="fieldHeader-print">Amount(in words)&#160;</td>
		   						</tr>
		   						<tr>
		   							<td class="fieldData-print"><xsl:value-of select="ReceiptData/m_strReceiptAmountInWords"/></td>
		   						</tr>
				              </table>
				            </td>
				            <td class="nil-spacing" style="width:35%;vertical-align:top;text-align:right">
				              <table class="nil-spacing">
		   						<tr>
		   							<td class="fieldHeader-print">Amount Received</td>
		   						</tr>
		   						<tr>
		   							<td class="fieldData-print"><span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="ReceiptData/m_nAmount"/></td>
		   						</tr>
				              </table>
				            </td>
			   	  		</tr>
		   	  		</table>
		   	  	</td>
	          </tr>
			  <tr>
	            <td class="fieldHeader-print" style="text-align: right">
	          		<br/><br/><br/>
	          		Authorised Signatory
	          	</td>
	          </tr>
			</table>
		  </body>
		</html>
	</xsl:template>
</xsl:stylesheet>