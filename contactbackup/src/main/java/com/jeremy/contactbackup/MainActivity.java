package com.jeremy.contactbackup;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jeremy.contactbackup.adapter.ContactAdapter;
import com.jeremy.contactbackup.beans.ContactInfoEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_contact)
    RecyclerView rvContact;
    private List<ContactInfoEntity> mContactInfoEntities = new ArrayList<>();
    private int countToAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvContact.setLayoutManager(mLayoutManager);
        mContactInfoEntities = getContacts();
        ContactAdapter mAdapter = new ContactAdapter(this, mContactInfoEntities);
        rvContact.setAdapter(mAdapter);
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @OnClick(R.id.btn_add)
    void onClick() {
        if (countToAdd != 0) {
            countToAdd = 0;
        }
        for (ContactInfoEntity entity : mContactInfoEntities) {
            if (entity.isChecked) {
                countToAdd++;
            }
        }
        Toast.makeText(this, "总共选中了：" + countToAdd, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<ContactInfoEntity> getContacts() {
        List<ContactInfoEntity> contactInfoEntities = new ArrayList<>();
        //生成ContentResolver对象
        ContentResolver contentResolver = getContentResolver();
        // 获得所有的联系人
        Cursor cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        // 循环遍历
        if (cursor != null && cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            int displayNameColumn = cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            do {
                ContactInfoEntity contactInfoEntity = new ContactInfoEntity();
                // 获得联系人的ID
                String contactId = cursor.getString(idColumn);
                // 获得联系人姓名并存入实体中去
                contactInfoEntity.name = cursor.getString(displayNameColumn);
                // 查看联系人有多少个号码，如果没有号码，返回0
                int phoneCount = cursor.getInt(cursor
                        .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (phoneCount > 0) {
                    // 获得联系人的电话号码列表
                    Cursor phoneCursor = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + "=" + contactId, null, null);
                    if (phoneCursor != null && phoneCursor.moveToFirst()) {
                        do {
                            //遍历所有的联系人下面所有的电话号码
                            String phoneNumber = phoneCursor.getString(phoneCursor
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            //将空格去掉并将号码存储到实体中去
                            phoneNumber = phoneNumber.replace(" ", "");
                            contactInfoEntity.phoneNumbers.add(phoneNumber);
                        } while (phoneCursor.moveToNext());
                        phoneCursor.close();
                    }
                }
                contactInfoEntities.add(contactInfoEntity);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return contactInfoEntities;
    }
}
