package data;

/**
 * @author Vanessa Rosenbaum
 * interface of a comment
 */
public interface Comment {

    /**
     * gets id of a comment
     * @return
     */
    String getCommentID();

    /**
     * sets if of a comment
     * @param id
     */
    void setCommentID(String id);

    /**
     * gets text of a comment
     * @return
     */
    String getText();

    /**
     * sets text of a comment
     * @param text
     */
    void setText(String text);

    /**
     * gets speech of a comment
     * @return
     */
    String getSpeech();

    /**
     * adds speech id to a comment
     * @param id
     */
    void setSpeech(String id);
}
