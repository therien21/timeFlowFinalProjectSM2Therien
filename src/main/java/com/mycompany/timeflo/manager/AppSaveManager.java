/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.timeflo.manager;
import com.mycompany.timeflo.model.AppData;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 *
 * @author theri
 */

public class AppSaveManager {
    private static final String FILE_NAME = "timeflo-data.ser";
    public void saveData(AppData data) throws Exception {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))){
            out.writeObject(data);
        }
    }
    public AppData loadData() throws Exception {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))){
            return (AppData) in.readObject();
        }
    }
}
