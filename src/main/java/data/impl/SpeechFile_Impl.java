package data.impl;

import data.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

public class SpeechFile_Impl implements Speech {
    String speechID;
    String text;
    ArrayList<Comment> allComments = new ArrayList<>();
    String speakerID;
    Protocol protocol;
    AgendaItem agendaItem;


    public SpeechFile_Impl(String speechID, Node kNode, Protocol protocol, AgendaItem agendaItem,
                           String speakerID, HashMap<String, Member> allMembers) {
        this.protocol = setProtocol(protocol);
        this.agendaItem = setAgendaItem(agendaItem);
        this.speechID = setSpeechID(speechID);
        this.speakerID = setSpeakerID(speakerID);
        this.text = setText(kNode, protocol, agendaItem);
        this.allComments = setAllComments(kNode, protocol, agendaItem);
        updateMemberWithSpeech(speakerID, allMembers, speechID);
    }

    String setText(Node kNode, Protocol protocol, AgendaItem agendaItem) {
        String text = "";
        NodeList children = kNode.getChildNodes();
        for (int i = 2; i < children.getLength(); i++) {
            Node child = children.item(i);
            text += child.getTextContent();
        }
        return text;
    }

    ArrayList<Comment> setAllComments(Node kNode, Protocol protocol, AgendaItem agendaItem) {
        ArrayList<Comment> allComments = new ArrayList<>();
        NodeList kommentarNodes = kNode.getChildNodes();
        for(int i = 0; i < kommentarNodes.getLength(); i++){
            Node kommentarNode = kommentarNodes.item(i);
            if(kommentarNode.getNodeName().equals("kommentar")) {
                String commentText = kommentarNode.getTextContent();
                Comment comment = new CommentFile_Impl();
                comment.setText(commentText.substring(1, commentText.length()-1));
                comment.setCommentID(this.speechID + "-" + comment.hashCode());
                allComments.add(comment);
            }
        }
        return allComments;
    }


    public String setSpeechID(String speechID){
        return speechID;
    }

    public String getSpeechID(){
        return this.speechID;
    }

    public String setSpeakerID(String speakerID){
        return speakerID;
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


    public Protocol getProtocol() {
        return this.protocol;
    }


    public Protocol setProtocol(Protocol protocol) {
        return protocol;
    }


    public AgendaItem getAgendaItem() {
        return this.agendaItem;
    }

    public AgendaItem setAgendaItem(AgendaItem agendaItem) {
        return agendaItem;
    }

    public void updateMemberWithSpeech(String speakerID, HashMap<String, Member> allMembers, String speechID) {
        if (allMembers.get(speakerID) != null) {
            allMembers.get(speakerID).addSpeech(speechID);
        }
    }
}
