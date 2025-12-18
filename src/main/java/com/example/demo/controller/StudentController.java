package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.StudentEntity;
import com.example.demo.service.StudentService;

@RestController
public class StudentController {
    @Autowired
    StudentService src;
    @PatchMapping("/post")
    public StudentEntity postdata(@RequestBody StudentEntity st){ //to show the only one data
        return src.savedata(st);
    }
    @GetMapping("/get")
    // list used to retrive all the data
    public List<StudentEntity> getdata(){
        return src.retdata();
    }
    @GetMapping("/getid/{id}")
    //particular details only taken @PathVariable is used to work for {id} 
    public StudentEntity getIdVal(@PathVariable int id){
        return src.id(id);
    }
    @PutMapping("/update/{id}")
    public StudentEntity funName (@PathVariable int id,@RequestBody StudentEntity st){
    return src.ids(id,st);
}    
@DeleteMapping("/delete/{id}")
public StudentEntity delData(@PathVariable int id){
    return src.ideas(id);
}

}
