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
<%@page import="java.util.*,
				java.net.*,
				java.text.*,
				java.util.zip.*,
				java.io.*" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/ion.tld" prefix="ion" %>
<%!

//The number of colums for the edit field
	private static final int EDITFIELD_COLS = 85;
//The number of rows for the edit field
	private static final int EDITFIELD_ROWS = 30;
//Open a new window to view a file
	private static final boolean USE_POPUP = true;
	/**
	 * If USE_DIR_PREVIEW = true, then for every directory a tooltip will be
	 * created (hold the mouse over the link) with the first DIR_PREVIEW_NUMBER entries.
	 * This can yield to performance issues. Turn it of, if the directory loads to slow.
	 */
	private static final boolean USE_DIR_PREVIEW = true;
	private static final int DIR_PREVIEW_NUMBER = 10;
	/**
	 * The name of an optional CSS Stylesheet file
	 */
	private static final String CSS_NAME = "/backoffice/styles/browser.css";
	/**
	 * The compression level for zip file creation (0-9)
	 * 0 = No compression
	 * 1 = Standard compression (Very fast)
	 * ...
	 * 9 = Best compression (Very slow)
	 */
	private static final int COMPRESSION_LEVEL = 1;
	/**
	 * The FORBIDDEN_DRIVES are not displayed on the list. This can be usefull, if the
	 * server runs on a windows platform, to avoid a message box, if you try to access
	 * an empty removable drive (See KNOWN BUGS in Readme.txt).
	 */
	private static final String[] FORBIDDEN_DRIVES= {};

	/**
	 * Command of the shell interpreter and the parameter to run a programm
	 */
	private static final String[] COMMAND_INTERPRETER = {};
	/**
	 * Max time in ms a process is allowed to run, before it will be terminated
	 */
	private static final long MAX_PROCESS_RUNNING_TIME = 30000; //30 seconds

//Button names
        private static final String RENAME_FILE = "cr_rn";
	private static final String DELETE_FILES = "Supprimer les éléments selectionnés";
        private static final String CREATE_DIR = "cr_dir";
        private static final String MOVE_FILES = "cr_mv";
        private static final String COPY_FILES = "cr_cp";

//Normally you should not change anything after this line
//----------------------------------------------------------------------------------
//Change this to locate the tempfile directory for upload (not longer needed)
	private static String VERSION_NR = "1.0";
	private static DateFormat dateFormat = DateFormat.getDateTimeInstance();
	public class FileInfo{
		public String name = null,
		clientFileName = null,
		fileContentType = null;
		private byte[] fileContents = null;
		public File file = null;
		public StringBuffer sb = new StringBuffer(100);
		public void setFileContents(byte[] aByteArray){
			fileContents = new byte[aByteArray.length];
			System.arraycopy(aByteArray, 0, fileContents, 0, aByteArray.length);
		}
	}
	// A Class with methods used to process a ServletInputStream
	public class HttpMultiPartParser{
		private final String lineSeparator = System.getProperty("line.separator", "\n");
		private final int ONE_MB=1024*1024*1;

		public Hashtable processData(ServletInputStream is, String boundary, String saveInDir)
				throws IllegalArgumentException, IOException {
			if (is == null) throw new IllegalArgumentException("InputStream");
			if (boundary == null || boundary.trim().length() < 1)
				throw new IllegalArgumentException("\"" + boundary + "\" is an illegal boundary indicator");
			boundary = "--" + boundary;
			StringTokenizer stLine = null, stFields = null;
			FileInfo fileInfo = null;
			Hashtable dataTable = new Hashtable(5);
			String line = null, field = null, paramName = null;
			boolean saveFiles = (saveInDir != null && saveInDir.trim().length() > 0);
			boolean isFile = false;
			if (saveFiles){ // Create the required directory (including parent dirs)
				File f = new File(saveInDir);
				f.mkdirs();
			}
			line = getLine(is);
			if (line == null || !line.startsWith(boundary))
				throw new IOException("Boundary not found; boundary = " + boundary
						+ ", line = " + line);
			while (line != null){
				if (line == null || !line.startsWith(boundary)) return dataTable;
				line = getLine(is);
				if (line == null) return dataTable;
				stLine = new StringTokenizer(line, ";\r\n");
				if (stLine.countTokens() < 2) throw new IllegalArgumentException("Bad data in second line");
				line = stLine.nextToken().toLowerCase();
				if (line.indexOf("form-data") < 0) throw new IllegalArgumentException("Bad data in second line");
				stFields = new StringTokenizer(stLine.nextToken(), "=\"");
				if (stFields.countTokens() < 2) throw new IllegalArgumentException("Bad data in second line");
				fileInfo = new FileInfo();
				stFields.nextToken();
				paramName = stFields.nextToken();
				isFile = false;
				if (stLine.hasMoreTokens()){
					field    = stLine.nextToken();
					stFields = new StringTokenizer(field, "=\"");
					if (stFields.countTokens() > 1){
						if (stFields.nextToken().trim().equalsIgnoreCase("filename")){
							fileInfo.name=paramName;
							String value = stFields.nextToken();
							if (value != null && value.trim().length() > 0){
								fileInfo.clientFileName=value;
								isFile = true;
							}
							else{
								line = getLine(is); // Skip "Content-Type:" line
								line = getLine(is); // Skip blank line
								line = getLine(is); // Skip blank line
								line = getLine(is); // Position to boundary line
								continue;
							}
						}
					}
					else if (field.toLowerCase().indexOf("filename") >= 0){
						line = getLine(is); // Skip "Content-Type:" line
						line = getLine(is); // Skip blank line
						line = getLine(is); // Skip blank line
						line = getLine(is); // Position to boundary line
						continue;
					}
				}
				boolean skipBlankLine = true;
				if (isFile){
					line = getLine(is);
					if (line == null) return dataTable;
					if (line.trim().length() < 1) skipBlankLine = false;
					else{
						stLine = new StringTokenizer(line, ": ");
						if (stLine.countTokens() < 2)
							throw new IllegalArgumentException("Bad data in third line");
						stLine.nextToken(); // Content-Type
						fileInfo.fileContentType=stLine.nextToken();
					}
				}
				if (skipBlankLine){
					line = getLine(is);
					if (line == null) return dataTable;
				}
				if (!isFile){
					line = getLine(is);
					if (line == null) return dataTable;
					dataTable.put(paramName, line);
					// If parameter is dir, change saveInDir to dir
					if (paramName.equals("dir")) saveInDir = line;
					line = getLine(is);
					continue;
				}
				try{
					OutputStream os = null;
					String path     = null;
					if (saveFiles)
						os = new FileOutputStream(path = getFileName(saveInDir, fileInfo.clientFileName));
					else os = new ByteArrayOutputStream(ONE_MB);
					boolean readingContent = true;
					byte previousLine[] = new byte[2 * ONE_MB];
					byte temp[] = null;
					byte currentLine[] = new byte[2 * ONE_MB];
					int read, read3;
					if ((read = is.readLine(previousLine, 0, previousLine.length)) == -1) {
						line = null;
						break;
					}
					while (readingContent){
						if ((read3 = is.readLine(currentLine, 0, currentLine.length)) == -1) {
							line = null;
							break;
						}
						if (compareBoundary(boundary, currentLine)){
							os.write(previousLine, 0, read-2);
							line = new String(currentLine, 0, read3);
							break;
						}
						else{
							os.write(previousLine, 0, read);
							temp = currentLine;
							currentLine = previousLine;
							previousLine = temp;
							read = read3;
						}//end else
					}//end while
					os.flush();
					os.close();
					if (!saveFiles){
						ByteArrayOutputStream baos = (ByteArrayOutputStream)os;
						fileInfo.setFileContents(baos.toByteArray());
					}
					else fileInfo.file = new File(path);
					dataTable.put(paramName, fileInfo);
				}//end try
				catch (IOException e) {
					System.out.println("1");
					System.out.println("Error : "+e.getClass().getName()+ e.getMessage());
					throw e;
				}
			}
			return dataTable;
		}

		/**
		 * Compares boundary string to byte array
		 */
		private boolean compareBoundary(String boundary, byte ba[]){
			byte b;
			if (boundary == null || ba == null) return false;
			for (int i=0; i < boundary.length(); i++)
				if ((byte)boundary.charAt(i) != ba[i]) return false;
			return true;
		}

		/** Convenience method to read HTTP header lines */
		private synchronized String getLine(ServletInputStream sis) throws IOException{
			byte b[]  = new byte[1024];
			int read = sis.readLine(b, 0, b.length), index;
			String line = null;
			if (read != -1){
				line = new String(b, 0, read);
				if ((index = line.indexOf('\n')) >= 0) line   = line.substring(0, index-1);
			}
			return line;
		}

		public String getFileName(String dir, String fileName) throws IllegalArgumentException{
			String path = null;
			if (dir == null || fileName == null) throw new IllegalArgumentException("dir or fileName is null");
			int index = fileName.lastIndexOf('/');
			String name = null;
			if (index >= 0) name = fileName.substring(index + 1);
			else name = fileName;
			index = name.lastIndexOf('\\');
			if (index >= 0) fileName = name.substring(index + 1);
			path = dir + File.separator + fileName;
			if (File.separatorChar == '/') return path.replace('\\', File.separatorChar);
			else return path.replace('/', File.separatorChar);
		}
	} //End of class HttpMultiPartParser

	/**
	 * This class is a comparator to sort the filenames and dirs
	 */
	class FileComp implements Comparator{
		int mode;
		int sign;
		/**
		 * @param mode sort by 1=Filename, 2=Size, 3=Date, 4=Type
		 * The default sorting method is by Name
		 * Negative mode means descending sort
		 */
		FileComp(){
			this.mode = 1;
			this.sign = 1;
		}

		FileComp (int mode){
			if (mode<0){
				this.mode = -mode;
				sign = -1;
			}
			else{
				this.mode = mode;
				this.sign = 1;
			}
		}

		public int compare(Object o1, Object o2){
			File f1 = (File)o1;
			File f2 = (File)o2;
			if (f1.isDirectory()){
				if (f2.isDirectory()){
					switch(mode){
						//Filename or Type
						case 1: case 4:return sign * f1.getAbsolutePath().toUpperCase().compareTo(f2.getAbsolutePath().toUpperCase());
						//Filesize
						case 2:return sign * (new Long(f1.length()).compareTo(new Long(f2.length())));
						//Date
						case 3:return sign * (new Long(f1.lastModified()).compareTo(new Long(f2.lastModified())));
						default:return 1;
					}
				}
				else return -1;
			}
			else if (f2.isDirectory()) return 1;
			else{
				switch(mode){
					case 1:return sign * f1.getAbsolutePath().toUpperCase().compareTo(f2.getAbsolutePath().toUpperCase());
					case 2:return sign * (new Long(f1.length()).compareTo(new Long(f2.length())));
					case 3:return sign * (new Long(f1.lastModified()).compareTo(new Long(f2.lastModified())));
					case 4: { // Sort by extension
						int tempIndexf1 = f1.getAbsolutePath().lastIndexOf('.');
						int tempIndexf2 = f2.getAbsolutePath().lastIndexOf('.');
						if ((tempIndexf1 == -1) && (tempIndexf2 == -1)){ // Neither have an extension
							return sign * f1.getAbsolutePath().toUpperCase().compareTo(f2.getAbsolutePath().toUpperCase());
						}
						// f1 has no extension
						else if(tempIndexf1 == -1) return -sign;
						// f2 has no extension
						else if(tempIndexf2 == -1) return sign;
						// Both have an extension
						else {
							String tempEndf1 = f1.getAbsolutePath().toUpperCase().substring(tempIndexf1);
							String tempEndf2 = f2.getAbsolutePath().toUpperCase().substring(tempIndexf2);
							return sign * tempEndf1.compareTo(tempEndf2);
						}
					}
					default:return 1;
				}
			}
		}
	}

	/**
	 * Wrapperclass to wrap an OutputStream around a Writer
	 */
	class Writer2Stream extends OutputStream{
		Writer out;
		Writer2Stream (Writer w){
			super();
			out = w;
		}
		public void write(int i) throws IOException{
			out.write(i);
		}
		public void write(byte[] b) throws IOException{
			for (int i=0;i<b.length;i++){
				int n=b[i];
				//Convert byte to ubyte
				n=((n>>>4)&0xF)*16+(n&0xF);
				out.write (n);
			}
		}
		public void write(byte[] b, int off, int len) throws IOException{
			for (int i = off; i < off + len; i++){
				int n=b[i];
				n = ((n>>>4)&0xF)*16+(n&0xF);
				out.write(n);
			}
		}
	} //End of class Writer2Stream

        static Vector expandFileList(String cdir, String[] files, boolean inclDirs){
		Vector v = new Vector();
		if (files == null) return v;
                for (int i=0; i < files.length; i++) v.add (new File(cdir,URLDecoder.decode(files[i])));
		for (int i=0; i < v.size(); i++){
			File f = (File) v.get(i);
			if (f.isDirectory()){
				File[] fs = f.listFiles();
				for (int n = 0; n < fs.length; n++) v.add(fs[n]);
				if (!inclDirs){
					v.remove(i);
					i--;
				}
			}
		}
		return v;
	}

	/**
	 * Method to build an absolute path
	 * @param dir the root dir
	 * @param name the name of the new directory
	 * @return if name is an absolute directory, returns name, else returns dir+name
	 */
	static String getDir (String dir, String name){
		if (!dir.endsWith(File.separator)) dir = dir + File.separator;
		File mv = new File (name);
		String new_dir = null;
		if (!mv.isAbsolute()){
			new_dir = dir + name;
		}
		else new_dir = name;
		return new_dir;
	}

	/**
	 * This Method converts a byte size in a kbytes or Mbytes size, depending on the size
	 *     @param size The size in bytes
	 *     @return String with size and unit
	 */
	static String convertFileSize (long size){
		int divisor = 1;
		String unit = "bytes";
		if (size>= 1024*1024){
			divisor = 1024*1024;
			unit = "MB";
		}
		else if (size>= 1024){
			divisor = 1024;
			unit = "KB";
		}
		if (divisor ==1) return size /divisor + " "+unit;
		String aftercomma = ""+100*(size % divisor)/divisor;
		if (aftercomma.length() == 1) aftercomma="0"+aftercomma;
		return size /divisor + "."+aftercomma+" "+unit;
	}

	/**
	* Copies all data from in to out
	* 	@param in the input stream
	*	@param out the output stream
	*	@param buffer copy buffer
	*/
	static void copyStreams (InputStream in, OutputStream out, byte[] buffer) throws IOException{
		copyStreamsWithoutClose(in, out, buffer);
		in.close();
		out.close();
	}

	/**
	* Copies all data from in to out
	* 	@param in the input stream
	*	@param out the output stream
	*	@param buffer copy buffer
	*/
	static void copyStreamsWithoutClose (InputStream in, OutputStream out, byte[] buffer) throws IOException{
		int b;
		while((b=in.read(buffer))!=-1) out.write(buffer, 0, b);
	}

	/**
	 * Returns the Mime Type of the file, depending on the extension of the filename
	 */
	static String getMimeType(String fName){
		fName = fName.toLowerCase();
		if (fName.endsWith(".jpg")||fName.endsWith(".jpeg")||fName.endsWith(".jpe")) return "image/jpeg";
		else if (fName.endsWith(".gif")) return "image/gif";
		else if (fName.endsWith(".pdf")) return "application/pdf";
		else if (fName.endsWith(".htm")||fName.endsWith(".html")||fName.endsWith(".shtml")) return "text/html";
		else if (fName.endsWith(".avi")) return "video/x-msvideo";
		else if (fName.endsWith(".mov")||fName.endsWith(".qt")) return "video/quicktime";
		else if (fName.endsWith(".mpg")||fName.endsWith(".mpeg")||fName.endsWith(".mpe")) return "video/mpeg";
		else if (fName.endsWith(".zip")) return "application/zip";
		else if (fName.endsWith(".tiff")||fName.endsWith(".tif")) return "image/tiff";
		else if (fName.endsWith(".rtf")) return "application/rtf";
		else if (fName.endsWith(".mid")||fName.endsWith(".midi")) return "audio/x-midi";
		else if (fName.endsWith(".xl")||fName.endsWith(".xls")||fName.endsWith(".xlv")||fName.endsWith(".xla")
				||fName.endsWith(".xlb")||fName.endsWith(".xlt")||fName.endsWith(".xlm")||fName.endsWith(".xlk"))
			return "application/excel";
		else if (fName.endsWith(".doc")||fName.endsWith(".dot")) return "application/msword";
		else if (fName.endsWith(".png")) return "image/png";
		else if (fName.endsWith(".xml")) return "text/xml";
		else if (fName.endsWith(".svg")) return "image/svg+xml";
		else if (fName.endsWith(".mp3")) return "audio/mp3";
		else if (fName.endsWith(".ogg")) return "audio/ogg";
		else return "text/plain";
	}

	/**
	* Converts some important chars (int) to the corresponding html string
	*/
	static String conv2Html(int i){
		if (i=='&') return "&amp;";
		else if (i=='<') return "&lt;";
		else if (i=='>') return "&gt;";
		else if (i=='"') return "&quot;";
		else return ""+(char)i;
	}

	/**
	* Converts a normal string to a html conform string
	*/
	static String conv2Html(String st){
		StringBuffer buf = new StringBuffer();
		for (int i = 0;i<st.length();i++){
			buf.append(conv2Html(st.charAt(i)));
		}
		return buf.toString();
	}

	/**
	* Starts a native process on the server
	* 	@param command the command to start the process
	*	@param dir the dir in which the process starts
	*/
	static String startProcess (String command, String dir) throws IOException{
		StringBuffer ret = new StringBuffer();
		String[] comm = new String[3];
		comm[0] = COMMAND_INTERPRETER[0];
		comm[1] = COMMAND_INTERPRETER[1];
		comm[2] = command;
		long start = System.currentTimeMillis();
		try {
			//Start process
			Process ls_proc = Runtime.getRuntime().exec(comm, null, new File(dir));
			//Get input and error streams
			BufferedInputStream ls_in = new BufferedInputStream(ls_proc.getInputStream());
			BufferedInputStream ls_err = new BufferedInputStream(ls_proc.getErrorStream());
			boolean end = false;
			while (!end){
				int c=0;
				while ((ls_err.available()>0)&&(++c <= 1000)){
					ret.append (conv2Html(ls_err.read()));
				}
				c=0;
				while ((ls_in.available()>0)&&(++c <= 1000)){
					ret.append (conv2Html(ls_in.read()));
				}
				try{
					ls_proc.exitValue();
					//if the process has not finished, an exception is thrown
					//else
					while (ls_err.available()>0) ret.append(conv2Html(ls_err.read()));
					while (ls_in.available()>0) ret.append(conv2Html(ls_in.read()));
					end = true;
				}
				catch (IllegalThreadStateException ex){
					System.out.println("2");
					System.out.println("Error : "+ex.getClass().getName()+ ex.getMessage());
					//Process is running
				}
				//The process is not allowed to run longer than given time.
				if (System.currentTimeMillis() - start > MAX_PROCESS_RUNNING_TIME){
					ls_proc.destroy();
					end = true;
					ret.append ("!!!! Process has timed out, destroyed !!!!!");
				}
				try { Thread.sleep(50); } catch (InterruptedException ie) {
					System.out.println("3");
					System.out.println("Error : "+ie.getClass().getName()+ ie.getMessage());
				}
			}
		}
		catch (IOException e) {
				System.out.println("4");
				System.out.println("Error : "+e.getClass().getName()+ e.getMessage());
				ret.append ("Erreur: "+e);
		}
		return ret.toString();
	}

	/**
	*	Returns true if the given filename tends towards a packed file
	*/
	static boolean isPacked(String name, boolean gz){
		return (name.toLowerCase().endsWith(".zip")||name.toLowerCase().endsWith(".jar")||
				(gz&&name.toLowerCase().endsWith(".gz"))||name.toLowerCase().endsWith(".war"));
	}
//---------------------------------------------------------------------------------------------------------------
%>
<% try {
//Get the current browsing directory
        String action = "" ;
        String selectedSite = "" ;
        String selectedType = "" ;
        String zdir = "" ;
        String wdir = "" ;
        String cdir = "" ;
        String sdir = "" ;
		
		
        HttpMultiPartParser parser = null ;
        Hashtable ht = null ;
        if ((request.getContentType()!=null)&&(request.getContentType().toLowerCase().startsWith("multipart")))
        {
                try {
										parser = new HttpMultiPartParser();
										int bstart = request.getContentType().lastIndexOf("oundary=");
										String bound = request.getContentType().substring(bstart+8);
                    ht = parser.processData(request.getInputStream(), bound, new java.io.File(session.getServletContext().getRealPath("/"),"../../temp").getCanonicalPath());
                    action = (String)ht.get("action") ;
                    selectedSite = (String)ht.get("selectedSite") ;
                    selectedType = (String)ht.get("selectedType") ;
                    cdir = (String)ht.get("cdir") ;
                }
                catch(Exception e) {
                	System.out.println("5");
                	System.out.println("Error : "+e.getClass().getName()+ e.getMessage());
                }
        }
        else
        {
                action = request.getParameter("action") ;
                selectedSite = (String) request.getParameter("selectedSite");
                selectedType = request.getParameter("selectedType") ;
                cdir = request.getParameter("cdir") ;
                sdir = request.getParameter("sdir") ;
        }
        if (!"_admin".equals(action)) {
                if (selectedSite==null || "".equals(selectedSite) || selectedType==null || "".equals(selectedType))
                        return ;
                org.nextime.ion.backoffice.bean.ResourceXmlBean bean = org.nextime.ion.backoffice.bean.Resources.getResourceXmlBean(session.getServletContext(),selectedType);
                java.io.File tresources = new java.io.File(org.nextime.ion.framework.config.Config.getInstance().getResourcesDirectory(),"/"+selectedSite);
                zdir = new java.io.File(tresources,bean.getDirectory()).getCanonicalPath();
                wdir = "/resources/"+selectedSite+"/"+bean.getDirectory() ;
        }
        else
                zdir=new java.io.File(org.nextime.ion.framework.config.Config.getInstance().getConfigDirectoryPath(),"..").getCanonicalPath();
        try {
                cdir = new java.io.File(cdir!=null && !"".equals(cdir)?cdir:zdir,sdir!=null && !"".equals(sdir)?sdir:"").getCanonicalPath() ;
        }
        catch(Exception e) {
        			System.out.println("6");
        			System.out.println("Error : "+e.getClass().getName()+ e.getMessage());
              cdir = zdir ;
        }
        
        
        
        request.setAttribute("dir", cdir);
        
        System.out.println("cdir= " + new java.io.File(cdir).toURI().toString() );	
        System.out.println("wdir= " + wdir);	
        System.out.println("zdir= " + zdir );	
        
        
        //String cwdir = wdir+new java.io.File(cdir).toURI().toString().substring(6+zdir.length()) ;
        String cwdir = wdir;
        System.out.println("cwdir = " + cwdir  );	
        
        

// The browser_name variable is used to keep track of the URI
// of the jsp file itself.  It is used in all link-backs.
	final String browser_name = request.getRequestURI();
	final String FOL_IMG = "";
	boolean nohtml = false;
	boolean dir_view = true;
		// Upload
		
		
		
    if (ht!=null){
		boolean error = false;
		try{
				if (ht.get("myFile")!=null){
				
				FileInfo fi = (FileInfo)ht.get("myFile");
				if (fi!=null){
                                        File f = fi.file;
                                        // Move file from temp to the right dir
                                        if (!f.renameTo(new File(cdir,f.getName()))){
                                                request.setAttribute("error", "Impossible de charger le fichier.");
                                                error = true;
                                                f.delete();
                                        }
                                        try {
                                                final String _zf = f.getName();
                                                java.io.FilenameFilter filter = new java.io.FilenameFilter() {
                                                        public boolean accept(java.io.File dir, String name) {
                                                                return name.matches(".+"+_zf+".+jpg");
                                                        }     } ;
                                                java.io.File[] zfiles = new java.io.File(zdir,"../cache").listFiles(filter);
                                                if (zfiles!=null) {
                                                        for (int j = 0; j < zfiles.length; j++) {
                                                                zfiles[j].delete();
                                                        }
                                                }
                                        } catch(Exception ze) {
                                        	System.out.println("7");
                                        	System.out.println("Error : "+ze.getClass().getName()+ ze.getMessage());
                                        }
                                }
                                else{
                                        request.setAttribute("error", "Probleme a la reception du fichier");
                                        error = true;
                                }
                        }
			else{
				
        request.setAttribute("error", "Aucun fichier a charger");
				error = true;
			}
		}
		catch (Exception e){
			System.out.println("8");
			System.out.println("Error : "+e.getClass().getName()+ e.getMessage());
			request.setAttribute("error", "Erreur "+e+". Chargement annule.");
			error = true;
		}
    if (!error) request.setAttribute("message", "Fichier charge correctement.");
		response.setContentType("text/html");
		
		
	}
	// Delete Files
	else if ((request.getParameter("Submit")!=null)&&(request.getParameter("Submit").equals(DELETE_FILES))){
    Vector v = expandFileList(cdir,request.getParameterValues("selfile"), true);
    
		boolean error = false;
		//delete backwards
		for (int i=v.size()-1;i>=0;i--){
			File f = (File)v.get(i);
                        if (!f.canWrite()||!f.delete()){
                                request.setAttribute("error", "Impossible de supprimer "+f.getAbsolutePath()+". Suppression annulee");
				error = true;
				break;
                        }
                        try {
                                final String _zf = f.getName();
                                java.io.FilenameFilter filter = new java.io.FilenameFilter() {
                                        public boolean accept(java.io.File dir, String name) {
                                                return name.matches(".+"+_zf+".+jpg");
                                        }     } ;
                                java.io.File[] zfiles = new java.io.File(zdir,"../cache").listFiles(filter);
                                if (zfiles!=null) {
                                        for (int j = 0; j < zfiles.length; j++) {
                                                zfiles[j].delete();
                                        }
                                }
                        } catch(Exception ze) {
                        	System.out.println("9");
                        	System.out.println("Error : "+ze.getClass().getName()+ ze.getMessage());
                        }
		}
                if ((!error)&&(v.size()>1)) request.setAttribute("message", "Fichiers supprimes");
                else if ((!error)&&(v.size()>0)) request.setAttribute("message", "Fichier supprime");
                else if (!error) request.setAttribute("error", "Aucun fichier selectionne");
	}
// Create Directory
	else if ((request.getParameter("Submit") != null)&&(request.getParameter("Submit").equals(CREATE_DIR))){
		String dir = ""+request.getAttribute("dir");
		String dir_name = request.getParameter("cr_dir");
                if (dir_name!=null && !"".equals(dir_name)) {
                        String new_dir = getDir (dir, dir_name);
                        if (new File(new_dir).mkdirs()){
                                request.setAttribute("message", "Repertoire cree");
                        }
                        else request.setAttribute("error", "Echec creation du repertoire "+new_dir);
                }
                else request.setAttribute("error", "Aucun repertoire a creer");
	}
// Rename a file
	else if ((request.getParameter("Submit") != null) && (request.getParameter("Submit").equals(RENAME_FILE))){
                Vector v = expandFileList(cdir,request.getParameterValues("selfile"), true);
		String dir = "" + request.getAttribute("dir");
                String new_file_name = request.getParameter("cr_rn");
		String new_file = getDir (dir, new_file_name);
		// The error conditions:
		// 1) Zero Files selected
                if (v.size() <= 0) request.setAttribute("error", "Renommage annule : il ne faut selectionner qu un seul element a renommer");
		// 2a) Multiple files selected and the first isn't a dir
		//     Here we assume that expandFileList builds v from top-bottom, starting with the dirs
		else if ((v.size() > 1) && !(((File)v.get(0)).isDirectory()))
                        request.setAttribute("error", "Renommage annule : il ne faut selectionner qu un seul element a renommer");
		// 2b) If there are multiple files from the same directory, rename fails
		else if ((v.size() > 1) && ((File)v.get(0)).isDirectory() && !(((File)v.get(0)).getPath().equals(((File)v.get(1)).getParent()))){
                        request.setAttribute("error", "Renommage annule : il ne faut selectionner qu un seul element a renommer");
		}
		else {
			File f = (File)v.get(0);
			// Test, if file_name is empty
			if ((new_file.trim() != "") && !new_file.endsWith(File.separator)){
				if (!f.canWrite() || !f.renameTo(new File(new_file.trim()))){
                                        request.setAttribute("error", "Creation du fichier " + new_file + " echouee");
				}
                                else {
                                        request.setAttribute("message", "Fichier "+((File)v.get(0)).getName()+" renomme en "+new File(new_file.trim()).getName());
                                        try {
                                                final String _zf = ((File)v.get(0)).getName();
                                                java.io.FilenameFilter filter = new java.io.FilenameFilter() {
                                                        public boolean accept(java.io.File dir, String name) {
                                                                return name.matches(".+"+_zf+".+jpg");
                                                        }     } ;
                                                java.io.File[] zfiles = new java.io.File(zdir,"../cache").listFiles(filter);
                                                if (zfiles!=null) {
                                                        for (int j = 0; j < zfiles.length; j++) {
                                                                zfiles[j].delete();
                                                        }
                                                }
                                        } catch(Exception ze) {
                                        	System.out.println("10");
                                        	System.out.println("Error : "+ze.getClass().getName()+ ze.getMessage());
                                        }
                                        
                                }
			}
                        else request.setAttribute("error", "Erreur: \"" + new_file_name + "\" n est pas un nom valide");
		}
	}
// Move selected file(s)
	else if ((request.getParameter("Submit") != null) && (request.getParameter("Submit").equals(MOVE_FILES))){
                Vector v = expandFileList(cdir,request.getParameterValues("selfile"), true);
		String dir = "" + request.getAttribute("dir");
                String dir_name = request.getParameter("cr_mv");
		String new_dir = getDir(dir, dir_name);
		boolean error = false;
		// This ensures that new_dir is a directory
		if (!new_dir.endsWith(File.separator)) new_dir += File.separator;
		for (int i = v.size() - 1; i >= 0; i--){
			File f = (File)v.get(i);
			if (!f.canWrite()||!f.renameTo(new File(new_dir + f.getAbsolutePath().substring(dir.length())))){
                                request.setAttribute("error", "Impossible de deplacer "+f.getAbsolutePath()+". Deplacement annule");
				error = true;
				break;
			}
		}
                if ((!error) && (v.size() > 1)) request.setAttribute("message", "Fichiers deplaces");
                else if ((!error) && (v.size() > 0)) request.setAttribute("message", "Fichier deplace");
                else if (!error) request.setAttribute("error", "Aucun fichier selectionne");
	}
// Copy Files
	else if ((request.getParameter("Submit")!=null)&&(request.getParameter("Submit").equals(COPY_FILES))){
                Vector v = expandFileList(cdir,request.getParameterValues("selfile"), true);
		String dir = (String)request.getAttribute("dir");
		if (!dir.endsWith(File.separator)) dir+=File.separator;
                String dir_name = request.getParameter("cr_cp");
		String new_dir = getDir(dir, dir_name);
		boolean error = false;
		if (!new_dir.endsWith(File.separator)) new_dir += File.separator;
		try{
			byte buffer[] = new byte[0xffff];
			for (int i = 0; i < v.size(); i++){
				File f_old = (File)v.get(i);
				File f_new = new File(new_dir + f_old.getAbsolutePath().substring(dir.length()));
				if (f_old.isDirectory()) f_new.mkdirs();
				// Overwriting is forbidden
				else if (!f_new.exists()){
					copyStreams(new FileInputStream (f_old), new FileOutputStream (f_new), buffer);
				}
				else{
					// File exists
                                        request.setAttribute("error", "Impossible de copier "+f_old.getAbsolutePath()+", fichier existant. Copie annulee");
					error = true;
					break;
				}
			}
		}
		catch (IOException e){
			System.out.println("11");
			System.out.println("Error : "+e.getClass().getName()+ e.getMessage());
			request.setAttribute("error", "Erreur "+e+". Copie annulee");
			error = true;
		}
                if ((!error)&&(v.size()>1)) request.setAttribute("message", "Fichiers copies");
                else if ((!error)&&(v.size()>0)) request.setAttribute("message", "Fichier copie");
                else if (!error) request.setAttribute("error", "Aucun fichier selectionne");
	}
// Directory viewer
	if (dir_view && request.getAttribute("dir")!=null){
		File f = new File(""+request.getAttribute("dir"));
		//Check, whether the dir exists
		if (!f.exists()){
                        request.setAttribute("error", "Repertoire "+f.getAbsolutePath()+" inexistant.");
			//if attribute olddir exists, it will change to olddir
			if (request.getAttribute("olddir")!=null){
				f = new File(""+request.getAttribute("olddir"));
			}
			//try to go to the parent dir
			else {
				if (f.getParent() != null) f = new File(f.getParent());
			}
			//If this dir also do also not exist, go back to browser.jsp root path
			if (!f.exists()){
				String path = null;
				if (application.getRealPath(request.getRequestURI()) != null)
					path = new File(application.getRealPath(request.getRequestURI())).getParent();

				if (path == null) // handle the case were we are not in a directory (ex: war file)
					path = new File(".").getAbsolutePath();
				f = new File(path);
			}
			request.setAttribute("dir", f.getAbsolutePath());
		}
%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=ISO-8859-1">
<meta name="robots" content="noindex">
<meta http-equiv="expires" content="0">
<meta http-equiv="pragma" content="no-cache">
<link rel="stylesheet" type="text/css" href="/backoffice/styles/browser.css">
<script type="text/javascript">
<!--
	<%// This section contains the Javascript used for interface elements %>
	var check = false;
	<%// Disables the checkbox feature %>
	function dis(){check = true;}

	var DOM = 0, MS = 0, OP = 0, b = 0;
	<%// Determine the browser type %>
	function CheckBrowser(){
		if (b == 0){
			if (window.opera) OP = 1;
			// Moz or Netscape
			if(document.getElementById) DOM = 1;
			// Micro$oft
			if(document.all && !OP) MS = 1;
			b = 1;
		}
	}
	<%// Allows the whole row to be selected %>
	function selrow (element, i){
		var erst;
		CheckBrowser();
		if ((OP==1)||(MS==1)) erst = element.firstChild.firstChild;
		else if (DOM==1) erst = element.firstChild.nextSibling.firstChild;
		<%// MouseIn %>
		if (i==0){
			if (erst.checked == true) element.className='mousechecked';
			else element.className='mousein';
		}
		<%// MouseOut %>
		else if (i==1){
			if (erst.checked == true) element.className='checked';
			else element.className='mouseout';
		}
		<%    // MouseClick %>
		else if ((i==2)&&(!check)){
			if (erst.checked==true) element.className='mousein';
			else element.className='mousechecked';
			erst.click();
		}
		else check=false;
	}
	<%//(De)select all checkboxes%>
	function AllFiles(){
		for(var x=0;x<document.FileList.elements.length;x++){
			var y = document.FileList.elements[x];
			var ytr = y.parentNode.parentNode;
			var check = document.FileList.selall.checked;
			if(y.name == 'selfile'){
				if (y.disabled != true){
					y.checked = check;
					if (y.checked == true) ytr.className = 'checked';
					else ytr.className = 'mouseout';
				}
			}
		}
	}
//-->
</script>
<title><%=request.getAttribute("dir")%></title>
<%
	System.out.println("dir att  --- = " );
%>	
	
</head>
<%
		int sortMode=1;
		if (request.getParameter("sort")!=null) sortMode = Integer.parseInt(request.getParameter("sort"));
%>
<body class="text" onload='' background="/backoffice/images/fond.gif">
        <form action="/backoffice/edx/browser.jsp" method="Post" name="FileList" onsubmit="return false;">
        <input type=hidden name=cdir id=cdir value="<%=cdir%>">
        <input type=hidden name=cwdir id=cwdir value="<%=cwdir%>">
        <input type=hidden name=sdir id=sdir value="">
        <input type=hidden name=sort id=sort value="<%=sortMode%>">
        <input type=hidden name=action id=action value="<%=action%>">
        <input type=hidden name=selectedSite id=selectedSite value="<%=selectedSite%>">
        <input type=hidden name=selectedType id=selectedType value="<%=selectedType%>">
<script>
function _refresh(sdir)
{
document.FileList.sdir.value=sdir;
document.FileList.submit();
}
function _sort(sort)
{
document.FileList.sort.value=sort;
document.FileList.submit();
}
function _open(x)
{
        window.open(x,'ressources','width=801,height=600,resizable=yes,scrollbars=yes');
}
</script>
        <div style="height:350px;overflow:auto">
        <table class="filelist" cellspacing="3px" cellpadding="3px">
<%
		// Output the table, starting with the headers.
		String dir = URLEncoder.encode("" + request.getAttribute("dir"));
		System.out.println("Dir = " + dir);
		
		int[] sort = new int[]{1,2,3,4};
		for (int i = 0;i<sort.length;i++)
			if (sort[i] == sortMode) sort[i] = -sort[i];
                out.println("<tr><th width='10'>&nbsp;</th><th width='300' title=\"Trier par nom\" align=left><a href=\"javascript:_sort('"+sort[0]+"')\">Nom</a></th>"+
                                "<th width='100' title=\"Trier par taille\" align=\"right\"><a href=\"javascript:_sort('"+sort[1]+"')\">Taille</a></th>" +
                                "<th width='100' title=\"Trier par type\" align=\"center\"><a href=\"javascript:_sort('"+sort[3]+"')\">Type</a></th>"+
                                "<th width='200' title=\"Trier par date\" align=\"left\"><a href=\"javascript:_sort('"+sort[2]+"')\">Date</a></th>"+
                                "<th width='100'>&nbsp;</th></tr>");
		char trenner = File.separatorChar;
		// Output the Root-Dirs, without FORBIDDEN_DRIVES
		File[] entry = File.listRoots();
		// Output the parent directory link ".."
                if (f.getParent()!=null && !zdir.equals(cdir)){
			out.println("<tr class=\"mouseout\" onmouseover=\"this.className='mousein'\""
					+ "onmouseout=\"this.className='mouseout'\">");
                        out.println("<td colspan=4><i>Contenu de "+cwdir+"</i>");
                        out.println("</td><td colspan=2 align=right>");
                        out.println(" &nbsp;<a href=\"javascript:_refresh('..')"
                               +"\">"+FOL_IMG+"Remonter au répertoire parent...</a>");
                        out.println("</td></tr>");
		}
		// Output all files and dirs and calculate the number of files and total size
		entry = f.listFiles();
		if (entry == null) entry = new File[]{};
		long totalSize = 0; // The total size of the files in the current directory
		long fileCount = 0; // The count of files in the current working directory
		if (entry != null && entry.length > 0){
			Arrays.sort(entry, new FileComp(sortMode));
			for (int i = 0; i < entry.length; i++){
				String name = URLEncoder.encode(entry[i].getAbsolutePath());
				String type = "File"; // This String will tell the extension of the file
                                if (entry[i].isDirectory()) type = "Répertoire"; // It's a DIR
				else {
					String tempName = entry[i].getName().replace(' ','_');
					if (tempName.lastIndexOf('.') != -1) type = tempName.substring(tempName.lastIndexOf('.')).toLowerCase();
				}
                                String ahref = "<a onmousedown=\"dis()\" href=\"javascript:_refresh('";
				String dlink = "&nbsp;"; // The "Download" link
				String elink = "&nbsp;"; // The "Edit" link
				String buf = conv2Html(entry[i].getName());
				if (!entry[i].canWrite()) buf = "<i>"+buf+"</i>";
				String link = buf;        // The standard view link, uses Mime-type
				if (entry[i].isDirectory()){
					if (entry[i].canRead()&&USE_DIR_PREVIEW){
						//Show the first DIR_PREVIEW_NUMBER directory entries in a tooltip
						File[] fs = entry[i].listFiles();
						if (fs == null) fs = new File[]{};
						Arrays.sort(fs, new FileComp());
						StringBuffer filenames =new StringBuffer();
						for (int i2 = 0;(i2<fs.length)&&(i2<10);i2++) {
							String fname = conv2Html(fs[i2].getName());
							if (fs[i2].isDirectory()) filenames.append ("["+fname+"];");
							else filenames.append (fname+";");
						}
						if (fs.length>DIR_PREVIEW_NUMBER) filenames.append("...");
						else if (filenames.length()>0) filenames.setLength(filenames.length()-1);
                                                link = ahref + buf + "')\" title=\"" + filenames + "\">"
                                                                + FOL_IMG  + buf + "</a>";
					}
					else if (entry[i].canRead()){
                                                link = ahref +  name + "')\">" + FOL_IMG + "[" + buf + "]</a>";
					}
					else link = FOL_IMG + "[" + buf + "]";
				}
				else if (entry[i].isFile()){ //Entry is file
					totalSize = totalSize + entry[i].length();
					fileCount = fileCount + 1;
                                        if (type.endsWith("gif") || type.endsWith("jpg") || type.endsWith("jpeg")) {
                                                //dlink="<table width=100 height=120 border=0 cellpadding=0 cellspacing=0><tr><td align=center valign=middle style='border:1px solid #959595'><a href='#zoom' onclick=\"_open('"+cwdir+"/"+link+"')\" ><img src=\"/backoffice/viewResources"+cwdir+"/"+link+"?width=100&height=100\" border='0' style='border:1px solid #ffffff'></A></td></tr></table>";
                                                dlink="<table width=100 height=120 border=0 cellpadding=0 cellspacing=0><tr><td align=center valign=middle style='border:1px solid #959595'><a href='#zoom' onclick=\"_open('"+cwdir+"/"+link+"')\" ><img src=\""+cwdir+"/"+link+"\" width='100' height='100' border='0' style='border:1px solid #ffffff'></A></td></tr></table>";
                                        }
                                        else if (type.endsWith("swf") ) {
                                                dlink="<table width=100 height=120 border=0 cellpadding=0 cellspacing=0><tr><td align=center valign=middle style='border:1px solid #959595'><object FullScreen='yes' type='application/x-shockwave-flash' data='"+cwdir+"/"+link+"' width='100' height='120'><param name='movie' value='"+cwdir+"/"+link+"' /><param name='scale' value='noborder' /></object> </td></tr></table>";
                                        }
                                        else if (type.endsWith("asf") || type.endsWith("avi") || type.endsWith("mpg")) {
                                                dlink="<table width=100 height=120 border=0 cellpadding=0 cellspacing=0><tr><td align=center valign=middle style='border:1px solid #959595'><object ID='MediaPlayer' CLASSID='CLSID:22D6f312-B0F6-11D0-94AB-0080C74C7E95' STANDBY='Chargement...' type='application/x-oleobject' width='100' height='120'><param name='filename' value='"+cwdir+"/"+link+"' /><param name='autostart' value='false' /><param name='showcontrols' value='true' /><embed src='"+cwdir+"/"+link+"' type='application/x-mplayer2' pluginspage='http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2inf.cab#Version=6,4,7,1112' width='100' height='120' autostart='false' showcontrols='true' ></embed></object></td></tr></table>";
                                        }
                                        else if ("_admin".equals(action))
                                                link = "<a href=\"/backoffice/edx/browserDnld.jsp?f="+cdir+File.separator+link+"\" >"+link+"</a>";
                                        else
                                                link = "<a href='#zoom' onclick=\"_open('"+cwdir+"/"+link+"')\">"+link+"</a>";
                                }
				String date = dateFormat.format(new Date(entry[i].lastModified()));
				out.println("<tr class=\"mouseout\" onmouseup=\"selrow(this, 2)\" " +
						"onmouseover=\"selrow(this, 0);\" onmouseout=\"selrow(this, 1)\">");
				if (entry[i].canRead()){
                                        out.println("<td align=center><input class=button3 type=\"checkbox\" name=\"selfile\" value=\"" + buf
							+ "\" onmousedown=\"dis()\"></td>");
				}
				else{
					out.println("<td align=center><input type=\"checkbox\" name=\"selfile\" disabled></td>");
				}
				out.print("<td align=left> &nbsp;" + link + "</td>");
				if (entry[i].isDirectory()) out.print("<td>&nbsp;</td>");
				else{
					out.print("<td align=right title=\""+entry[i].length()+" bytes\">"
							+convertFileSize(entry[i].length())+"</td>");
				}
				out.println("<td align=\"center\">" + type + "</td><td align=left> &nbsp;" + // The file type (extension)
                                                date + "</td><td >" + // The date the file was created
						dlink + "</td></tr>"); // The edit link (or view, depending)
			}
		}%>
	</table>
        </div>
        <hr>
        <script language=javascript>
        function go(t,v) {
                if (!v) {
                        alert("Veuillez saisir une valeur.");
                        return ;
                }
                var lcTest = /[^a-zA-Z\.0-9\_]/g;
                if ((lcTest.test(v)))
                {
                        alert("Veuillez saisir une valeur ne contenant que des lettres alphanumériques ou un underscore.");
                        return ;
                }
                if (confirm('Voulez-vous exécuter cette opération ?')) {
                        document.FileList.Submit.value=t;
                        document.FileList.submit() ;
                }
        }
        function goU() {
                v =document.upFile.myFile.value;
                if (!v) {
                        alert("Veuillez choisir un fichier à charger.");
                        return ;
                }
                var liste = v.split('\\');
                var lcTest = /[^a-zA-Z\.0-9\_]/g;
                if ((lcTest.test(liste[liste.length-1])))
                {
                        alert("Veuillez choisir un nom fichier ne contenant que des lettres alphanumériques ou un underscore.");
                        return ;
                }
                if (confirm('Voulez-vous exécuter cette opération ?')) {
                        document.upFile.submit() ;
                }
        }
        </script>
        <table width="100%" cellpadding="4" cellspacing="4" style="border:1px solid black;background-color:#F5F5F5">
        <tr>
        <td align=right valign="top">
        Créer le répertoire nommé :&nbsp;<input type="text" name="cr_dir" style="width:100px">&nbsp;<input type='image' align="absmiddle" style='border:0px;background-color:#F5F5F5' src='/backoffice/images/valide.gif' border='0' onclick="go('<%=CREATE_DIR%>',document.FileList.cr_dir.value);"><br>
        Renommer l'élement selectionné en :&nbsp;<input type="text" name="cr_rn" style="width:100px">&nbsp;<input type='image' align="absmiddle" style='border:0px;background-color:#F5F5F5' src='/backoffice/images/valide.gif' border='0' onclick="go('<%=RENAME_FILE%>',document.FileList.cr_rn.value);"><br>
        Déplacer les élements selectionnés vers le répertoire nommé :&nbsp;<input type="text" name="cr_mv" style="width:100px">&nbsp;<input type='image' align="absmiddle" style='border:0px;background-color:#F5F5F5' src='/backoffice/images/valide.gif' border='0' onclick="go('<%=MOVE_FILES%>',document.FileList.cr_mv.value);"><br>
        Copier les élements selectionnés vers le répertoire nommé :&nbsp;<input type="text" name="cr_cp" style="width:100px">&nbsp;<input type='image' align="absmiddle" style='border:0px;background-color:#F5F5F5' src='/backoffice/images/valide.gif' border='0' onclick="go('<%=COPY_FILES%>',document.FileList.cr_cp.value);">
        <input type=hidden name=Submit id=Submit value="">
        </td>
        <td align=right valign="top">
        Supprimer les élements selectionnés&nbsp;<input type='image' align="absmiddle" style='border:0px;background-color:#F5F5F5' src='/backoffice/images/valide.gif' border='0' onclick="go('<%=DELETE_FILES%>','lc');"><br>
        Réactualiser le répertoire courant&nbsp;<input type='image' align="absmiddle" style='border:0px;background-color:#F5F5F5' src='/backoffice/images/valide.gif' border='0' onclick="go('','lc');">
        </form>
        <form id="upFile" name="upFile" action="/backoffice/edx/browser.jsp" enctype="multipart/form-data" method="POST" onsubmit="return false">
                <input type=hidden name=cdir id=cdir value="<%=cdir%>">
                <input type=hidden name=action id=action value="<%=action%>">
                <input type=hidden name=selectedSite id=selectedSite value="<%=selectedSite%>">
                <input type=hidden name=selectedType id=selectedType value="<%=selectedType%>">
                <input type="hidden" name="sort" value="<%=sortMode%>">
                <input type=hidden name=Submit id=Submit value="Ajouter ce fichier">
                <div style="background-color:#FFFFFF;border:1px solid black;">Ajouter <input type="file" name="myFile" class="button4">
                <input type='image' align="absmiddle" style='border:0px;background-color:#F5F5F5' src='/backoffice/images/valide.gif' border='0'
                        onclick="goU();"></div>
	</form>
        </td></tr>
<%
		//Output message
		if (request.getAttribute("message")!=null){
                        out.println("<tr><td colspan='2' class=\"message\">");
			out.println(request.getAttribute("message"));
                        out.println("</td></tr>");
		}
		//Output error
		if (request.getAttribute("error")!=null){
                        out.println("<tr><td colspan='2' class=\"error\">");
			out.println(request.getAttribute("error"));
                        out.println("</td></tr>");
		}
%>
        </table>
</body>
</html><%
	}
%>
<%
}catch(Exception bE) {
	System.out.println("12");
	System.out.println("Error : "+bE.getClass().getName()+ bE.getMessage());
	System.out.println("Error >>> " +  bE.getMessage());
}
%>
