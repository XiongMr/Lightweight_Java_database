package executeSQL;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import javax.xml.stream.events.Attribute;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
/*Table主要存储表的主要信息，包括：
表名；
属性；
主键；
外键；
 */
public class Table {
    public class ForeignKey{
        String key;//存储外键
        String tab;//存储外键所属表

        public String getKey() { return key; }

        public void setKey(String key) { this.key = key; }

        public String getTab() { return tab; }

        public void setTab(String tab) { this.tab = tab; }
    }
    public class Attribute{
        String Aname;//属性名
        String dataType;//数据类型
        int charLen =0;//字符串长度

        public String getAname() { return Aname; }

        public void setAname(String aname) { Aname = aname; }

        public String getDataType() { return dataType; }

        public void setDataType(String dataType) { this.dataType = dataType; }

        public int getCharLen() { return charLen; }

        public void setCharLen(int charLen) { this.charLen = charLen; }
    }

    public String tabName;//表名
    public ArrayList<Attribute> attributeList = new ArrayList<Attribute>();//属性
    public ArrayList<String> primary_key = new ArrayList<String>();//主键
    public ArrayList<ForeignKey> foreign_key = new ArrayList<ForeignKey>();//外键
    public ArrayList dataList[] = new ArrayList[1000];//存储表的具体数据
    public int number;//表中数据的条数

    //初始化表的格式
    public boolean initConfig(String Tname){
        String configpath = Tname+"config.txt";
        File file = new File(configpath);
        if(file.exists()){
            try{
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = null;
                if((line=br.readLine())!=null){
                    if(line.equals("tableName:")){
                        tabName = br.readLine();//将表名写入tabName
                    }
                    //写入属性
                    int i=0;
                    while(!(line= br.readLine()).equals("primary key:")){
                        //取出单条属性的数据
                        Attribute att = new Attribute();
                        //将数据分割为属性名与数据类型
                        String ch[] = line.split(" ");
                        att.Aname = ch[0];//写入属性名
                        //写入属性的数据类型
                        if(ch[1].contains("char")){
                            att.dataType = "char";
                            String str = ch[1].replace("char","");
                            att.charLen = Integer.parseInt(str);
                        }
                        else
                            att.dataType = ch[1];
                        attributeList.add(att);
                    }
                    //写入主键
                    while(!(line=br.readLine()).equals("foreign key:")){
                        primary_key.add(line);//添加主键
                    }
                    //写入外键
                    i=0;
                    while((line=br.readLine())!=null){
                        //将数据分割为属性名与数据类型
                        System.out.println(line);
                        ForeignKey fk = new ForeignKey();
                        String []ch = line.split("\\.");
                        System.out.println(ch[0]);
                        fk.tab = ch[0];
                        fk.key = ch[1];
                        foreign_key.add(fk);
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }


        }else{
            System.out.println("该表不存在！");
            return false;
        }
        return true;
    }

    //初始化表的内容
    public void init_Data(String Tname){
        String path = Tname+"data.txt";
        File file = new File(path);
        int num=0;//存储总的数据条数
        int j=0;
        if(file.exists()){
            try{
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();//获取表格的第一行即表头
                while((line = br.readLine())!=null){
                    num++;
                    String []data=line.split(" ");//将一行数据按照空格分割
                    ArrayList oneline = new ArrayList();//将一行数据处理后存入该list中
                    for(int i=0;i<attributeList.size();i++){
                        //如果数据类型为date或char型，直接存储
                        Attribute att = attributeList.get(i);
                        if(att.dataType.equals("char") ||att.dataType.equals("date")){
                            oneline.add(data[i]);
                        }
                        else if(att.dataType.equals("int")){
                            //如果数据类型为int型，将数据转换为int型再存储
                            int n = Integer.parseInt(data[i]);
                            oneline.add(n);
                        }
                    }
                    dataList[j++]=oneline;
                }
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        else{
            System.out.println("该表不存在！");
        }
        number = num;
    }

    //判断判定条件是否成立

}
