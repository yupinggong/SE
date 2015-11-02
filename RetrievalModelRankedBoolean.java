/**
 *  An object that stores parameters for the Ranked Boolean
 *  retrieval model (there are none) and indicates to the query
 *  operators how the query should be evaluated.
 */
public class RetrievalModelRankedBoolean extends RetrievalModel {

	@Override
	public String defaultQrySopName() {
		// TODO Auto-generated method stub
		return new String ("#or");
	}

}