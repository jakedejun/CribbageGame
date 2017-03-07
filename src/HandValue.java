/** This class provides the methods to compute 5 cards inputs on the command line
 * in the game of Cribbage. and will print out only the number of points the hand 
 * comprising the first four of those card would score if the fifth card is the start card. 
 * It also provides a main method for experimentation. 
 * 
 *@author Dejun Xie dejunx@student.unimelb.edu.au StudentID:826419
 *@author Peter Schachte schachte@unimelb.edu.au
 *
 */

import java.util.Arrays;

public class HandValue {
    
    private String[] cards;
    private int totalScore;
    private String[][] combos;
    
    /** This is a main method to run the whole class, It simply prints out the scores
     * of the five cards input.
     * The 5 cards inputs on the command line should be two-character strings, the first 
     * being a Upper-case A for Ace, K for King, Q for Queen, J for Jack, T for Ten, or digit
     * between 2 to 9. The second character should be C for clubs, D for Diamond, H for Hearts
     * or a S for Spades.
     */  
    
    public static void main(String[] args) {           
        HandValue hand = new HandValue(args);
        System.out.println(hand.getHandValue());        
    }
    
    /**construct the cards on hand.
     * 
     * @param cards the cards input on the command line.
     */    
    HandValue(String[] cards) {
        this.cards = cards;
        totalScore = 0;
        combos = Combinations.combinations(cards);
    }
    
    /**Compute the nobs for the cards on hand.
     * @return nvalue, the scores getting from "one for his nob"
     **/
    private int nobValue() {
        int numberOfCards= cards.length;
        int nValue = 0;
        
        for (int i = 0; i < numberOfCards - 1; i++) {
            char cardRank = cards[i].charAt(0);
            char cardSuit = cards[i].charAt(1);
            char startSuit = cards[numberOfCards - 1].charAt(1);
                                   
            if (cardRank=='J'&&cardSuit == startSuit) {
                nValue += 1;
            }
        }        
        return nValue;
    }
    

    /**Compute the scores if there are flush.
     * @return flushValue, the scores getting from "flush"
     */
    private int flushValue() {
        int flushValue = 0;
        if (inSameSuit()) {
            flushValue += 4;
            
            char firstCardSuit = cards[0].charAt(1);
            char StartCardSuit = cards[cards.length-1].charAt(1);
            
            if(firstCardSuit==StartCardSuit){
                flushValue +=1;
            }
        }        
        return flushValue;
    }
    
    /**
     * check whether the player's cards are in same suit
     * @return same - true; different - false
     */
    private boolean inSameSuit() {
        for (int i = 0; i < cards.length-2; i++) {
            if (cards[i].charAt(1) != cards[i + 1].charAt(1)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**Compute the scores if there are pairs on the hand.
     * @return pValue, the score getting from "pairs"
     */
    private int pairValue() {
        int pValue = 0;
        
        for (int i = 0; i < combos.length; i++) {
            if (combos[i].length == 2 && combos[i][0].charAt(0) == combos[i][1].charAt(0)) {
                pValue += 2;
            }
        }
        
        return pValue;
    }
    
    
    /**Compute the scores if there are 15s on the hand.
     * @return fiveValue,the value getting from "15s"
     */
    private int fifteenValue() {
        int fiveValue = 0;
        Integer[] faceValue = new Integer[cards.length];
        Integer[][] combos;
        
        for (int i = 0; i < cards.length; i++) {
            for (CribbageRank card : CribbageRank.values()) {
                if (cards[i].charAt(0) == card.abbrev()) {
                    faceValue[i] = card.faceValue();
                    break;
                }
            }
        }
        
        /** For each combinations of the cards, check whether the sum is
        *   equal to 15.
        */
        combos = Combinations.combinations(faceValue);
        for (int i = 0; i < combos.length; i++) {
            int sum = 0;
            for (int j = 0; j < combos[i].length; j++) {
                sum += combos[i][j];
            }
            
            if (sum == 15) {
                fiveValue += 2;
            }
        }     
        return fiveValue;
    }
    
    
    /**Compute the scores if there are runs on the hand.
     * @return the value getting from "runs"
     */
    private int runValue() {
        int rValue = 0;
        int maxLength = 0;
        
        CribbageRank[] rank = new CribbageRank[cards.length];
        CribbageRank[][] combos;
        
        for (int i = 0; i < cards.length; i++) {
            for (CribbageRank card : CribbageRank.values()) {
                if (cards[i].charAt(0) == card.abbrev()) {
                    rank[i] = card;
                }
            }
        }
        
        /** Find the maximum length for the combination which can form a run.
        */
        combos = Combinations.combinations(rank);
        
        for (int i = 0; i < combos.length; i++) {
            Arrays.sort(combos[i]);         
          if(combos[i].length>=3 && isRun(combos[i])){
              if (combos[i].length > maxLength) {
                  maxLength = combos[i].length;
                  }
            }            
        }        

        /** Add up the scores if the maximum length combination is a run.
        */
        for (int i = 0; i < combos.length; i++) {
            if (combos[i].length == maxLength && isRun(combos[i])) {
                rValue += combos[i].length;
            }
        }        
        return rValue;
    }
    
    /**
     * this method checks whether a combination is a run
     * @param combo, all the combinations of the cards on the hand.
     * @return true - there is a run, false - this is not a run.
     */
    private boolean isRun(CribbageRank[] combo) {
        for (int i = 1; i < combo.length; i++) {
            if (!combo[i - 1].nextHigher().equals(combo[i])) {
                return false;
            }
        }
        return true;
    }
    
    /**This method is to calculate all the scores from different combinations
     * 
     * @return totalScore, the total score of the cards on the hand.
     */
    
    public int getHandValue() {
        
        totalScore += nobValue();
        totalScore += flushValue();
        totalScore += pairValue();
        totalScore += fifteenValue();
        totalScore += runValue();
        
        return totalScore;
    }
    
}
