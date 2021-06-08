package com.ecell.end_eavour.events.humor;

public class Voting_Model {

    public String teamName;
    public String image;
    public int totalVotes;
    public String voteId;

    public Voting_Model() {
    }

    public Voting_Model(String teamName, String image, int totalVotes, String voteId) {
        this.teamName = teamName;
        this.image = image;
        this.totalVotes = totalVotes;
        this.voteId = voteId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }

    public String getVoteId() {
        return voteId;
    }

    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }
}
