package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.entity.StudentEntity;
// post use mapip
@Service
public class StudentService {
    Map<Integer,StudentEntity> mp = new HashMap<>();
    //give the data as it is 
    public StudentEntity savedata(StudentEntity st){
        mp.put(st.getId(),st);
        return st;

    }
    //retrive all the data
    public List<StudentEntity> retdata() {
        return new ArrayList<>(mp.values());
    }
    //retrive particular data
    public Studententity id(int id) {
        return mp.get(id);
    }
    //update
    public StudentEntity ids(int id, StudentEntity st) {
        return mp.put(id, st);
    }
    //delete
    public StudentEntity ideas(int id) {
        return mp.remove(id);
    }
}
