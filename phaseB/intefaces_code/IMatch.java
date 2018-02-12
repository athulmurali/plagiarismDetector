package plagiarism.detector;


import java.util.List;

public interface IMatch {
    /**
     *
     * @return the type of match <br>
     *     where, the value should be one of the enum values (MatchType)
     */
    MatchType           returnMatchType();              // represents the type of match

    /**
     *
     * @return a MatchMetaData object, that represents the line number and column number of the match found
     * in the other file, <br>
     *     source refers to the file given for detection
     *     compared with refers to the file against which the source is compared with
     */
    MatchMetaData       returnSourceMetadata();         // represents the metadata for the source in match

    /** returnComparedWithMetadata
     *
     * @return a List<MatchMetaData> obj, that contains a list of metadata of the files compared with
     */
    List<MatchMetaData> returnComparedWithMetadata();   // represents the metadata for compared with in match
}
