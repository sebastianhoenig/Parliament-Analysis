package data.impl;

import data.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Vanessa Rosenbaum
 * this class implements speeches of a protocol
 * a speech has an id, text, comments, speaker id, belongs to a protocol and an agenda item and has a plain text
 */
public class SpeechFile_Impl implements Speech {
    String speechID;
    String text;
    ArrayList<Comment> allComments = new ArrayList<>();
    String speakerID;
    Protocol protocol;
    AgendaItem agendaItem;
    String plainText = "";


    public SpeechFile_Impl(String speechID, Node kNode, Protocol protocol, AgendaItem agendaItem,
                           String speakerID, HashMap<String, Member> allMembers) {
        this.protocol = setProtocol(protocol);
        this.agendaItem = setAgendaItem(agendaItem);
        this.speechID = setSpeechID(speechID);
        this.speakerID = setSpeakerID(speakerID);
        this.text = setText(kNode);
        this.allComments = setAllComments(kNode, protocol, agendaItem);
        updateMemberWithSpeech(speakerID, allMembers, speechID);
    }

    /**
     * this function sets the text of a speech
     * @param kNode
     * @return text
     */
    String setText(Node kNode) {
        String text = "";
        NodeList children = kNode.getChildNodes();
        for (int i = 2; i < children.getLength(); i++) {
            Node child = children.item(i);
            String t = child.getTextContent().trim();

            if (child.getNodeName().equals("kommentar")){
                text += " ";
                plainText += " " + t + " ";
                continue;
            } else {
                t = child.getTextContent().trim();
                if (t.endsWith(".") || t.endsWith(":")){
                    t += " ";
                }
            }
            text += t;
            plainText += t;
        }
        return text;
    }

    /**
     * this function initializes all comments of a speech
     * @param kNode
     * @param protocol
     * @param agendaItem
     * @return all comments
     */
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


    /**
     * @param speechID
     * @return speech id
     */
    public String setSpeechID(String speechID){
        return speechID;
    }

    /**
     * @return speech id
     */
    public String getSpeechID(){
        return this.speechID;
    }

    /**
     * @param speakerID
     * @return speaker id
     */
    public String setSpeakerID(String speakerID){
        return speakerID;
    }

    /**
     * @return id of the speaker
     */
    public String getSpeakerID(){
        return this.speakerID;
    }

    /**
     * @return text
     */
    public String getText(){
        return this.text;
    }

    /**
     * @return plain text
     */
    public String getPlainText(){
        return this.plainText;
    }

    /**
     * @return all comments
     */
    public ArrayList<Comment> getAllComments(){
        return this.allComments;
    }


    /**
     * @return the protocol the speech belongs to
     */
    public Protocol getProtocol() {
        return this.protocol;
    }


    /**
     * @param protocol
     * @return protocol the speech belongs to
     */
    public Protocol setProtocol(Protocol protocol) {
        return protocol;
    }


    /**
     * @return agenda item
     */
    public AgendaItem getAgendaItem() {
        return this.agendaItem;
    }

    /**
     * @param agendaItem
     * @return agenda item
     */
    public AgendaItem setAgendaItem(AgendaItem agendaItem) {
        return agendaItem;
    }

    /**
     * @author Sebastian
     * @param speakerID
     * @param allMembers
     * @param speechID
     */
    public void updateMemberWithSpeech(String speakerID, HashMap<String, Member> allMembers, String speechID) {
        if (allMembers.get(speakerID) != null) {
            allMembers.get(speakerID).addSpeech(speechID);
        }
    }
}
