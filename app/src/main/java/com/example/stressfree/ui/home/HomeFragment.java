package com.example.stressfree.ui.home;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.example.stressfree.MainActivity;
import com.example.stressfree.databinding.FragmentHomeBinding;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ImageView pie;
    ListView emotions_list;
    ByteArrayInputStream inputStream;
    Activity main;
    Python python;
    PyObject py;
    boolean load_camera;
    SharedPreferences data;
    SharedPreferences.Editor edit;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        python = Python.getInstance();
        py = python.getModule("main");

        pie = binding.pie;
        emotions_list = binding.emotionList;

        main = getActivity();

        get_emotion();

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode == RESULT_OK) {
            assert data != null;
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            PyObject out = py.callAttr("main", (Object) outputStream.toByteArray());
            String emotion = out.toString();
            Log.d("emotion",emotion);
        }
    }

    public void get_emotion() {

        if(ContextCompat.checkSelfPermission(main, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(main,new String[] {Manifest.permission.CAMERA},100);
        }
        Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(capture,101);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}