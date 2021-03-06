<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:m="http://www.w3.org/1998/Math/MathML"
                xmlns:mal="http://projectmallard.org/1.0/"
                version="1.0">

  <!-- Parameters -->
<!--
  <xsl:param name="mal2html.editor_mode" select="true()"/>
  <xsl:param name="html.syntax.highlight" select="false()"/>
-->

<!--  Some custom CSS -->

<xsl:template name="html.css.custom">
  <xsl:param name="node" select="."/>
  <xsl:param name="direction">
    <xsl:call-template name="l10n.direction"/>
  </xsl:param>
  <xsl:param name="left">
    <xsl:call-template name="l10n.align.start">
      <xsl:with-param name="direction" select="$direction"/>
    </xsl:call-template>
  </xsl:param>
  <xsl:param name="right">
    <xsl:call-template name="l10n.align.end">
      <xsl:with-param name="direction" select="$direction"/>
    </xsl:call-template>
  </xsl:param>
  <xsl:text>

span.media-image {
    display: block;
    float: left;
    margin-right: .5em;
}

p {
    overflow: auto;
}
dl.terms dt.terms {
    font-weight: bold;
}

div.sect div.hgroup {
    border-bottom: solid 2px #babdb6;
}

div.sect div.sect div.hgroup {
    border-bottom: solid 1px #babdb6;
}
</xsl:text>
</xsl:template>


</xsl:stylesheet>
