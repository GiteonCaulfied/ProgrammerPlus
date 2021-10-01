package au.edu.anu.cecs.COMP6442GroupAssignment.util.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import au.edu.anu.cecs.COMP6442GroupAssignment.R;

public class ChatFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_chat, container, false);
        return view;
    }
}
