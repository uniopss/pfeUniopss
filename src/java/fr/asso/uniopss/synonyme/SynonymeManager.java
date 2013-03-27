package fr.asso.uniopss.synonyme;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.FastArrayList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.nextime.ion.framework.xml.XML;
import org.nextime.ion.framework.xml.XMLException;

/**
 * 	Gestionnaire de synonymes
 * 	Permet de recharcher le fichier xml contenant des synonymes dans le cas o�
 * 	le fichier a �t� chang� ( par rapport � la date de modification et � la taille du fichier
 *
 * @author q.nguyen
 *
 */
public class SynonymeManager {

    private List _synonymes; // une liste des synonymes

    private long _lastModified = 0;
    private String _fileName = StringUtils.EMPTY;

	public SynonymeManager() {
    	_synonymes=new FastArrayList();

	}

	/**
	 * 	D�termine si on a besoin de recharger le fichier
	 * @param p_file
	 * @return
	 */
    private boolean needRefreshFile(File p_file) {
    	if (! _fileName.equalsIgnoreCase(p_file.getAbsolutePath() )
    			|| _lastModified != p_file.lastModified()) {
    		return true;
    	}
    	return false;
    }

    /**
     *	Mettre � jour le nom du fichier et recharger si besoin.
     * @param p_FileName
     * @throws IOException
     * @throws XMLException
     */
    public void setFileName(String p_FileName) throws IOException, XMLException {
    	File file = new File(p_FileName);
    	if (!needRefreshFile(file)) {
    		return;
    	}

    	// sinon, on re parse le fichier
    	parse(file);

    	// et on m�morise la taille
    	_fileName=file.getAbsolutePath();
    	_lastModified=file.lastModified();

    }


    /**
     * 	Parser le fichier afin de constuire en interne des mot-cl�s synonymes
     * @param p_File
     * @throws IOException
     * @throws XMLException
     */
    private void parse(File p_File) throws IOException, XMLException {
    	_synonymes=new FastArrayList();

    	String content=FileUtils.readFileToString(p_File);
    	Document document=XML.getInstance().getDocument(content);
    	List l=document.getContent();
    	Element root=(Element)l.get(0);
    	List listSynonymes=root.getChildren();

    	// pour chaque synonyme, opn construrie une liste et ajoute dans la liste finale
    	for (Iterator it=listSynonymes.iterator(); it.hasNext();  ) {
    		Element synonymes = (Element)it.next();
    		List keyWordsList=synonymes.getChildren();
    		SynonymeEntry synonyme=new SynonymeEntry();
    		for (Iterator eachSynonyme=keyWordsList.iterator(); eachSynonyme.hasNext();  ) {
        		Element synonymeElement = (Element)eachSynonyme.next();
        		synonyme.add(synonymeElement.getText());
        	}
    		_synonymes.add(synonyme);
    	}
    	print();
    }


    /**
     * 	Recherche un ensemble des mot-cl�s selon me mot-cl� entr� comme param�tre
     * @param p_keyWordToFind
     * @return
     * @throws IOException
     * @throws XMLException
     */
    public Set findKeyWords(String p_keyWordToFind) throws IOException, XMLException {
    	Set  ret= new HashSet();
    	for (Iterator it=_synonymes.iterator(); it.hasNext();  ) {
    		SynonymeEntry entry =(SynonymeEntry)it.next();
    		if (entry.isMatchKeywords(p_keyWordToFind)) {
    			ret.addAll(entry.getKetWords());
    		}
    	}
    	return ret;
    }
    /**
     * 	Rercherche un ensemble des mot-cl�s par rapport � un ensemble de mot-cl�s entr�
     * @param p_ListKeyWords
     * @return
     * @throws IOException
     * @throws XMLException
     */
    public Set findKeyWords(Set p_ListKeyWords) throws IOException, XMLException {
    	Set ret= new HashSet();
    	for (Iterator it=p_ListKeyWords.iterator(); it.hasNext();  ) {
    		String keyword=(String)it.next();
    		Set equivalences = findKeyWords(keyword);
    		ret.addAll(equivalences);
    	}
    	return ret;
    }

    private void print() {
    	for (Iterator it=_synonymes.iterator(); it.hasNext();  ) {
    		SynonymeEntry keyWordsList=(SynonymeEntry)it.next();

    	}
    }
}
