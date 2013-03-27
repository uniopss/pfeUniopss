package org.nextime.ion.framework.helper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.logger.Logger;

/**
 *	Renvoie une instance de IndexSearcher pour toute application
 *	car la classe IndexSearcher est thread-safe.
 *		elle base sur la denière modification de l'index afin de se recharger si nécéssaire
 * @author q.nguyen
 */
public class IndexSearcherFactory {

    private static IndexSearcherFactory _instance=new IndexSearcherFactory();

    private HashMap<String, IndexSearcher> _indexMap;

    private long _lastModified = 0;

    public static IndexSearcherFactory getInstance() {
        return _instance;
    }

    /**
     * Constructeur privé
     */
    private IndexSearcherFactory() {
        _indexMap=new HashMap<String, IndexSearcher>();
    }


    /**
     * 	Récupération de IndexSearcher par rapport à la base d'indexation : en, fr ...
     * @param p_IndexName : le nom de la base d'indexation
     * @return IndexSearcher
     * @throws IOException
     * @throws CorruptIndexException
     */
    public IndexSearcher getIndexSearcher(String p_IndexName) throws IOException, CorruptIndexException {

    	IndexSearcher searcher = null;

    	File directory = new  File(Config.getInstance().getIndexRoot(), p_IndexName);

    	if (_indexMap.get(p_IndexName) !=null) {
    		searcher  = _indexMap.get(p_IndexName);
        	// test la dernière modif
        	long lastModified = IndexReader.lastModified(directory);

        	// dans le cas ou ce n'est pas le même valeur, alors on reouvre Index
        	if (lastModified != _lastModified) {
        		try {
        			IndexReader reader = searcher.getIndexReader();
        			searcher.close();
        			reader.close();
        			reader=null;
        		} catch (IOException e) {
        			Logger.getInstance().error("Error de fermeture reader ", IndexSearcherFactory.class, e);
        		} finally {
        			searcher=null;
        		}
        	}
        }

    	if (searcher==null) {
    		long lastModified = IndexReader.lastModified(directory);
    		// on reouvre avec un autre
            IndexReader reader = IndexReader.open(directory);
            searcher =  new IndexSearcher(reader);
            // on renseigne des valeurs
            _lastModified = lastModified;
            _indexMap.put(p_IndexName, searcher);
    	}
    	return searcher;

    }


}
