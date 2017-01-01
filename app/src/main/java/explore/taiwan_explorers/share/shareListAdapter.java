package explore.taiwan_explorers.share;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import explore.taiwan_explorers.MainActivity;
import explore.taiwan_explorers.R;

/**
 * Created by no_name on 2017/1/1.
 */

public class shareListAdapter extends ArrayAdapter<shareDatas> {

    Context context;
    int layoutResourceId;
    //shareDatas data[] = null;
    share_fragment fragment;
    ArrayList<shareDatas> dataGroup;

    public shareListAdapter(Context context, int layoutResourceId, ArrayList<shareDatas> dataGroup, share_fragment fragment) {
        super(context, layoutResourceId, dataGroup);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.dataGroup = dataGroup;
        this.fragment = fragment;
    }

    @Override
    public View getView(int position,final View convertView, ViewGroup parent) {
        View row = convertView;
        shareListHolder holder = null;


        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new shareListHolder();
            holder.sharePic = (ImageView)row.findViewById(R.id.imageViewSharePicure);
            holder.shareTitle = (TextView)row.findViewById(R.id.textViewShareTitle);
            holder.shareContext = (TextView)row.findViewById(R.id.textViewShareContext);
            holder.shareUploader = (TextView)row.findViewById(R.id.textViewUploader);
            holder.shareFlag = (ImageView)row.findViewById(R.id.imageViewShareFlag);
            holder.shareTime = (TextView)row.findViewById(R.id.textViewTime);
            row.setTag(holder);
        }
        else
        {
            holder = (shareListHolder)row.getTag();
        }

        final shareDatas shareDatas = dataGroup.get(position);
        holder.sharePic.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                fragment.enlargePIC();

                /*new AlertDialog.Builder(getContext())
                        .setTitle("圖片")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();*/
            }
        });

        holder.shareFlag.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                List<String> option;
                option = new ArrayList<>();
                option.add("資訊");
                option.add("跳轉地圖");

                new AlertDialog.Builder(fragment.getActivity()).setItems(option.toArray(new String[option.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                new AlertDialog.Builder(getContext())
                                        .setTitle("旗標")
                                        .setMessage("標題 : " + shareDatas.flagTitle + "\n" + "描述 : " + shareDatas.flagContext + "\n" + "經度 : " +  shareDatas.latitude + "\n" + "緯度 : " + shareDatas.longtitude)
                                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .show();
                                break;
                            case 1:
                                ((MainActivity)fragment.getActivity()).searchFlagFragment(shareDatas.latitude,shareDatas.longtitude,shareDatas.flagTitle,shareDatas.flagContext,1,1);
                                break;
                        }

                    }
                }).show();
            }
        });

        holder.shareTitle.setText(shareDatas.title);
        String[] beforeNewline = shareDatas.diary.split("\n", 20);
        holder.shareContext.setText(beforeNewline[0]);//.setImageResource(shareDatas.title);
        holder.shareUploader.setText(shareDatas.uploader);
        holder.shareTime.setText(timeString(shareDatas.time));

        return row;
    }

    static class shareListHolder
    {
        ImageView sharePic;
        TextView shareTitle;
        TextView shareContext;
        TextView shareUploader;
        ImageView shareFlag;
        TextView shareTime;
    }

    private String timeString(String s){
        String s1 = s.substring(0,4);
        String s2 = s.substring(4,6);
        String s3 = s.substring(6,8);
        String s4 = s.substring(8,10);
        String s5 = s.substring(10,12);
        String s6 = s.substring(12,14);
        String re = s1 + "/" + s2 + "/" + s3 + " " + s4 + ":" + s5 + ":" + s6;
        return re;

    }
}
