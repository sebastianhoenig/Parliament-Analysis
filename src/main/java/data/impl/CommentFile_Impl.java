package data.impl;

import data.Comment;

public class CommentFile_Impl implements Comment {
    String text;

    public void setText(String text){
        System.out.println("KOMMENTARTEXT: "+text);
        this.text = text;
    }

    public String getText(){
        return this.text;
    }
}
