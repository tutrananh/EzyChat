package com.ptit.ezychat.cache_db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


import com.ptit.ezychat.model.Message;

import java.util.ArrayList;
import java.util.List;

public class SQLHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ezychat.db";
    private static final int DB_VERSION = 1;
    private static final String DB_TITLE = "title";
    public SQLHelper(@Nullable Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE message(" +
                "id long PRIMARY KEY," +
                "message TEXT," +
                "time TEXT," +
                "channelId long," +
                "sender TEXT" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public Message searchLastMessageByChannelId(long channelId){
        Message message = new Message();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM message WHERE channelId = '"+channelId + "'", null);
        while (cursor != null && cursor.moveToNext()){
            message.setId(cursor.getLong(0));
            message.setMessage(cursor.getString(1));
            message.setTime(cursor.getString(2));
            message.setChannelId(cursor.getLong(3));
            message.setSender(cursor.getString(4));
        }
        return message;
    }

    public List<Message> searchMessagesByChannelId(long channelId){
        List<Message> messageList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM message WHERE channelId = "
                +channelId + " ORDER BY id ASC", null);
        while (cursor != null && cursor.moveToNext()){
            long id = cursor.getLong(0);
            String messageStr = cursor.getString(1);
            String time = cursor.getString(2);
            long idOfChannel = cursor.getLong(3);
            String sender = cursor.getString(4);
            Message message = new Message(id, messageStr,time,idOfChannel,sender);
            messageList.add(message);
        }
        return messageList;
    }


    public void addMessage(Message message){
        String sql = "INSERT INTO message (id, message, time, channelId, sender)" +
                "VALUES (?, ?, ?, ?, ?)";
        String agrs[] = {
                String.valueOf(message.getId()),
                message.getMessage(),
                message.getTime(),
                String.valueOf(message.getChannelId()),
                message.getSender()};
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        sqLiteDatabase.execSQL(sql, agrs);
    }


}
