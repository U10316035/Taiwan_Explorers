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

import explore.taiwan_explorers.MainActivity;
import explore.taiwan_explorers.R;

/**
 * Created by no_name on 2017/1/1.
 */

public class shareListAdapter extends ArrayAdapter<shareDatas> {

    Context context;
    int layoutResourceId;
    shareDatas data[] = null;

    public shareListAdapter(Context context, int layoutResourceId, shareDatas[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

        shareDatas shareDatas = data[position];
        holder.sharePic.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("圖片")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
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
