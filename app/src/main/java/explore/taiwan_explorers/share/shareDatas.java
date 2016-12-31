package explore.taiwan_explorers.share;

/**
 * Created by no_name on 2017/1/1.
 */

public class shareDatas {
    public String title;
    public String uploader;
    public String flagTitle;
    public String flagContext;
    public double latitude;
    public double longtitude;
    public String diary;
    public String picture;

    public shareDatas(){
        super();
    }

    public shareDatas(String title,String uploader,String flagTitle,String flagContext,
                      double latitude,double longtitude,String diary,String picture) {
        super();
        this.title = title;
        this.uploader = uploader;
        this.flagTitle = flagTitle;
        this.flagContext = flagContext;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.diary = diary;
        this.picture = picture;
    }
}
