package executeSQL;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class CreSQL {
    public String tabName;//存储表名
    public ArrayList colList  = new ArrayList();//存储列名与属性
    public ArrayList primaryList  = new ArrayList();//存储主键
    public ArrayList foreignList = new ArrayList();//存储外键

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public ArrayList getColList() {
        return colList;
    }

    public void setColList(ArrayList colList) {
        this.colList = colList;
    }

    public ArrayList getPrimaryList() {
        return primaryList;
    }

    public void setPrimaryList(ArrayList primaryList) {
        this.primaryList = primaryList;
    }

    public ArrayList getForeignList() {
        return foreignList;
    }

    public void setForeignList(ArrayList foreignList) {
        this.foreignList = foreignList;
    }

    //执行创建create语句
    public void ExecuteCreSQL(){
        String descpath = "descripition.txt";//描述文件
        String configpath = tabName+"config.txt";
        String datapath = tabName+"data.txt";
        File configfile = new File(configpath);//打开TXT文件
        File datafile = new File(datapath);
        File descfile = new File(descpath);
        FileOutputStream fos = null;
        if(!configfile.exists() && !datafile.exists()){
           // System.out.println("正在输入...");
            try{
                configfile.createNewFile();
                datafile.createNewFile();
                fos = new FileOutputStream(configfile);
                String AttStr = "";
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                //写入表名
                osw.write("tableName:"+"\r\n");
                osw.write(tabName+"\r\n");
                //写入属性
                for(int i=0;i<colList.size();i=i+2){
                    AttStr = AttStr+colList.get(i)+" ";
                    osw.write(colList.get(i)+" "+colList.get(i+1)+"\r\n");
                }
                //写入表级完整性约束
                //写入主键
                osw.write("primary key:"+"\r\n");
                for(int i=0;i<primaryList.size();i++){
                    osw.write(primaryList.get(i)+"\r\n");
                }
                //写入外键
                FileOutputStream fos1 = new FileOutputStream(descfile,true);
                OutputStreamWriter osw1 = new OutputStreamWriter(fos1);
                osw.write("foreign key:"+"\r\n");
                for(int i=0;i<foreignList.size();i=i+2){
                    osw.write(foreignList.get(i+1)+"."+foreignList.get(i)+"\r\n");
                    osw1.write(tabName+"^"+foreignList.get(i+1)+"\r\n");//将外键的引用表写入描述文件

                }
                //写入数据表头
                FileOutputStream fos2 = new FileOutputStream(datafile,true);
                OutputStreamWriter osw2 = new OutputStreamWriter(fos2);
                osw2.write(AttStr+"\r\n");
                System.out.println(AttStr);
                osw.close();
                osw1.close();
                osw2.close();
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        else{
            System.out.println("该表已创建！");
        }
    }
}
