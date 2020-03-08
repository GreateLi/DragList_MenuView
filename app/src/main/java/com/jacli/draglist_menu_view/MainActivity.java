package com.jacli.draglist_menu_view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;


import com.jacli.draglist_menu_view.note.DragListAdapter;
import com.jacli.draglist_menu_view.note.DragListView;
import com.jacli.draglist_menu_view.note.bean.newItemNoteContentParam.NotesBean;
import com.jacli.draglist_menu_view.utils.CallbackInterface;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    public final static String EDITTEXT_CONETENT_CHANGED = "edittext_changed";
    private DragListView listView;
    private DragListAdapter dragListAdapter;
    private Context mContext;

    private List<NotesBean> noteList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        listView = findViewById(R.id.listview);




       // into_animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_right);
        dragListAdapter = new DragListAdapter(mContext, getItems());
        listView.setAdapter(dragListAdapter);
    }

    public static ArrayList<NotesBean> getItems() {
        ArrayList<NotesBean> items = new ArrayList<NotesBean>();
        for (int i = 0; i < 20; i++) {
            NotesBean bean = new NotesBean();
            bean.setId(""+i);
            bean.setCreateTime(System.currentTimeMillis()+i*100);
            bean.setNotesContent("this is "+i);
            bean.setNotesType("0");
            items.add(bean);
        }
        return items;
    }

    /**
     * 显示提示语句
     */
    public void simpleDialogSelect_(String str ,final CallbackInterface callback) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(str);

            builder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(null!= callback)
                            {
                                callback.select(true);
                            }
                        }
                    });
            // cancle
            builder.setNegativeButton(R.string.soft_update_cancel,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
