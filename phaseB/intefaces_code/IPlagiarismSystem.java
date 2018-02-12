package plagiarism.detector;

import java.io.File;


/** Contains the functions for main system interactions with the UI and system IT
 *
 * Note : Plagiarism Settings - Type of Plagiarism to detect
 *
 */
public interface IPlagiarismSystem {

    /** setPlagiarismConfig
     *
     * @return 0 on success ; -1 on failure to set the given parameter
     */

    int setPlagiarismConfig(int confidenceLevel);   // sets the confidence level

    /** setInputFilesToDetect
     *
     * @return 0 on successful return after configuration
     * <br> -1 on unsuccessful return after configuration
     */
    int setInputFilesToDetect(File[] fileArray); //  The function to be called from the UI after file path validation

    /** setInputFilesToDetect
     *
     * @return 0 on successful return after configuration
     * <br> -1 on unsuccessful return after configuration
     */
    int setFilesToCompareWith(File[] fileArray);

    /** returnPlagiarismSettings
     *
     * @return a Map of String, String
     * where each key represents a setting and its value is given as String
     */
    int returnPlagiarismSettings();

    /**
     *
     * @return  an integer,
     * <br> 0 on successful reset to default
     * <br> -1 on any exception during reset
     */
    int resetPlagiarismConfig();    // resets the plagiarism configs to default settings

}
