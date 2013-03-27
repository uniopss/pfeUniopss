package fr.asso.uniopss.synonyme;


/**
 * 	Service d'instanciation le gestionnaire des mot-clés
 * @author q.nguyen
 *
 */
public class SynonymeService {

    private static SynonymeService _instance;

    private SynonymeManager _synonymes;

    public static SynonymeService getInstance() {
        if ( _instance==null ) {
            synchronized ( SynonymeService.class ) {
                if ( _instance==null ) {
                    _instance=new SynonymeService();
                }
            }
        }
        return _instance;
    }

    private SynonymeService() {
    	_synonymes=new SynonymeManager();
    }

	public SynonymeManager getSynonymes() {
		return _synonymes;
	}


}
