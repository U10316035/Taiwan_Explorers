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
            row.setTag(holder);
        }
        else
        {
            holder = (shareListHolder)row.getTag();
        }

        shareDatas shareDatas = dataGroup.get(position);
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
                new AlertDialog.Builder(getContext())
                        .setTitle("旗標")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });
        holder.shareTitle.setText(shareDatas.title);
        holder.shareContext.setText(shareDatas.diary);//.setImageResource(shareDatas.title);
        holder.shareUploader.setText(shareDatas.uploader);

        return row;
    }

    static class shareListHolder
    {
        ImageView sharePic;
        TextView shareTitle;
        TextView shareContext;
        TextView shareUploader;
        ImageView shareFlag;
    }
}
