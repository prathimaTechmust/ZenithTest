<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <html>
      <body>
		<table class="trademust">
			<div id='cssmenu' onload="removeActiveClass()">
				<ul>
					<xsl:for-each select="root/ActionArea">
					<xsl:sort select="Sequence" data-type="number"/>
				   <li class='has-sub'><a href="javascript:initMenu()"><span><xsl:value-of select="ActionAreaName"/></span></a>
				      <ul>
				      	 <xsl:for-each select="ActionList/Action">
						 <xsl:sort select="Sequence" data-type="number"/>
					         <li><a href='javascript:{Target}'><span><xsl:value-of select="ActionName"/></span></a></li>
				         </xsl:for-each>
				      </ul>
				   </li>
				   </xsl:for-each>
				</ul>
			</div>
		</table>     
	</body>
	</html>
  </xsl:template>

</xsl:stylesheet>
