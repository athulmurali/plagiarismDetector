package com.blacksheep;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * This class contains the implementation for comparing the syntax tree of source and suspect to get the
 * plagiarism if the variable and methods names have been changed but code order is the same
 */
public class NameChangePlagiarism {

    /**
     * Logger instance
     */
    private final Logger logger = Logger.getLogger(NameChangePlagiarism.class);

    /**
     * Compares two syntax trees and provides the match percentage between the two source codes
     *
     * @param sourceTree  : Syntax tree of the source as a list
     * @param suspectTree : Syntax tree of the suspect as a list
     */
    public void check(List sourceTree, List suspectTree) {

        int sourceSize = sourceTree.size();
        int suspectSize = suspectTree.size();

        double similarityCount = 0;
        double minimumSize;

        double matchPercent;

        if (sourceSize <= suspectSize)
            minimumSize = sourceSize;
        else
            minimumSize = suspectSize;

        for (int i = 0; i < minimumSize; i++) {
            if (sourceTree.get(i).equals(suspectTree.get(i)))
                similarityCount++;
        }

        matchPercent = (similarityCount / minimumSize) * 100;

        logger.info("The match percent is " + matchPercent);
    }
}
