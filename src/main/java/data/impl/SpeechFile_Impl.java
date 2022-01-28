package data.impl;

import data.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class SpeechFile_Impl implements Speech {
    String speechID;
    String text;
    ArrayList<Comment> allComments = new ArrayList<>();
    String speakerID;

    public void initialize(Node kNode){
        NodeList kommentarNodes = kNode.getChildNodes();
        ArrayList<String> textArrayList = new ArrayList<String>();
        for(int i = 0; i < kommentarNodes.getLength(); i++){
            Node kommentarNode = kommentarNodes.item(i);
            if(kommentarNode.getNodeName().equals("kommentar")) {
                String commentText = kommentarNode.getTextContent();
                Comment comment = new CommentFile_Impl();
                this.allComments.add(comment);
                comment.setText(commentText);
            } else if(kommentarNode.getNodeName().equals("p")){
                Element p = (Element) kommentarNodes.item(i);
                if(p.getAttribute("klasse").equals("J")||p.getAttribute("klasse").equals("J_1")||p.getAttribute("klasse").equals("O")){
                    textArrayList.add(kommentarNode.getTextContent());
                }
            }
        }
        this.text = textArrayList.toString();
        System.out.println("REDE ID: "+speechID);
        System.out.println("REDENTEXT: "+text);
    }


    public void setSpeechID(String speechID){
        this.speechID = speechID;
    }

    public String getSpeechID(){
        return this.speechID;
    }

    public void setSpeakerID(String speakerID){
        System.out.println("SPEAKER ID: "+speakerID);
        this.speakerID = speakerID;
    }

    /**
     * @return ID des Redners der Rede
     */
    public String getSpeakerID(){
        return this.speakerID;
    }

    public String getText(){
        return this.text;
    }

    public ArrayList<Comment> getAllComments(){
        return this.allComments;
    }
}
