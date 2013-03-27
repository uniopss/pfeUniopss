<%--
	jsp File browser 1.0
	Copyright (C) 2003,2004, Boris von Loesch
	This program is free software; you can redistribute it and/or modify it under
	the terms of the GNU General Public License as published by the
	Free Software Foundation; either version 2 of the License, or (at your option)
	any later version.
	This program is distributed in the hope that it will be useful, but
	WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
	FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with
	this program; if not, write to the
	Free Software Foundation, Inc.,
	59 Temple Place, Suite 330,
	Boston, MA 02111-1307 USA
	- Description: jsp File browser v1.0 -- This JSP program allows remote web-based
				file access and manipulation.  You can copy, create, move and delete files.
				Text files can be edited and groups of files and folders can be downloaded
				as a single zip file that's created on the fly.
	- Credits: Taylor Bastien, David Levine, David Cowan
--%>
<%!
	/**
	 * Wrapperclass to wrap an OutputStream around a Writer
	 */
        class Writer2Stream extends java.io.OutputStream{
                java.io.Writer out;
                Writer2Stream (java.io.Writer w){
			super();
			out = w;
		}
                public void write(int i) throws java.io.IOException{
			out.write(i);
		}
                public void write(byte[] b) throws java.io.IOException{
			for (int i=0;i<b.length;i++){
				int n=b[i];
				//Convert byte to ubyte
				n=((n>>>4)&0xF)*16+(n&0xF);
				out.write (n);
			}
		}
                public void write(byte[] b, int off, int len) throws java.io.IOException{
			for (int i = off; i < off + len; i++){
				int n=b[i];
				n = ((n>>>4)&0xF)*16+(n&0xF);
				out.write(n);
			}
		}
	} //End of class Writer2Stream
%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/x.tld" prefix="x" %>
<%@ taglib uri="/WEB-INF/tlds/ion.tld" prefix="ion" %>
<c:if test="${userLogin=='admin'}">
<%
        try {
                if (request.getParameter("f") != null){
                        String filePath = request.getParameter("f");
                        java.io.File f = new java.io.File(filePath);
                        if (f.exists() && f.canRead()) {
                                response.setContentType ("application/octet-stream");
                                response.setHeader ("Content-Disposition", "attachment;filename=\""+f.getName()+"\"");
                                response.setContentLength((int) f.length());
                                java.io.BufferedInputStream fileInput = new java.io.BufferedInputStream(new java.io.FileInputStream(f));
                                byte buffer[] = new byte[8 * 1024];
                                out.clearBuffer();
                                java.io.OutputStream out_s = new Writer2Stream(out);
                                int b;
                                while((b=fileInput.read(buffer))!=-1) out_s.write(buffer, 0, b);
                                fileInput.close();
                                out_s.flush();
                        }
                }
        } catch(Exception e) {     out.println("!! Erreur a la lecture de "+request.getParameter("f"));
        }
%>
</c:if>
