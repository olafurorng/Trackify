package dk.dtu.trackify.trackify.entities;

import android.support.annotation.NonNull;

import java.util.Comparator;

public class AppUsages implements Comparable {

    private String mPackageName;

    private long mBeginTimeStamp;

    private long mEndTimeStamp;

    private long mLastTimeUsed;

    private long mTotalTimeInForeground;

    public AppUsages(String mPackageName, long mBeginTimeStamp, long mEndTimeStamp, long mLastTimeUsed, long mTotalTimeInForeground) {
        this.mPackageName = mPackageName;
        this.mBeginTimeStamp = mBeginTimeStamp;
        this.mEndTimeStamp = mEndTimeStamp;
        this.mLastTimeUsed = mLastTimeUsed;
        this.mTotalTimeInForeground = mTotalTimeInForeground;
    }

    @Override
    public String toString() {
        return "{" +
                "\"mPackageName\":\"" + mPackageName + '\"' +
                ", \"mBeginTimeStamp\":" + mBeginTimeStamp +
                ", \"mEndTimeStamp\":" + mEndTimeStamp +
                ", \"mLastTimeUsed\":" + mLastTimeUsed  +
                ", \"mTotalTimeInForeground\":" + mTotalTimeInForeground + "," +
                '}';
    }

    public String getmPackageName() {
        return mPackageName;
    }

    public long getmBeginTimeStamp() {
        return mBeginTimeStamp;
    }

    public long getmEndTimeStamp() {
        return mEndTimeStamp;
    }

    public long getmLastTimeUsed() {
        return mLastTimeUsed;
    }

    public long getmTotalTimeInForeground() {
        return mTotalTimeInForeground;
    }

    public void setmTotalTimeInForeground(long mTotalTimeInForeground) {
        this.mTotalTimeInForeground = mTotalTimeInForeground;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        AppUsages other = (AppUsages) o;
        return (int) (other.mTotalTimeInForeground - mTotalTimeInForeground);
    }
}
