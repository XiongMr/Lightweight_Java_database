package executeSQL;

import java.io.*;
import java.util.ArrayList;

public class DropSQL {
    public String tabName;//表名
    public String  dropCond = "restrict";//删除条件，是否级联

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getDropCond() {
        return dropCond;
    }

    public void setDropCond(String dropCond) {
        this.dropCond = dropCond;
    }
    //获取关联表
    public ArrayList getCASCADE(String tabN){
        String descpath="descripition.txt";
        File descfile = new File(descpath);
        ArrayList referList = new ArrayList();
        //读取描述文件的内容
        try{
            BufferedReader br = new BufferedReader(new FileReader(descfile));
            String line = null;
            while((line = br.readLine())!=null){
                if(line.contains("^"+tabN)){
                    line = line.replace("^"+tabN,"");
                    referList.add(line);
                }
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return referList;
    }
    //执行删除drop语句
    public void ExecuteDropSQL(){
        String configpath=tabName+"config.txt";
        String datapath=tabName+"data.txt";
        String descpath="descripition.txt";

        File configfile = new File(configpath);//打开TXT文件
        File datafile = new File(datapath);
        File descfile = new File(descpath);

        //如果该表的两个文件存在，根据删除条件进行删除操作，否则判定删除错误
        ArrayList referList = getCASCADE(tabName);
        if(configfile.exists() && datafile.exists()){
                //如果实现级联删除
                if(dropCond.equals("cascade")){
                    //删除该表
                    datafile.delete();
                    configfile.delete();
                    //删除与该表关联的表
                    for(int i=0;i<referList.size();i++){
                        //获取关联表路径
                        String configpath1=referList.get(i)+"config.txt";
                        String datapath1=referList.get(i)+"data.txt";

                        File configfile1 = new File(configpath1);//打开TXT文件
                        File datafile1 = new File(datapath1);
                        //删除关联表
                        configfile1.delete();
                        datafile1.delete();
                        //删除描述文件中的相关信息
                        CommonFun.delLine("descripition.txt",referList.get(i).toString());
                    }
                }
                //如果实现限制删除
                else
                {
                    //该表没有关联表
                    if(referList.size()==0){
                        //删除该表
                        datafile.delete();
                        configfile.delete();
                        //删除描述文件中的相关信息
                        CommonFun.delLine("descripition.txt",tabName);
                    }else{
                        System.out.println("该表有关联表，无法删除！");
                    }
                }

        }
        else{
            System.out.println("该表不存在！");
        }
    }
}
