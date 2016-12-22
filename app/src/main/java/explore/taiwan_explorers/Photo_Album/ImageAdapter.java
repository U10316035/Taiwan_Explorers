package explore.taiwan_explorers.Photo_Album;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import org.xmlpull.v1.XmlPullParser;

import java.util.List;

import explore.taiwan_explorers.R;

import static android.support.v7.widget.TintTypedArray.obtainStyledAttributes;

/**
 * Created by no_name on 2016/12/22.
 */
//改寫BaseAdapter自訂一ImageAdapter class
public class ImageAdapter extends BaseAdapter {
    //public Bitmap bit;
    int mGalleryItemBackground;
    private Context mCoNtext;
    private List<String> lis;
    public ImageAdapter(Context c, List<String> li) {
        mCoNtext = c;
        lis = li;
//使用在res/values/aatrs.xml中的<declare-styleable>定義
        TypedArray a = c.obtainStyledAttributes(R.styleable.Gallery);
//取得Gallery屬性
        mGalleryItemBackground = a.getResourceId(
                R.styleable.Gallery_android_galleryItemBackground, 0);
//讓物件的styleable屬性能夠反復使用
        a.recycle();
    }
    //重寫的方法，返回圖片數目
    public int getCount() {
        return lis.size();
    }
    //重寫的方法，返回圖片的陣列id
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    //重寫的方法，返回一View物件
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//產生ImageView物件
        ImageView i = new ImageView(mCoNtext);
//設置圖片給ImageView物件
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 16;
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(lis.get(position).toString(),options);
        //setBitmap(bm);
        i.setImageBitmap(bm);
//重新設置圖片的寬度
        i.setScaleType(ImageView.ScaleType.FIT_XY);
//重新設置Layout的寬度
        i.setLayoutParams(new Gallery.LayoutParams(200, 288));
//設置Gallery背景圖
        i.setBackgroundResource(mGalleryItemBackground);
        return i;
    }

    /*public void setBitmap(Bitmap b) {
        bit = b;
    }

    public Bitmap getBitmap() {
        return bit;
    }*/
}