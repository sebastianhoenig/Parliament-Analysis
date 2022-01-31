package data.impl;

import data.Comment;

public class CommentFile_Impl implements Comment {
    private String id;
    private String text;
    private String speechID;

    public void setText(String text){
//        System.out.println("KOMMENTARTEXT: "+text);
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public String getCommentID() {
        return id;
    }

    public void setCommentID(String id) {
        if (id.contains("--")) {
            id = id.replace("--", "-");
        }
        this.id = id;
    }

    public int hashCode() {
        return this.getText().hashCode();
    }

    public String getSpeech() {
        return speechID;
    }

    public void setSpeech(String speechID) {
        this.speechID = speechID;
    }
}
