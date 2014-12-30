package com.winginno.charitynow;

import java.util.Date;

import java.text.SimpleDateFormat;
import java.util.Locale;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Event implements ListItemInterface {
    private Date Start_Date;
    private Date End_Date;
    private String Headline;
    private String Text;
    private String Media;
    private String Media_Caption;
    private String Tag;
    private String Membership;
    private String Recruit_Phone;
    private String Recruit_Web;
    private String Id;

    public Date getStartDate()
    {
        return Start_Date;
    }

    public Date getEndDate()
    {
        return End_Date;
    }

    public String getStartDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(Start_Date);
    }

    public String getName()
    {
        return Headline;
    }

    public String getDescription()
    {
        return Text;
    }

    public String getImageUrl()
    {
        return Media;
    }

    public String getWebsiteUrl()
    {
        return Media_Caption;
    }

    public String getRegion()
    {
        return Tag;
    }

    public String getMembership()
    {
        return Membership;
    }

    public List<String> getMemberships() {
        if (Membership.isEmpty()) {
            return new ArrayList<String>();
        }
        return Arrays.asList(Membership.split(","));
    }

    public boolean isSectionHeader() {
        return false;
    }

    public String getRecruitPhone()
    {
        return Recruit_Phone;
    }

    public String getRecruitWeb()
    {
        return Recruit_Web;
    }

    public String getId()
    {
        return Id;
    }

    public boolean isInKowloon()
    {
        return (getRegion().equals("九龍")) || (getRegion().equals("全港各處"));
    }

    public boolean isInHongKong()
    {
        return (getRegion().equals("香港島")) || (getRegion().equals("全港各處"));
    }

    public boolean isInNt()
    {
        return (getRegion().equals("新界及離島")) || (getRegion().equals("全港各處"));
    }

    public boolean isFinished()
    {
        if (getEndDate() == null) {
            return true;
        }
        return getEndDate().before(new Date());
    }
}
