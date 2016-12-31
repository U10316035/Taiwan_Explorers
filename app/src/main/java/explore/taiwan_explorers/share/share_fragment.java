package explore.taiwan_explorers.share;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import explore.taiwan_explorers.R;

/**
 * Created by no_name on 2016/12/14.
 */
public class share_fragment  extends Fragment {

    private ListView listView1;
    ImageView enlarge;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.share_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enlarge = (ImageView)view.findViewById(R.id.imageViewShareEnlarge);
        shareDatas data[] = new shareDatas[]
                {
                        new shareDatas("title","uploader", "flagtitle", "flagcontext", 0, 0, "diary", "pic"),
                        new shareDatas("title1","uploader1", "flagtitle1", "flagcontext1", 0, 0, "diary1", "pic"),
                        new shareDatas("title","uploader2", "flagtitle2", "flagcontext", 0, 0, "diary2", "pic"),
                        new shareDatas("title","uploader3", "flagtitle3", "flagcontext", 0, 0, "diary2", "pic"),
                };

        shareListAdapter adapter = new shareListAdapter(getContext(),
                R.layout.share_fragment_listitem, data,share_fragment.this);

        listView1 = (ListView)getView().findViewById(R.id.listViewShare);
        listView1.setAdapter(adapter);
    }

    public void enlargePIC(){
        enlarge.setVisibility(View.VISIBLE);
        enlarge.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                enlarge.setVisibility(View.INVISIBLE);
            }
        });
    }
}
