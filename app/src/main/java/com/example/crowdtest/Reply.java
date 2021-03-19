package com.example.crowdtest;

/**
 * Reply class
 */
public class Reply extends Comment {

    // Reply attributes
    private String parentID;

    /**
     * Reply constructor
     * @param replyID
     *  Unique ID of reply
     * @param commenterID
     *  Unique ID of experimenter who created the reply
     * @param content
     *  Content of reply
     */
    public Reply(String replyID, String parentID, String commenterID, String content) {
        super(replyID, commenterID, content);
        this.parentID = parentID;
    }

    /**
     * Function for getting the ID of the parent question
     * @return
     *  Unique ID of parent question
     */
    public String getParentID() {
        return parentID;
    }

    /**
     * Function for setting the ID of the parent question
     * @param parentID
     *  Unique ID of parent question
     */
    public void setParentID(String parentID) {
        this.parentID = parentID;
    }
}
