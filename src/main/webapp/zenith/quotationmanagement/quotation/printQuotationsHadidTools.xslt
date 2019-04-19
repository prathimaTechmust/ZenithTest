<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
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
				border-spacing: 0;
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
			}
			td.fieldData-print
			{
				font-family:sans-serif;
				font-weight: bold;
				font-size: 11px;
				line-height: normal;
			}
			td.fieldHeader-print
			{
				font-family:sans-serif;
				font-weight: normal;
				font-size: 10px;
				line-height: normal;
			}
			td.footerTagLine
			{
				text-align:center;
				font-family:sans-serif;
				font-size: 10px;
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
			td.border-all, td.border-top, td.border-right, td.border-bottom, td.border-top-right, td.border-right-bottom
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
			td.border-no-bottom
			{
				border-bottom-style: none;
			}
			td.border-no-top
			{	
				border-top-style: none;
			}
			td.border-top
			{	
				border-style: none;
				border-top-style: solid;
			}
			td.border-right
			{	
				border-style: none;
				border-right-style: solid;
			}
			td.border-bottom
			{	
				border-style: none;
				border-bottom-style: solid;
			}
			td.border-top-right
			{	
				border-style: none;
				border-right-style: solid;
				border-top-style: solid;
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
		  </style>
		  <body>
		    <table class="main-print border-collapse">
		      <tr>
		        <td class="title-print">
		          Quotation
		        </td>
		      </tr>
		      <tr>
		        <td class="nil-spacing">
		          <table class="nil-spacing">
		            <tr>
		            	<td class="nil-spacing" style="width:50%">
		            		<table class="nil-spacing">
		            			<tr>
		            				<td class="nil-spacing border-all border-no-left" style="width:50%;vertical-align:top;height:32px">
		            					<table class="nil-spacing">
		            						<tr>
		            							<td class="fieldHeader-print">
		            								Delivery Note
		            							</td>
		            						</tr>
		            						<tr>
		            							<td class="fieldData-print">
		            							</td>
		            						</tr>
		            					</table>
		            				</td>
		            				<td class="nil-spacing border-all border-no-left" style="width:50%;vertical-align:top;"> 
		            					<table class="nil-spacing">
		            						<tr>
		            							<td class="fieldHeader-print">
		            								Dated
		            							</td>
		            						</tr>
		            						<tr>
		            							<td class="fieldData-print">
		            							</td>
		            						</tr>
		            					</table>
		            				</td>
		            			</tr>
		            		</table>
		            	</td>
		            	<td class="nil-spacing">
		            		<table class="nil-spacing">
		            			<tr>
		            				<td class="nil-spacing border-all border-no-left" style="width:50%;vertical-align:top;height:32px">
		            					<table class="nil-spacing">
		            						<tr>
		            							<td class="fieldHeader-print nil-spacing">Quotation No</td>
		            						</tr>
		            						<tr>
		            							<td class="fieldData-print"><xsl:value-of select="QuotationData/m_strQuotationNumber" /></td>
		            						</tr>
		            					</table>
		            				</td>
		            				<td class="nil-spacing border-all border-no-left border-no-right" style="width:50%;vertical-align:top;"> 
		            					<table class="nil-spacing">
		            						<tr>
		            							<td class="fieldHeader-print">
		            								Date
		            							</td>
		            						</tr>
		            						<tr>
		            							<td class="fieldData-print">
		            								<xsl:value-of select="QuotationData/m_strDate"/>
		            							</td>
		            						</tr>
		            					</table>
		            				</td>
		            			</tr>
		            		</table>
		            	</td>
		            </tr>
		            <tr>
		            	<td class="nil-spacing border-right" style="width:50%; vertical-align:top;height:95px">
		            		<table class="nil-spacing">
		            			<tr>
				                    <td class="fieldHeader-print">
				                      Site
				                    </td>
		                  		</tr>
		                  		<tr>
				                    <td class="fieldData-print">
				                    	<xsl:value-of select="QuotationData/SiteData/m_strSiteName" /><br/>
										<xsl:value-of select="QuotationData/SiteData/m_strSiteAddress" />
				                    </td>
		                  		</tr>
		            		</table>
		            	</td>
		            	<td class="nil-spacing">
		            		<table class="nil-spacing">
		            			<tr>
				                    <td class="nil-spacing border-bottom" style="vertical-align:top;height:65px">
				                      <table class="nil-spacing">
						    			<tr>
						                    <td class="fieldHeader-print" style="vertical-align:top">
						                      To
						                    </td>
						          		</tr>
						          		<tr>
						                    <td class="fieldData-print" style="vertical-align:top">
						                    	<xsl:value-of select="QuotationData/ClientData/m_strCompanyName" /><br/>
												<xsl:value-of select="QuotationData/ClientData/m_strAddress" /><br/>
												<xsl:value-of select="QuotationData/ClientData/m_strCity" /> - <xsl:value-of select="QuotationData/m_oClientData/ClientData/m_strPinCode" />
						                    </td>
						          		</tr>
		        					  </table>
				                    </td>
		                  		</tr>
		                  		<tr>
		   		                    <td class="nil-spacing"> 
				                      <table class="nil-spacing">
				                        <tr>
				                          <td class="nil-spacing border-right" style="width:50%;vertical-align:top;height:32px">
				                          	<table class="nil-spacing">
			            						<tr>
			            							<td class="fieldHeader-print">
			            								VAT TIN
			            							</td>
			            						</tr>
			            						<tr>
			            							<td class="fieldData-print">
			            								<xsl:value-of select="QuotationData/ClientData/m_strTinNumber" />
			            							</td>
			            						</tr>
		            						</table>
				                          </td>
				                          <td class="nil-spacing" style="width:50%;vertical-align:top;">
				                          	<table class="nil-spacing">
			            						<tr>
			            							<td class="fieldHeader-print">
			            								CST No.
			            							</td>
			            						</tr>
			            						<tr>
			            							<td class="fieldData-print">
			            								<xsl:value-of select="QuotationData/ClientData/m_strCSTNumber" />
			            							</td>
			            						</tr>
		            						</table>
				                          </td>
				                        </tr>
				                      </table>
				                    </td>
			                    </tr>
		            		</table>
		            	</td>
		            </tr>
		          </table>
		        </td>
		      </tr>
		      <tr>
		        <td class="nil-spacing" style="height:690px;vertical-align:top">
		          <table class="nil-spacing border-collapse">
		            <tr>
		              <td class="fieldHeader-print border-all border-no-left" style="width:5%;text-align:center;">
		                #
		              </td>
		              <td class="fieldHeader-print border-all" style="width:37%;text-align:center;">
		                Description of Goods
		              </td>
		              <td class="fieldHeader-print border-all" style="width:6%;text-align:center;">
		                Tax %
		              </td>
		              <td class="fieldHeader-print border-all" style="width:10%;text-align:center;">
		                Quantity
		              </td>
		              <td class="fieldHeader-print border-all" style="width:10%;text-align:center;">
		                Rate
		              </td>
		              <td class="fieldHeader-print border-all" style="width:5%;text-align:center;">
		                per
		              </td>
		              <td class="fieldHeader-print border-all" style="width:9%;text-align:center;">
		                Disc %
		              </td>
		              <td class="fieldHeader-print border-all border-no-right" style="width:13%;text-align:center">
		                Amount
		              </td>
		            </tr>
		            <xsl:for-each select="QuotationData">
						  <xsl:for-each select="m_oQuotationLineItems/LineItemData">
							<tr>
								<td class="fieldData-print border-right-bottom" style="text-align:center">
									<xsl:value-of select="SerialNumber"/>
								</td>
								<td class="fieldData-print border-right-bottom">
									<xsl:value-of select="QuotationLineItemData/m_strArticleDescription" /> &#160; <xsl:value-of select="m_strDetail" />
								</td>
								<td class="fieldData-print border-right-bottom" style="text-align:right">
									<xsl:value-of select="format-number(QuotationLineItemData/m_nTax, '##,##,##,##0.00')" />
								</td>
								<td class="fieldData-print border-right-bottom" style="text-align:right">
									<xsl:value-of select="format-number(QuotationLineItemData/m_nQuantity, '##,##,##,##0.00')" />&#160;<xsl:value-of select="SalesLineItemData/m_strUnit" />
								</td>
								<td class="fieldData-print border-right-bottom" style="text-align:right">
									<xsl:value-of select="format-number(QuotationLineItemData/m_nPrice, '##,##,##,##0.00')" />
								</td>
								<td class="fieldData-print border-right-bottom" style="text-align:center">
									<xsl:value-of select="QuotationLineItemData/m_strUnit" />
								</td>
								<td class="fieldData-print border-right-bottom" style="text-align:right">
									<xsl:value-of select="format-number(QuotationLineItemData/m_nDiscount, '##,##,##,##0.00')" />
								</td>
								<td class="fieldData-print border-bottom" style="text-align:right">
									<xsl:value-of select="format-number(QuotationLineItemData/m_nQuantity * (QuotationLineItemData/m_nPrice - (QuotationLineItemData/m_nPrice * QuotationLineItemData/m_nDiscount div 100)), '##,##,##,##0.00')"/>
								</td>
							</tr>
						</xsl:for-each> 
					</xsl:for-each> 
					<tr class="blank-row">
		              	<td class="fieldData-print border-right-bottom" style="width:5%;">
						</td>
						<td class="fieldData-print border-right-bottom" style="width:37%;">
						</td>
						<td class="fieldData-print border-right-bottom" style="width:6%;">
						</td>
						<td class="fieldData-print border-right-bottom" style="width:10%;">
						</td>
						<td class="fieldData-print border-right-bottom" style="width:10%;">
						</td>
						<td class="fieldData-print border-right-bottom" style="width:5%;">
						</td>
						<td class="fieldData-print border-right-bottom" style="width:9%;">
						</td>
						<td class="fieldData-print border-bottom" style="width:13%;">
						</td>
		            </tr>
		  			<xsl:for-each select="QuotationData/m_oTaxes/Tax">
						<tr>
			              	<td class="fieldData-print border-right-bottom" style="width:5%;text-align:center;">
							</td>
							<td class="fieldData-print border-right-bottom" style="width:37%;text-align:right;">
								<xsl:value-of select="m_strTaxName" />
							</td>
							<td class="fieldData-print border-right-bottom" style="width:6%;">
							</td>
							<td class="fieldData-print border-right-bottom" style="width:10%;">
							</td>
							<td class="fieldData-print border-right-bottom" style="width:10%;text-align:right;">
								<xsl:value-of select="format-number(m_nTaxPercent, '##,##,##,###.00')"/>
							</td>
							<td class="fieldData-print border-right-bottom" style="width:5%;text-align:left;">
								%
							</td>
							<td class="fieldData-print border-right-bottom" style="width:9%;">
							</td>
							<td class="fieldData-print border-bottom" style="width:13%;text-align:right">
								<xsl:value-of select="format-number(m_nTaxAmount, '##,##,##,##0.00')" />
							</td>
			            </tr>
					</xsl:for-each> 
		            <tr>
		              	<td class="fieldData-print border-right-bottom" style="width:5%;text-align:center;">
						</td>
						<td class="fieldData-print border-right-bottom" style="width:37%;text-align:right;">
							Round Off
						</td>
						<td class="fieldData-print border-right-bottom" style="width:6%;">
						</td>
						<td class="fieldData-print border-right-bottom" style="width:10%;">
						</td>
						<td class="fieldData-print border-right-bottom" style="width:10%;text-align:right;">
						</td>
						<td class="fieldData-print border-right-bottom" style="width:5%;text-align:left;">
						</td>
						<td class="fieldData-print border-right-bottom" style="width:9%;">
						</td>
						<td class="fieldData-print border-bottom" style="width:13%;text-align:right">
							<xsl:value-of select="format-number((round(QuotationData/grandTotal) - QuotationData/grandTotal) ,'##,##,##,##0.00')"/>
						</td>
		            </tr>
		          </table>
		        </td>
		        <tr>
		        	<td class="nil-spacing">
		        		<table class="nil-spacing">
		        			<tr>
				              <td class="fieldHeader-print border-all border-no-left" style="width:5%;text-align:center;">
				              </td>
				              <td class="fieldHeader-print border-all border-no-left" style="width:37%;text-align:right;">
				              	Total
				              </td>
				              <td class="fieldHeader-print border-all border-no-left" style="width:6%;text-align:center;">
				              </td>
				              <td class="fieldData-print border-all border-no-left" style="width:10%;text-align:right;">
				              </td>
				              <td class="fieldHeader-print border-all border-no-left" style="width:10%;text-align:center;">
				              </td>
				              <td class="fieldHeader-print border-all border-no-left" style="width:5%;text-align:center;">
				              </td>
				              <td class="fieldHeader-print border-all border-no-left" style="width:9%;text-align:center;">
				              </td>
				              <td class="fieldData-print border-all border-no-left border-no-right" style="width:13%;text-align:right">
				              	<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(round(QuotationData/grandTotal),'##,##,##,###.00')"/>
		              		  </td>
				            </tr>
		        		</table>
		        	</td>
				</tr>
		      </tr>
		      <tr>
		        <td class="nil-spacing">
		          <table class="nil-spacing border-collapse">
		            <tr>
		              <td class="nil-spacing">
		                <table class="nil-spacing">
		            	 <tr>
		                    <td class="nil-spacing" style="width:50%;vertical-align:top;height:85px">
		                      <table class="nil-spacing">
		                        <tr>
		                          <td class="fieldHeader-print">Amount(in words)</td>
		                        </tr>
		                        <tr>
		                          <td class="fieldData-print"><xsl:value-of select="QuotationData/m_nQuotationAmountInWords" /></td>
		                        </tr>
		                        <tr>
		                          <td class="fieldHeader-print">Tax Amount(in words)</td>
		                        </tr>
		                        <tr>
		                          <td class="fieldData-print"><xsl:value-of select="QuotationData/m_oTaxes/totalTaxAmountInWords" /></td>
		                        </tr>
		                      </table>
		                    </td>
		                    <td class="nil-spacing" style="width:50%;vertical-align:top">
		                    	<table class="nil-spacing">
			                  		<tr>
										<td class="fieldHeader-print" colspan="3">Company's Bank Details</td>
									</tr>
									<tr>
										<td class="fieldHeader-print" style="width:35%;">Bank Name</td>
										<td class="fieldHeader-print" style="width:5%;">:</td>
										<td class="fieldData-print" style="width:60%;font-weight:bold">Bank of Maharashtra</td>
									</tr>
									<tr>
										<td class="fieldHeader-print" style="width:35%;">A/c No.</td>
										<td class="fieldHeader-print" style="width:5%;">:</td>
										<td class="fieldData-print" style="width:60%;font-weight:bold">20061600441</td>
									</tr>
									<tr>
										<td class="fieldHeader-print" style="width:35%;">Branch &amp; IFS Code</td>
										<td class="fieldHeader-print" style="width:5%;">:</td>
										<td class="fieldData-print" style="width:60%;font-weight:bold">CITY MARKET &amp; MAHB0000304</td>
									</tr>
								</table>
		                    </td>
		                  </tr>
		                </table>
		              </td>
		            </tr>
		            <tr>
		              <td class="footerTagLine border-top nil-spacing ">
		              	SUBJECT TO BANGALORE JURISDICTION <br/>
		              	This is a computer Generated Quotation
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