package data.impl;

import data.Comment;

/**
 * @author Vanessa Rosenbaum
 * this class implements comments of a speech. A comment has an id, a text and a speechID
 */
public class CommentFile_Impl implements Comment {
    private String id;
    private String text;
    private String speechID;

    /**
     * @param text
     */
    public void setText(String text){
//        System.out.println("KOMMENTARTEXT: "+text);
        this.text = text;
    }

    /**
     * @return text of a comment
     */
    public String getText(){
        return this.text;
    }

    /**
     * @return comment id
     */
    public String getCommentID() {
        return id;
    }

    /**
     * @param id id of a comment
     */
    public void setCommentID(String id) {
        if (id.contains("--")) {
            id = id.replace("--", "-");
        }
        this.id = id;
    }

    /**
     * @return hashcode of a comment
     */
    public int hashCode() {
        return this.getText().hashCode();
    }

    /**
     * @return speech id of a comment
     */
    public String getSpeech() {
        return speechID;
    }

    /**
     * @param speechID 
     */
    public void setSpeech(String speechID) {
        this.speechID = speechID;
    }
}
