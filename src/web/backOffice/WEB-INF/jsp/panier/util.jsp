<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/ion.tld" prefix="ion" %>
<%@ taglib uri="/WEB-INF/tlds/struts-template.tld" prefix="template" %>

        <%
                String _a = request.getAttribute("txt")!=null?"_":"<";
                String _b = request.getAttribute("txt")!=null?"_":">";
                String type = ""+request.getAttribute("type") ;
                String pRef = ""+request.getAttribute("pRef") ;
                java.util.Vector vE = new java.util.Vector();
                pageContext.setAttribute("xml", request.getAttribute("xml").toString().replaceFirst("<\\?xml version=\"1.0\" encoding=\".+\"\\?>",""));
                String regex = _a+type+_b+"\\s*([a-z|A-Z|0-9|]+)\\s*"+_a+"/"+type+_b;
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex,java.util.regex.Pattern.MULTILINE);
                java.util.regex.Matcher matcher = pattern.matcher(pageContext.getAttribute("xml")+"");
                while (matcher.find()) {
                        String _m = matcher.group().trim().replaceAll(_a+type+_b,"").replaceAll(_a+"/"+type+_b,"") ;
                        if (!vE.contains(_m) && !pRef.equals(_m))
                                vE.add(_m);
                }
                pageContext.setAttribute("_type",type+"s");
        %>
        <ion:selectObjects type="org.nextime.ion.frontoffice.objectSelector.SearchOnlinePublications">
                <ion:param name="queryString" value="${pRef}"/>
                <ion:param name="index" value="${currentLocale}"/>
                <ion:param name="type"  value="${_type}"/>
                <ion:param name="view"  value=""/>
                <ion:iterateOver var="result">
                        <c:set var="_var"><c:out value="${result.searchResult.name}" escapeXml="false"/></c:set>
                        <%
                                String _m = pageContext.getAttribute("_var").toString().trim() ;
                                if (!vE.contains(_m) && !pRef.equals(_m))
                                        vE.add(_m);
                        %>
                </ion:iterateOver>
        </ion:selectObjects>
        <%
                String _e = "";
                for(int i=0;i<vE.size();i++)
                        _e += (request.getAttribute("txt")!=null?vE.get(i):_a+type+_b+vE.get(i)+_a+"/"+type+_b)+" ";
                regex = _a+type+"s"+_b+"(.*?)"+_a+"/"+type+"s"+_b;
                pattern = java.util.regex.Pattern.compile(regex,java.util.regex.Pattern.MULTILINE);
                matcher = pattern.matcher(pageContext.getAttribute("xml")+"");
                String _f = matcher.replaceAll(request.getAttribute("txt")!=null?_e:_a+type+"s"+_b+_e+_a+"/"+type+"s"+_b);
                _f.replaceAll(request.getAttribute("txt")!=null?_e:_a+type+"s/"+_b,_a+type+"s"+_b+_e+_a+"/"+type+"s"+_b);
                out.println(_f);
        %>
