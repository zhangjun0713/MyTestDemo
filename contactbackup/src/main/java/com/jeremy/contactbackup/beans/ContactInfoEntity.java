package com.jeremy.contactbackup.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjun on 2017/3/7.
 * 联系人信息
 */

public class ContactInfoEntity {
    /**
     * 是否被选中了
     */
    public boolean isChecked;
    /**
     * 联系人姓名
     */
    public String name;
    /**
     * 联系人电话号码
     */
    public List<String> phoneNumbers = new ArrayList<>();
}
