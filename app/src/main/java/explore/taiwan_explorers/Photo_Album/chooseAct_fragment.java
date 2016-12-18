package explore.taiwan_explorers.Photo_Album;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import explore.taiwan_explorers.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by no_name on 2016/12/11.
 */
public class chooseAct_fragment  extends Fragment {

    Button shot;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_act,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shot = (Button) getView().findViewById(R.id.buttonShot);
        shot.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                //使用Intent調用其他服務幫忙拍照
                Intent intent_camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent_camera, 0);
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //拍照後顯示圖片
        ImageView iv = (ImageView)getActivity().findViewById(R.id.imagePhoto);
        if (resultCode == RESULT_OK)
        {
            //取出拍照後回傳資料
            Bundle extras = data.getExtras();
            //將資料轉換為圖像格式
            Bitmap bmp = (Bitmap) extras.get("data");
            //載入ImageView
            iv.setImageBitmap(bmp);
        }

        //覆蓋原來的Activity
        super.onActivityResult(requestCode, resultCode, data);
    }

}
