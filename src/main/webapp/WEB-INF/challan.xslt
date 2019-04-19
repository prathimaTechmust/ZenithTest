<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<xsl:variable name="subTotal"
			select="sum(ChallanData/m_oSalesData/SalesData/m_oSalesLineItems/SalesLineItemData/m_nAmount)" />
		<xsl:variable name="subTaxTotal"
			select="sum(ChallanData/m_oSalesData/SalesData/m_oTaxes/Tax/m_nTaxAmount)" />
		<html>
		<head>
				<style>
					table.traderp
					{
						width: 100%;
					}
					td.traderp
					{
						font-family: sans-serif;
						font-weight: lighter;
						font-size: small;
						line-height: 150%;
						vertical-align: middle;
					}
					table.print_tableBorderBottom
					{
						border-collapse:collapse;
					}
					td.print_tdBorderBottom
					{
						border-bottom:1px solid black;
					}	
					td.print_footerData
					{
						font-family: sans-serif;
						font-size: small;
						font-weight:normal;
						vertical-align: middle;
						text-align: middle;
						color: black;
						line-height: 120%;
					}
					td.print_formHeading
					{
						padding: 4px;
						color: black;
						font-weight:bold;
						font-size : 12pt;
						font-family: sans-serif;
						line-height: 200%;
						vertical-align: middle;
						text-align: center;
					}
					table.print_tableBorder
					{
						border-collapse:collapse;
						border:1px solid black;
					}
					td.print_tdBorder
					{
						border:1px solid black;
						padding-right: 5px;
						padding-left: 5px;
					}
					td.print_fieldData
					{
						font-family: sans-serif;
						font-size: normal;
						font-weight:bold;
						vertical-align: middle;
						text-align: left;
						color: black;
						line-height: 150%;
					}
					td.print_fieldTableHeading
					{
						font-family: sans-serif;
						font-size: normal;
						font-weight:bold;
						vertical-align: middle;
						text-align: center;
						color: black;
						line-height: 150%;
					}
					td.print_fieldTableData
					{
						font-family: sans-serif;
						font-size: normal;
						font-weight:normal;
						vertical-align: middle;
						text-align: middle;
						color: black;
						line-height: 150%;
					}
					p { margin: 0 !important; }
					@font-face
					{
					    font-family: 'rupee';
					    src: url('rupee_foradian-1-webfont.eot');
					    src: local('Ã¢ËœÂº'), url(data:font/truetype;charset=utf-8;base64,AAEAAAANAIAAAwBQRkZUTVen5G0AAADcAAAAHEdERUYAQAAEAAAA+AAAACBPUy8yRQixzQAAARgAAABgY21hcGmyCE0AAAF4AAABamdhc3D//wADAAAC5AAAAAhnbHlmmuFTtAAAAuwAABAoaGVhZPOmAG0AABMUAAAANmhoZWELSAQOAAATTAAAACRobXR4KSwAAAAAE3AAAABMbG9jYUCgSLQAABO8AAAAKG1heHAAFQP+AAAT5AAAACBuYW1lWObwcQAAFAQAAAIDcG9zdCuGzNQAABYIAAAAuAAAAAEAAAAAxtQumQAAAADIadrpAAAAAMhp2uoAAQAAAA4AAAAYAAAAAAACAAEAAQASAAEABAAAAAIAAAADAigBkAAFAAgFmgUzAAABGwWaBTMAAAPRAGYCEgAAAgAFAAAAAAAAAIAAAKdQAABKAAAAAAAAAABITCAgAEAAICBfBZr+ZgDNBrQBoiAAARFBAAAAAAAFnAAAACAAAQAAAAMAAAADAAAAHAABAAAAAABkAAMAAQAAABwABABIAAAADgAIAAIABgAgAFIAoCAKIC8gX///AAAAIABSAKAgACAvIF/////j/7L/ZeAG3+LfswABAAAAAAAAAAAAAAAAAAAAAAEGAAABAAAAAAAAAAECAAAAAgAAAAAAAAAAAAAAAAAAAAEAAAMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB//8AAgABAAAAAAO0BZwD/QAAATMVMzUhFTMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVIxUjNSMVIzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNSE1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1ITUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNSECTBAYATwEBAQEBAQEBAQEBAQEBAQEBAQEBAQQ2AQEBAQEBAQEBAQEBAQEBAT0BAQEBAQEBAQEBAQEBAQEBAQEBAQECJwEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAgEBAQECAQECAQIBAgECAgECAwICAgMCAwMEAwQFBAcHBAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBIAcMAwEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEsCAcEBAMDAwICAgICAgECAQIBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAT9/AQEBAQEBAQEBAQEBAQEBAQEBAQECAGYBAQEBAQEBAQEBAQEBAgECAQIBAwICAwIEBAYFCjwBAQEBAQEBAQEBAQEBAQEBAQEBAQECAH0BZwEBAQIBAgIBAgECAQIBAgIBAgECAQIBAQEDAgECAQIBAgECAwICAwQEAQEBAgECAQICAQIBAgECAgECAQICAQEBAgQEBAMDAgIDAQICAQICAQEBAgEBAgEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBBAECAQEBAgEBAgEBAQECAQECAQEBAgEBAQIBAQIBAQEBAQIBAgEBAgEBAQIBAQECAQEBAgEBAQIBAQEBAQIBAQIBAQECAQEBAgEBAgEBAQEBAgEBAgEBAgEBAQEBAgEBAgEBAQECAQECAQEBAgEBAQECAQECAQECAQEBAQIBAQEBAgEBAQEBAQECAQEBAQIBAQECAQEBAgEBAQIBAQECAQECAQEBAgECAQEBAgEBAQIBAQIBAQEBAgEBAgEBAgEBAQECAQECAQEBAQIBAQECAQECAQEBAQIBAQIBAQEBAQIBAQECAQEBAgEBAgEBAQEBAQIBAQECAQEBAQIBAQECAQEBAQEBAgIeAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQECAQECAQICAgIDAwIHBQMCAQICAQIBAgECAQICAQIBAgEBAQEDAgIBAgEBAgEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAgECAQIBAgECAQIBAgECAQICAQEBAQAAAAAAQAAAAADtAWcA/0AAAEzFTM1IRUzFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUjFSMVIxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFTMVMxUzFSMVIzUjFSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUhNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNSE1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUjNSM1IzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUzNTM1MzUhAkwQGAE8BAQEBAQEBAQEBAQEBAQEBAQEBAQEENgEBAQEBAQEBAQEBAQEBAQE9AQEBAQEBAQEBAQEBAQEBAQEBAQEBAicBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQIBAQEBAgEBAgECAQIBAgIBAgMCAgIDAgMDBAMEBQQHBwQBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBASAHDAMBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBLAgHBAQDAwMCAgICAgIBAgECAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQE/fwEBAQEBAQEBAQEBAQEBAQEBAQEBAgBmAQEBAQEBAQEBAQEBAQIBAgECAQMCAgMCBAQGBQo8AQEBAQEBAQEBAQEBAQEBAQEBAQEBAgB9AWcBAQECAQICAQIBAgECAQICAQIBAgECAQEBAwIBAgECAQIBAgMCAgMEBAEBAQIBAgECAgECAQIBAgIBAgECAgEBAQIEBAQDAwICAwECAgECAgEBAQIBAQIBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQQBAgEBAQIBAQIBAQEBAgEBAgEBAQIBAQECAQECAQEBAQECAQIBAQIBAQECAQEBAgEBAQIBAQECAQEBAQECAQECAQEBAgEBAQIBAQIBAQEBAQIBAQIBAQIBAQEBAQIBAQIBAQEBAgEBAgEBAQIBAQEBAgEBAgEBAgEBAQECAQEBAQIBAQEBAQEBAgEBAQECAQEBAgEBAQIBAQECAQEBAgEBAgEBAQIBAgEBAQIBAQECAQECAQEBAQIBAQIBAQIBAQEBAgEBAgEBAQECAQEBAgEBAgEBAQECAQECAQEBAQECAQEBAgEBAQIBAQIBAQEBAQECAQEBAgEBAQECAQEBAgEBAQEBAQICHgEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAgEBAgECAgICAwMCBwUDAgECAgECAQIBAgECAgECAQIBAQEBAwICAQIBAQIBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQIBAgECAQIBAgECAQIBAgECAgEBAQEAAAAAAEAAAABAACTKPMBXw889QALCAAAAAAAyGna6gAAAADIadrqAAAAAAO0BZwAAAAIAAIAAAAAAAAAAQAABrT+XgDeBZwAAAAAA7QAAQAAAAAAAAAAAAAAAAAAABMD9gAAAAAAAAKqAAAB/AAAA/YAAAH8AAACzgAABZwAAALOAAAFnAAAAd4AAAFnAAAA7wAAAO8AAACzAAABHwAAAE8AAAEfAAABZwAAAAAECgQKBAoECggUCBQIFAgUCBQIFAgUCBQIFAgUCBQIFAgUCBQIFAABAAAAEwP+AAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACgB+AAEAAAAAABMABQAAAAMAAQQJAAAAaAAFAAMAAQQJAAEACgBtAAMAAQQJAAIADgB3AAMAAQQJAAMADgCFAAMAAQQJAAQAGgCTAAMAAQQJAAUAVgCtAAMAAQQJAAYACgEDAAMAAQQJABMACgENAAMAAQQJAMgAbgEXUnVwZWUAVAB5AHAAZQBmAGEAYwBlACAAqQAgACgAeQBvAHUAcgAgAGMAbwBtAHAAYQBuAHkAKQAuACAAMgAwADEAMAAuACAAQQBsAGwAIABSAGkAZwBoAHQAcwAgAFIAZQBzAGUAcgB2AGUAZABSAHUAcABlAGUAUgBlAGcAdQBsAGEAcgB3AGUAYgBmAG8AbgB0AFIAdQBwAGUAZQAgAFIAZQBnAHUAbABhAHIAVgBlAHIAcwBpAG8AbgAgADEALgAwADAAIABKAHUAbAB5ACAAMQA1ACwAIAAyADAAMQAwACwAIABpAG4AaQB0AGkAYQBsACAAcgBlAGwAZQBhAHMAZQBSAHUAcABlAGUAUgB1AHAAZQBlAFQAaABpAHMAIABmAG8AbgB0ACAAdwBhAHMAIABnAGUAbgBlAHIAYQB0AGUAZAAgAGIAeQAgAHQAaABlACAARgBvAG4AdAAgAFMAcQB1AGkAcgByAGUAbAAgAEcAZQBuAGUAcgBhAHQAbwByAC4AAAIAAAAAAAD/JwCWAAAAAAAAAAAAAAAAAAAAAAAAAAAAEwAAAAEAAgADADUBAgEDAQQBBQEGAQcBCAEJAQoBCwEMAQ0BDgEPB3VuaTAwQTAHdW5pMjAwMAd1bmkyMDAxB3VuaTIwMDIHdW5pMjAwMwd1bmkyMDA0B3VuaTIwMDUHdW5pMjAwNgd1bmkyMDA3B3VuaTIwMDgHdW5pMjAwOQd1bmkyMDBBB3VuaTIwMkYHdW5pMjA1Rg==)
					    format('truetype');
					    font-weight: normal;
					    font-style: normal;
					}
					span.rupeeSign
					{
						font-family: rupee;
						font-size: 15px;
					}
				</style>
			</head>
			<body>
				<table class="traderp">
					<tr>
						<td class="print_formHeading" style="text-decoration:underline;" colspan="2">
							DELIVERY CHALLAN CUM TAX INVOICE
          				</td>
					</tr>
					<tr>
						<td style="width:70%; text-align:left">
							<img src="cid:image" title="Logo" width="100%" height="100px"/>
						</td>
						<td class="traderp" style="line-height:30%; width:30%;">
							<p>Phone:##########</p>
							<p>Fax:########</p>
							<p>E-mail:default@default.com</p>
							<p>Website:www.default.com</p>
							<p>Address</p>
							<p>City-pincode</p>
						</td>
					</tr>
					<tr>
						<td class="traderp" style="width:70%; vertical-align: top" colspan="2">
							<table class="traderp print_tableBorder">
								<tr>
									<td class ="print_tdBorder" style="width:70%; vertical-align: top;" >
										<b>To: </b>
										<xsl:value-of select="ChallanData/m_oSalesData/SalesData/ClientData/m_strCompanyName" /><br/>
										<xsl:value-of select="ChallanData/m_oSalesData/SalesData/ClientData/m_strAddress" /><br/>
										<xsl:value-of select="ChallanData/m_oSalesData/SalesData/ClientData/m_strCity" /> - <xsl:value-of select="ChallanData/m_oSalesData/SalesData/ClientData/m_strPinCode" /><br/>
										<xsl:value-of select="ChallanData/m_oSalesData/SalesData/ClientData/m_strTelephone" /><br/>
										<xsl:value-of select="ChallanData/m_oSalesData/SalesData/ClientData/m_strMobileNumber" />
									</td>
									<td rowspan="2" style="padding-left:0px; padding-right:0px">
										<table class="traderp print_tableBorderBottom">
											<tr>
												<td class="print_fieldData print_tdBorderBottom" style="text-align:left" id="m_strDateTime">
													<table  class="traderp">
														<tr>
															<td style="width:40%;">Date<br/>Time</td>
															<td style="width:5%;">:<br/>:</td>
															<td style="width:55%;"><xsl:value-of select="ChallanData/m_strDate"/><br/><xsl:value-of select="ChallanData/m_strTime"/></td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td class="print_fieldData  print_tdBorderBottom" style="text-align:left">
													<table  class="traderp">
														<tr>
															<td style="width:40%;">TIN No.<br/>C.S.T. No.</td>
															<td style="width:5%;">:<br/>:</td>
															<td style="width:55%;"><xsl:value-of select="ChallanData/m_oSalesData/SalesData/ClientData/m_strTinNumber" /><br/><xsl:value-of select="ChallanData/m_oSalesData/SalesData/ClientData/m_strCSTNumber" /></td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td class="print_fieldData print_tdBorderBottom" style="text-align:left">
													<table  class="traderp">
														<tr>
															<td style="width:40%;">Order No.</td>
															<td style="width:5%;">:</td>
															<td style="width:55%;"><xsl:value-of select="ChallanData/m_oSalesData/SalesData/m_strPurchaseOrderNumber"/></td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td class="print_fieldData print_tdBorderBottom" style="text-align:left">
													<table  class="traderp">
														<tr>
															<td style="width:40%;">DC No.</td>
															<td style="width:5%;">:</td>
															<td style="width:55%;"><xsl:value-of select="ChallanData/m_strChallanNumber"/></td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td class="print_fieldData" style="text-align:left">
													<table  class="traderp">
														<tr>
															<td style="width:40%;">DC Date</td>
															<td style="width:5%;">:</td>
															<td style="width:55%;"><xsl:value-of select="ChallanData/m_strDate"/></td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td class ="print_tdBorder" style="width:70%; vertical-align: top;">
										<b>Site: </b>
											<xsl:value-of select="ChallanData/m_oSalesData/SalesData/SiteData/m_strSiteName" /><br/>
											<xsl:value-of select="ChallanData/m_oSalesData/SalesData/SiteData/m_strSiteAddress" />
									</td>
								</tr>
								<tr>
									<td colspan="2">
										<div style="height:760px">
											<table class="print_tableBorder traderp">
<tr>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:5%">
														#
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:35%">
														Articles
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="7%">
														Size
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:8%">
														Qty
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:8%;">
														Unit
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:10%">
														Price
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:7%">
														Disc(%)
													</td>
													<td class="print_fieldTableHeading print_tdBorder"
														style="width:7%">
														Tax(%)
													</td>
													<td class="print_fieldTableHeading print_tdBorder" style="width:13%">
														Amount
													</td>
												</tr>
												<xsl:for-each select="ChallanData/m_oSalesData/SalesData/m_oSalesLineItems/SalesLineItemData">
													<tr>
														<td class="print_fieldTableData print_tdBorder" style="text-align:center">
															<xsl:value-of select="position()"/>
														</td>
														<td class="print_fieldTableData print_tdBorder">
															<xsl:value-of select="m_strArticleDescription" />
														</td>
														<td class="print_fieldTableData print_tdBorder">
															<xsl:value-of select="m_strDetail" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<xsl:value-of select="format-number(m_nQuantity, '##,##,##,###.00')" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:center">
															<xsl:value-of select="m_strUnit" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(m_nPrice, '##,##,##,###.00')" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<xsl:value-of select="format-number(m_nDiscount, '##,##,##,##0.00')" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<xsl:value-of select="format-number(m_nTax, '##,##,##,##0.00')" />
														</td>
														<td class="print_fieldTableData print_tdBorder" style="text-align:right">
															<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(m_nQuantity * (m_nPrice - (m_nPrice * m_nDiscount div 100)), '##,##,##,###.00')"/>
														</td>
													</tr>
												</xsl:for-each> 
												<tr>
													<td class="print_fieldTableHeading print_tdBorder" colspan="8" style="text-align:right">
														Total
													</td>
													<td class="print_fieldTableHeading print_tdBorder" colspan="1" style="text-align:right">
														<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number($subSalesTotal,'##,##,##,###.00')"/>
													</td>
												</tr>
											</table>
											<table class="traderp">
												<tr>
													<td class="traderp" style="vertical-align:top;" colspan="2">
													</td>
												</tr>
												<tr>
													<td style="width:65%;">
													</td>
													<td style="width:35%;">
														<table class="print_tableBorder traderp">
															<xsl:for-each select="ChallanData/m_oSalesData/SalesData/m_oTaxes/Tax">
																<tr>
																	<td class="print_fieldTableData print_tdBorder"
																		style="width:10%; text-align:right">
																		<xsl:value-of select="m_strTaxName" />
																	</td>
																	<td class="print_fieldTableData print_tdBorder" style="width:12%; text-align:right">
																		<xsl:value-of select="format-number(m_nTaxPercent, '##,##,##,###.00')"/>
																	</td>
																	<td class="print_fieldTableData print_tdBorder" style="width:13%; text-align:right">
																		<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(m_nTaxAmount, '##,##,##,###.00')" />
																	</td>
																</tr>
															</xsl:for-each> 
															<xsl:if test= "$subTaxTotal &gt; 0">
																<tr>
																	<td></td>
																	<td class="print_fieldTableHeading print_tdBorder" style="text-align:right">
																		Total
																	</td>
																	<td class="print_fieldTableHeading print_tdBorder" style="text-align:right">
																		<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number($subTaxTotal,'##,##,##,###.00')"/>
																	</td>
																</tr>
															</xsl:if>
															<tr>
																<td class="print_fieldTableHeading print_tdBorder" style="text-align:right" colspan="2">
																	Round Off
																</td>
																<td class="print_fieldTableHeading print_tdBorder" style="text-align:right">
																	<xsl:value-of select="format-number(round($subSalesTotal + $subTaxTotal) - ($subSalesTotal + $subTaxTotal) ,'##,##,##,##0.00')"/>
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
									<td class="print_fieldTableHeading print_tdBorder" style="text-align:right; width:70%">
										Grand Total
									</td>
									<td class="print_fieldTableHeading print_tdBorder" style="text-align:right">
										<span class="rupeeSign" style="font-weight: normal;">R </span><xsl:value-of select="format-number(round($subSalesTotal + $subTaxTotal),'##,##,##,###.00')"/>
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