package kg.auca.doodle;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kg.auca.doodle.content.DoodleItem;
import kg.auca.doodle.content.DoodleModel;

public class DoodleDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";

    private DoodleItem mItem;

    public DoodleDetailFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = new DoodleModel(getContext()).getDoodle(getArguments().getLong(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.doodle_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.doodle_detail)).setText(mItem.getPath());
        }

        return rootView;
    }
}
