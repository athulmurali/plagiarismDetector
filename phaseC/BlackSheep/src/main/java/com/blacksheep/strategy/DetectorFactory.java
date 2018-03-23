package com.blacksheep.strategy;

/**
 * factory class for creating different detector
 */
public class DetectorFactory {

    /**
     * @return CodeMoveDetector
     */
    public Plagiarism createCodeMoveDetector() {
        return new CodeMoveDetector();
    }

    /**
     * @return CommentSimilarityDetecor()
     */
    public Plagiarism createCommentSimilarityDetecor() {
        return new CommentSimilarityDetector();
    }

    /**
     * @return CRCDetecor()
     */
    public Plagiarism createCRCDetecor() {
        return new CRCDetector();
    }

    /**
     * @return VariableChangeDetecor()
     */
    public Plagiarism createVariableChangeDetecor() {
        return new VariableChangeDetector();
    }
}
