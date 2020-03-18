package executeSQL;

import java.io.*;
import java.util.ArrayList;

public class CommonFun {
    //删除文件中的某一行数据，matchStr表示要删除行的匹配字符
    static public void delLine(String path,String matchStr){
        File file = new File(path);
        ArrayList contentList = new ArrayList();
        //删除文件中的某一行数据
        if(file.exists()){
            try{
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = null;
                while((line = br.readLine())!=null){
                    //未到达要删除的指定行
                    if(!line.contains(matchStr)){
                        contentList.add(line);
                    }
                    //到达要删除的指定行并且只删除一行
                    else{
                        continue;
                    }
                }
                //将删除指定行后的数据重新写入数据
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                for(int i=0;i<contentList.size();i++){
                    osw.write(contentList.get(i).toString()+"\r\n");
                }
                osw.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }
    //修改文件中的某一行数据，matchStr表示要修改行的匹配字符，如果匹配成功，将updStr替换原字符串
    static public void updLine(String matchStr,String updStr,String path){
        File file = new File(path);
        ArrayList contentList = new ArrayList();//将文件中不需要修改的原数据放入List中
        if(file.exists()){
            try{
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = null;
                while((line = br.readLine())!=null){
                    //如果匹配成功，则替换
                    if(line.contains(matchStr)){
                        contentList.add(updStr);
                    }
                    //如果匹配失败，则将原数据放入List中
                    else{
                        contentList.add(line);
                    }
                }
                //将删除指定行后的数据重新写入数据
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                for(int i=0;i<contentList.size();i++){
                    osw.write(contentList.get(i).toString()+"\r\n");
                }
                osw.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    //获取属性在表中的位置
    static public int getLocation(Table t,String attName){
        int location = 0;
        int i=0;
        for(i=0;i<t.attributeList.size();i++){
            if(t.attributeList.get(i).Aname .equals(attName)){
                location = i;
                break;
            }
        }
        if(i==t.attributeList.size()){
            System.out.println("条件中出现的属性不存在！");
            return -1;
        }
        return location;
    }

    //判断where后紧跟的条件
    static public ArrayList[] judgeCondition(ArrayList conditionList,Table t){
        ArrayList afterList[] = new ArrayList[100];
        ArrayList linkList = new ArrayList();
        ArrayList fail[] = new ArrayList[1];//如果条件中出现的属性错误，则返回fail
        ArrayList f =new ArrayList();
        f.add("fail");
        fail[0]=f;
        //将条件分割
        int num = 0;
        for(int i=0;i<conditionList.size();i=i+4){
            ArrayList list = new ArrayList();
            list.add(conditionList.get(i));
            list.add(conditionList.get(i+1));
            list.add(conditionList.get(i+2));
            afterList[num++]=list;
            if(i+3>=conditionList.size()){
                break;
            }
            else {
                linkList.add(conditionList.get(i+3));
            }
        }
        //得出条件中属性的位置
        for(int i=0;i<num;i++){
            int location = 0;
            String attName = afterList[i].get(0).toString();
            location = getLocation(t,attName);
            if(location == -1){
                return fail;
            }
            switch (afterList[i].get(1).toString()){
                case "=":
                    for(int j=0;j<t.number;j++){
                        if(t.dataList[j]!=null){
                            if(t.attributeList.get(location).dataType.equals("int")){
                                if(Integer.parseInt(t.dataList[j].get(location).toString())!=Integer.parseInt(afterList[i].get(2).toString())){
                                    t.dataList[j]=null;
                                }
                            }
                            else {
                                if(!(t.dataList[j].get(location).toString().equals(afterList[i].get(2).toString()))){
                                    t.dataList[j]=null;
                                }
                            }
                        }
                    }
                    break;
                case">":
                    for(int j=0;j<t.number;j++){
                        if(t.dataList[j]!=null){
                            System.out.println(t.dataList[j].get(location));
                            System.out.println(afterList[i].get(2));
                            if(t.attributeList.get(location).dataType.equals("int")){
                                if(Integer.parseInt(t.dataList[j].get(location).toString())<=Integer.parseInt(afterList[i].get(2).toString())){
                                    t.dataList[j]=null;
                                }
                            }
                            else {
                                if((t.dataList[j].get(location).toString().compareTo(afterList[i].get(2).toString()))<1){
                                    t.dataList[j]=null;
                                }
                            }
                        }
                    }
                    break;
                case "<":
                    for(int j=0;j<t.number;j++){
                        if(t.dataList[j]!=null){
                            System.out.println(t.dataList[j].get(location));
                            System.out.println(afterList[i].get(2));
                            if(t.attributeList.get(location).dataType.equals("int")){
                                if(Integer.parseInt(t.dataList[j].get(location).toString())>=Integer.parseInt(afterList[i].get(2).toString())){
                                    t.dataList[j]=null;
                                }
                            }
                            else {
                                if((t.dataList[j].get(location).toString().compareTo(afterList[i].get(2).toString()))>-1){
                                    t.dataList[j]=null;
                                }
                            }
                        }
                    }
                    break;
                case">=":
                    for(int j=0;j<t.number;j++){
                        if(t.dataList[j]!=null){
                            if(t.attributeList.get(location).dataType.equals("int")){
                                if(Integer.parseInt(t.dataList[j].get(location).toString())< Integer.parseInt(afterList[i].get(2).toString())){
                                    t.dataList[j]=null;
                                }
                            }
                            else {
                                if(t.dataList[j].get(location).toString().compareTo(afterList[i].get(2).toString())==-1){
                                    t.dataList[j]=null;
                                }
                            }
                        }
                    }
                    break;
                case"<=":
                    for(int j=0;j<t.number;j++){
                        if(t.dataList[j]!=null){
                            if(t.attributeList.get(location).dataType.equals("int")){
                                if(Integer.parseInt(t.dataList[j].get(location).toString())> Integer.parseInt(afterList[i].get(2).toString())){
                                    t.dataList[j]=null;
                                }
                            }
                            else {
                                if(t.dataList[j].get(location).toString().compareTo(afterList[i].get(2).toString())==1){
                                    t.dataList[j]=null;
                                }
                            }
                        }
                    }
                    break;
                case"<>":
                    for(int j=0;j<t.number;j++){
                        if(t.dataList[j]!=null){
                            if(t.attributeList.get(location).dataType.equals("int")){
                                if(Integer.parseInt(t.dataList[j].get(location).toString()) != Integer.parseInt(afterList[i].get(2).toString())){
                                    t.dataList[j]=null;
                                }
                            }
                            else {
                                if(t.dataList[j].get(location).toString().compareTo(afterList[i].get(2).toString()) == 0){
                                    t.dataList[j]=null;
                                }
                            }
                        }
                    }
                    break;
                case"like":
                    System.out.println("本程序未实现like功能！");
                    break;
                default:break;
            }
        }
        return t.dataList;
    }
}
