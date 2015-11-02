import java.io.IOException;
import java.io.*;
import java.util.*;


public class QryIopNear extends QryIop {
	
	private int distance;
	
	public QryIopNear(int distance) {
		this.distance = distance;
	}
	// Constructor for #NEAR/distance
	
//	@Override
//	public boolean docIteratorHasMatch(RetrievalModel r) {
//		// TODO Auto-generated method stub
//		return false;
//	}

//	@Override
//	public void initialize(RetrievalModel r) throws IOException {
//		// TODO Auto-generated method stub
//
//	}
	
	 /**
	   *  Evaluate the query operator; the result is an internal inverted
	   *  list that may be accessed via the internal iterators.
	   *  @throws IOException Error accessing the Lucene index.
	   */
	 protected void evaluate () throws IOException {
		 
		 this.invertedList = new InvList (this.getField());
		 
	     if (this.args.size () == 0) {
	        return;
	     }
	     
	     Qry q_0 = this.args.get(0);
	     int docid_0 = q_0.docIteratorGetMatch ();
	     // Find a common docid
	     while(q_0.docIteratorHasMatch(null)) {
	    	 boolean matchFound = false;
	    	 
	    	 while (! matchFound) {
	    		 if (! q_0.docIteratorHasMatch(null)) {
	    			 break;
	    		 }
	    	     docid_0 = q_0.docIteratorGetMatch ();
		    	 for(int i = 1 ; i < this.args.size(); i++) {
		    		 Qry q_i = this.args.get(i);
		    		 q_i.docIteratorAdvanceTo (docid_0);
		    		 
		    		 if(! q_i.docIteratorHasMatch(null)) {
		    			 return;
		    		 }
		    		 
		    		 int docid_i = q_i.docIteratorGetMatch ();
		    		 if (docid_0 != docid_i) {	
	    				 q_0.docIteratorAdvanceTo (docid_i);
	    				 break;
	    			 }else {
	    				 matchFound = true;
	    			 }	    			  
		    	 }		    	 
	    	 }
	    	 
	    	 // Use locIterator to match location
			 Vector<Integer> position = new Vector<Integer>();
			 boolean finish = false;
			 q_0.docIteratorAdvanceTo(docid_0);
			 while (! finish && matchFound) {	
				 for(int i = 0 ; i < this.args.size() - 1 ; i++) {
					 QryIop q_1 = this.getArg(i);
					 QryIop q_2 = this.getArg(i+1);
					 if (! (q_1.locIteratorHasMatch() && q_2.locIteratorHasMatch())) {
						 finish = true;
						 break;
					 } // No more match because the shortest location list is over
					 
					 int curDistance = q_2.locIteratorGetMatch() - q_1.locIteratorGetMatch();
					 if (curDistance > 0 && curDistance <= this.distance) {
						 continue;
					 } // The distance satisfy the query
					 if (curDistance <= 0) {
						 q_2.locIteratorAdvance();
						 i--;
						 continue;
					 } // Need to advance the second location
					 if (curDistance > this.distance) {
						 q_1.locIteratorAdvance();
						 i = -1;
						 continue;
					 } // Need to advance the first location
				 }
				if(finish) 	break; // No more match
				
				position.add(this.getArg(this.args.size() - 1).locIteratorGetMatch()); //Append the position list
				
				for(int i = 0 ; i < this.args.size() ; i++) {
					//this.getArg(i).locIteratorAdvancePast(this.getArg(this.args.size()-1).locIteratorGetMatch());
					this.getArg(i).locIteratorAdvance();
				} // Once match, advance all the location 
				
			 }
			 if (position.size() > 0) {
				 this.invertedList.appendPosting(docid_0, position); // Append the docID list
			 }

	    	 q_0.docIteratorAdvancePast(docid_0);
	    	 

	     }

		 return;
	 
	     
	 }
}
