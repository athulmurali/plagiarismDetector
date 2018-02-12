package plagiarism.detector;

import java.util.List;

public interface IPlagiarismCore {

    /** returnRenamedDatatypesMatches
     *
     * @return a List of Match type,
     * <br> where, each Match represents
     *
     * @see  Match
     */
    List<Match> returnRenamedDatatypesMatches();

    /** returnFunctionizedMatches
     *
     * @return a List of matches after scanning for codes moved into functions and vice versa
     */
    List<Match> returnFunctionizedMatches();


    /** returnRenamedCodeMatches
     *
     * @return a List<Match>  obj that contains a list of matches where,datatypes and functions renamed
     */
    List<Match> returnRenamedCodeMatches();


    /** returnExactMatches
     *
     * @return a List<Match> obj that contains a list of matches where ,the code is exactly similar in
     * the source and
     */

    List<Match> returnExactMatches();

}
