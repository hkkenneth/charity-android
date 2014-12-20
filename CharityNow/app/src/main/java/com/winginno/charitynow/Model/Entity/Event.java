package com.winginno.charitynow;

import java.util.Date;

public class Event {
    private Date Start_Date;
    private Date End_Date;
    private String Headline;
    private String Text;
    private String Media;
    private String Media_Caption;
    private String Tag;
    private String Membership;

    public Date getStartDate()
    {
        return Start_Date;
    }

    public Date getEndDate()
    {
        return End_Date;
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
}
