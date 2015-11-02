import java.io.IOException;

public class QrySopAnd extends QrySop {

	@Override
	public boolean docIteratorHasMatch(RetrievalModel r) {
		// TODO Auto-generated method stub
		return this.docIteratorHasMatchAll (r);
	}

	
	  public double getScore (RetrievalModel r) throws IOException {

		    if (r instanceof RetrievalModelUnrankedBoolean) {
		      return this.getScoreUnrankedBoolean (r);
		    } else if(r instanceof RetrievalModelRankedBoolean) {
		      return this.getScoreRankedBoolean (r);	
		    } else {
		      throw new IllegalArgumentException
		        (r.getClass().getName() + " doesn't support the OR operator.");
		    }
		  }
		  
		  /**
		   *  getScore for the UnrankedBoolean retrieval model.
		   *  @param r The retrieval model that determines how scores are calculated.
		   *  @return The document score.
		   *  @throws IOException Error accessing the Lucene index
		   */
		  private double getScoreUnrankedBoolean (RetrievalModel r) throws IOException {
		    if (! this.docIteratorHasMatchCache()) {
		      return 0.0;
		    } else {
		      return 1.0;
		    }
		  }
		  
		  /**
		   *  getScore for the RankedBoolean retrieval model.
		   *  @param r The retrieval model that determines how scores are calculated.
		   *  @return The document score.
		   *  @throws IOException Error accessing the Lucene index
		   */
		  private double getScoreRankedBoolean (RetrievalModel r) throws IOException {
		    if (! this.docIteratorHasMatchCache()) {
		      return 0.0;
		    } else {
		        int docid = this.docIteratorGetMatch();
		        double minScore = Integer.MAX_VALUE;
		        for(int i = 0 ; i < this.args.size() ; i++) {
		          Qry q_i = this.args.get(i);
		    	  if(q_i.docIteratorHasMatch(r)) {
			    	  int q_iDocid = q_i.docIteratorGetMatch();
			    	  if(q_iDocid == docid) {
				    	  double score = ((QrySop) q_i).getScore(r);
				    	  minScore = Math.min(minScore, score);
			    	  }
		    	  }

		        }
		        return minScore;
		    }
		  }

}
