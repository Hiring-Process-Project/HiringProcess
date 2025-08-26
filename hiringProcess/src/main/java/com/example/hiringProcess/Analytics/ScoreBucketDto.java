package com.example.hiringProcess.Analytics;

public class ScoreBucketDto {
    private int from;
    private int to;
    private String range;
    private long count;

    public ScoreBucketDto() {}

    public ScoreBucketDto(int from, int to, long count) {
        this.from = from;
        this.to = to;
        this.range = (to == 100) ? (from + "–100") : (from + "–" + to);
        this.count = count;
    }

    public int getFrom() { return from; }
    public void setFrom(int from) { this.from = from; }

    public int getTo() { return to; }
    public void setTo(int to) { this.to = to; }

    public String getRange() { return range; }
    public void setRange(String range) { this.range = range; }

    public long getCount() { return count; }
    public void setCount(long count) { this.count = count; }
}
