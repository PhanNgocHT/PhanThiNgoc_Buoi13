package com.example.admin.phanthingoc_buoi13;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener {
    private FloatingActionButton flButton;
    private ListView lvFile;
    private AdapterFile adapterFile;
    private ArrayList<File> arrFile = new ArrayList<>();
    private FileManager fileManager = new FileManager();
    private String currentPath;
    private DrawerLayout activity_main;
    private ActionBarDrawerToggle toggle;
    private Dialog dialog;
    private Dialog dialogCreate;
    private File fileDelete;
    private EditText edtName;
    private EditText edtPath;
    protected String Pa;
    private String fileC;
    private ListView lvFileX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDrawable();
        setupActionBar();
        initDialog();
    }

    private void initDialog() {
        dialog = new Dialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog, null);
        TextView tvMove = (TextView) view.findViewById(R.id.tvMove);
        TextView tvDelete = (TextView) view.findViewById(R.id.tvDelete);
        TextView tvCopy = (TextView) view.findViewById(R.id.tvCopy);
        dialog.setContentView(view);
        tvMove.setOnClickListener(this);
        tvCopy.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
    }

    private void setupActionBar() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initDrawable() {
        activity_main = (DrawerLayout) findViewById(R.id.main_activity);
        toggle = new ActionBarDrawerToggle(this, activity_main, null, R.string.open, R.string.close);
        activity_main.addDrawerListener(toggle);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {
        lvFile = (ListView) findViewById(R.id.lvFile);
        lvFileX= (ListView) findViewById(R.id.lvFileX);
        flButton = (FloatingActionButton) findViewById(R.id.flButon);
        currentPath = FileManager.ROOT_PATH;
        arrFile.addAll(Arrays.asList(fileManager.readFolder(currentPath)));
        adapterFile = new AdapterFile(arrFile, this);
        lvFile.setAdapter(adapterFile);
        lvFile.setOnItemClickListener(this);
        lvFile.setOnItemLongClickListener(this);
        flButton.setOnClickListener(this);
        lvFileX.setAdapter(adapterFile);
        lvFileX.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activity_main.closeDrawers();
//                File file = arrFile.get(position);
//                if (file.isDirectory()) {
//                    arrFile.clear();
//                    currentPath = file.getPath();
//                    arrFile.addAll(Arrays.asList(fileManager.readFolder(currentPath)));
//                    adapterFile.notifyDataSetChanged();
//                }

            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File file = arrFile.get(position);
        if (file.isDirectory()) {
            arrFile.clear();
            currentPath = file.getPath();
            arrFile.addAll(Arrays.asList(fileManager.readFolder(currentPath)));
            adapterFile.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        File file = new File(currentPath);
        currentPath = file.getParent();
        if (currentPath != null) {
            arrFile.clear();
            arrFile.addAll(Arrays.asList
                    (fileManager.readFolder(currentPath)));
            adapterFile.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        fileDelete=arrFile.get(position);
        fileC=arrFile.get(position).getPath();
        Pa=arrFile.get(position).getName();
        dialog.show();
        return true;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.tvDelete:
                dialog.dismiss();
                deleteFile();
                adapterFile.notifyDataSetChanged();
                break;
            case R.id.tvCopy:
                dialog.dismiss();
                File src=new File(fileC);
                File dst = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), Pa);
                try {
                    dst.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                copy(src, dst);
                adapterFile.notifyDataSetChanged();
                break;
            case R.id.tvMove:
                dialog.dismiss();
                File sr=new File(fileC);
                File ds = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), Pa);
                try {
                    ds.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                copy(sr, ds);
                deleteFile();
                adapterFile.notifyDataSetChanged();
                break;
            case R.id.flButon:
                final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Do you want create new folder ?");
                alertDialog.setButton(alertDialog.BUTTON_POSITIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setButton(alertDialog.BUTTON_NEGATIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                        dialogCreate = new Dialog(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog);
                        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                        View view = inflater.inflate(R.layout.dialog_create, null);
                        Button button = (Button) view.findViewById(R.id.btnOK);
                        edtName= (EditText) view.findViewById(R.id.edtName);
                        edtPath= (EditText) view.findViewById(R.id.edtPath);
                        button.setOnClickListener(MainActivity.this);
                        dialogCreate.setContentView(view);
                        dialogCreate.show();
                    }
                });
                alertDialog.show();
                break;
            case R.id.btnOK:
                dialogCreate.dismiss();
                createFile();
                adapterFile.notifyDataSetChanged();
                break;
        }
    }

    private void createFile()  {
        File sdCard = Environment.getExternalStorageDirectory();
        if (sdCard.exists()) {
            if (edtName.getText().toString().isEmpty()||edtPath.getText().toString().isEmpty()) {
                Toast.makeText(this, "Folder or file invite", Toast.LENGTH_SHORT).show();
                return;
            }
            File directory = new File(sdCard.getAbsolutePath()+ edtPath.getText().toString());
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory, edtName.getText().toString());
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        currentPath = FileManager.ROOT_PATH;
        arrFile.addAll(Arrays.asList(fileManager.readFolder(currentPath)));
    }

    public void copy(File src, File dst) {
        InputStream in = null;
        try {
            in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void deleteFile() {
        File file=new File(fileC);
        if (fileDelete.isDirectory()) {
            try {
                FileUtils.deleteDirectory(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            file.delete();
        }
        arrFile.clear();
        currentPath = FileManager.ROOT_PATH;
        arrFile.addAll(Arrays.asList(fileManager.readFolder(currentPath)));
    }
}
