package com.ivlup.udicate.backend.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ivlup.udicate.backend.users.Person;
import com.ivlup.udicate.backend.users.Teacher;

public abstract class UserProfileRepository {
    private static String type;
    private static Person person;
    private static Teacher teacher;
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();;


    public interface Callback{
        void personInfoBack();
    }

    static UserProfileRepository.Callback callback;


    public static void registerCallBack(UserProfileRepository.Callback callback){
        UserProfileRepository.callback = callback;
    }

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        UserProfileRepository.type = type;
    }

    public static Person getPerson() {
        return person;
    }

    public static void setPerson(Person person) {
        UserProfileRepository.person = person;
    }

    public static Teacher getTeacher() {
        return teacher;
    }

    public static void setTeacher(Teacher teacher) {
        UserProfileRepository.teacher = teacher;
    }

    private static void getPersonByUid(String id) {
        mDatabase.child("users").child("persons").child(id).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    setPerson(dataSnapshot.getValue(Person.class));
                    Log.i("MyLog", getPerson().name);
                    setType("Person");
                    callback.personInfoBack();
                }
                catch (Exception e){
                    Log.i("MyLog", e.getMessage());
                    callback.personInfoBack();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("MyLog", "Erorrrrrrrrrrrrrrrrrrrrrrrr");
                callback.personInfoBack();
            }
        });
    }

    private static void getTeacherByUid(String id) {

        mDatabase.child("users").child("teachers").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    setTeacher(dataSnapshot.getValue(Teacher.class));
                    setType("Teacher");
                    Log.i("MyLog", getTeacher().name);
                    callback.personInfoBack();
                }
                catch (Exception e){
                    callback.personInfoBack();
                    Log.i("MyLog", e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("MyLog", "Erorrrrrrrrrrrrrrrrrrrrrrrr");
            }
        });

    }

    public static void getUserByUid(String token){
        getPersonByUid(token);
        getTeacherByUid(token);
    }
}
