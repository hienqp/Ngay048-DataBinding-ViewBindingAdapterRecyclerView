package com.example.viewbindingadapterrecyclerview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viewbindingadapterrecyclerview.databinding.FragmentMyBinding;

import java.util.ArrayList;
import java.util.List;

public class MyFragment extends Fragment {
    // đối tượng View Binding
    private FragmentMyBinding mFragmentMyBinding;

    // đối tượng instance View của Fragment
    private View mView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFragmentMyBinding = FragmentMyBinding.inflate(inflater, container, false);

        mFragmentMyBinding.tvName.setText("TEST VIEW BINDING IN FRAGMENT");

        // khởi tạo đối tượng View để return về cho Fragment
        mView = mFragmentMyBinding.getRoot();

        // sử dụng Fragment Binding ánh xạ cho đối tượng RecyclerView
        RecyclerView rcvUser = mFragmentMyBinding.rcvUser;
        // khởi tạo LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mView.getContext());
        // setLayoutManager cho RecyclerView
        rcvUser.setLayoutManager(linearLayoutManager);

        // thêm đường trang trí phân cách cho mỗi item trên RecyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mView.getContext(), DividerItemDecoration.VERTICAL);
        rcvUser.addItemDecoration(dividerItemDecoration);

        // khởi tạo UserAdapter
        UserAdapter userAdapter = new UserAdapter(getUserList());

        // đổ dữ liệu từ Adapter lên RecyclerView
        rcvUser.setAdapter(userAdapter);

        return mView;
    }

    private List<User> getUserList() {
        List<User> userList = new ArrayList<>();

        userList.add(new User("User 1"));
        userList.add(new User("User 2"));
        userList.add(new User("User 3"));
        userList.add(new User("User 4"));
        userList.add(new User("User 5"));
        userList.add(new User("User 6"));
        userList.add(new User("User 7"));
        userList.add(new User("User 8"));
        userList.add(new User("User 9"));
        userList.add(new User("User 10"));
        userList.add(new User("User 11"));
        userList.add(new User("User 12"));

        return userList;
    }
}