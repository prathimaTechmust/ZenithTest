<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <html>
      <body>
      	<li class="right" style="line-height:1rem;list-style-type:none;padding-right:0.5rem;">
      		<a href="#"><img align="right" width="25px" height="16px" src="images/closeOffcanvas.png"></img></a>
    	</li>
		<ul class="off-canvas-list" style="padding-top:1.5rem;">
			<xsl:for-each select="root/ActionArea">
			<xsl:sort select="Sequence" data-type="number"/>
		      	 <xsl:for-each select="ActionList/Action">
				 <xsl:sort select="Sequence" data-type="number"/>
			         <li style="list-style: none;"><a href='javascript:{Target}'><span><xsl:value-of select="ActionName"/></span></a></li>
		         </xsl:for-each>
		   </xsl:for-each>
		</ul>
	</body>
	</html>
  </xsl:template>

</xsl:stylesheet>
