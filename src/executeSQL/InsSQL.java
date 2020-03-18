package executeSQL;

import java.io.*;
import java.util.ArrayList;

public class InsSQL {
    public String tabName;//存储表名
    public ArrayList inList = new ArrayList();//存储要插入的属性
    public ArrayList vList = new ArrayList();//存储要插入的属性数据

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }


    //将省略的属性补全
    public void AddAttribute(Table t){
        System.out.println("inList的长度："+inList);
        System.out.println("属性个数："+t.attributeList.size());
        int inList_size = inList.size();
        int j=0;
        if(inList.size()!=0 && (inList.size() < t.attributeList.size())){
            for(int i=0;i<t.attributeList.size();i++) {
                for (j = 0; j < inList_size; j++) {
                    if(inList.get(j).toString().equals(t.attributeList.get(i).Aname)) {
                        break;
                    }
                }
                if(j >= inList_size){
                    String attname = t.attributeList.get(i).Aname;
                    inList.add(attname);
                    vList.add("null");
                }
            }
        }
    }
    //判断插入的属性名在表中是否存在
    public boolean judgeAtt(Table t){
        int j=0;
        if(inList.size()!=0){
            for(int i=0;i<inList.size();i++){
                for(j=0;j<t.attributeList.size();j++){
                    if(inList.get(i).toString().equals(t.attributeList.get(j).Aname)){
                        break;
                    }
                }
                if(j==t.attributeList.size())
                    return false;
            }
        }
        return true;
    }

    //将乱序的插入内容调整为正确的顺序
    public void AdjustOrder(Table t){
        if(inList.size()!=0){
            for(int i=0;i<t.attributeList.size();i++){
                for(int j=0;j<inList.size();j++){
                    if(inList.get(j).toString().equals(t.attributeList.get(i).Aname)){
                        if(i!=j){
                            String str = inList.get(i).toString();
                            inList.set(i,inList.get(j).toString());
                            inList.set(j,str);
                            ArrayList l = new ArrayList();
                            l.add(vList.get(i));
                            vList.set(i,vList.get(j));
                            vList.set(j,l.get(0));
                        }
                        break;
                    }
                }
            }
        }
    }
    //判断要插入的属性和属性数据的个数是否相等
    public boolean isEqual(){
        int inList_Size = inList.size();
        int vList_Size = vList.size();
        if(inList_Size!=0){
            if(inList_Size == vList_Size){
                return true;
            }
            else
                return false;
        }
        return true;
    }
    //判断是否存在主键
    public boolean isPrimary(ArrayList<String> primary){
        int i=0;
        int flag=0;
        if(inList.size()!=0){
            for(i=0;i<primary.size();i++){
                if(inList.get(i).equals(primary.get(i))){
                    flag++;
                }
            }
            if(flag != i)
                return false;
        }
        return true;
    }
    //执行插入insert语句
    public void ExecuteInsSQL(){
        if(!isEqual()){
            System.out.println("插入格式错误！");
            return;
        }
        Table t = new Table();
        t.initConfig(tabName);
        t.init_Data(tabName);
        AddAttribute(t);//将省略的属性补全，插入的数据默认为null
        AdjustOrder(t);//将插入顺序调整为正确的顺序
        if(!judgeAtt(t)){
            System.out.println("插入的数据属性在该表中不存在！");
            return;
        }
        if(!isPrimary(t.primary_key)){
            System.out.println("插入的数据没有完整的主键！");
            return;
        }
        //判断插入的数据的数据类型是否一致
        for(int i=0;i<vList.size();i++){
            //判断数据是否为字符串类型
            if(t.attributeList.get(i).dataType == "char") {
                if (vList.get(i).getClass().toString().contains("String")) {
                    if(vList.get(i).toString().length() <= t.attributeList.get(i).charLen){
                        System.out.println("该数据合法！");
                    }
                    else{
                        System.out.println("该字符串长度大于规定长度，不合法！");
                        return;
                    }
                }
                else{
                    System.out.println("该数据不是字符串类型，不合法！");
                    return;
                }
            }
            //判断数据是否为date型
            else if(t.attributeList.get(i).dataType == "date"){
                if(vList.get(i).toString().contains("-") == false){
                    System.out.println("插入数据的格式不合法！");
                    return;
                }
            }
            //判断数据是否为int型
            else{
                if(vList.get(i).toString().matches("[0-9]+") == false && !(vList.get(i).toString().equals("null"))){
                    System.out.println("int插入数据的格式不合法！");
                    return;
                }
            }
        }
        //判断文件中的主键内容是否相等
        for(int i=0;i<t.number;i++){
            int j=0;
            for(j=0;j<t.primary_key.size();j++){
                if(vList.get(j).toString().compareTo(t.dataList[i].get(j).toString())==0){
                    System.out.println("该表中已存在相等的主键数据！");
                    return;
                }
            }
        }
        //插入数据
        try{
            String path = tabName+"data.txt";
            File file = new File(path);
            if(file.exists()){
                String str = "";
                for(int i=0;i<vList.size();i++){
                    str = str+vList.get(i).toString()+" ";
                }
                FileOutputStream fos = new FileOutputStream(file,true);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                osw.write(str+"\r\n");
                osw.close();
            }
            else{
                System.out.println("不存在该表，插入失败！");
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
