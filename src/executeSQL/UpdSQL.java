package executeSQL;

import java.util.ArrayList;

public class UpdSQL {
    public String tabName;//表名
    public ArrayList upList = new ArrayList();//修改列表
    public ArrayList conditionList = new ArrayList();//修改条件

    public String getTabName() { return tabName; }

    public void setTabName(String tabName) { this.tabName = tabName; }

    public Boolean GrammerMistake(){
        for(int i=0;i<conditionList.size();i++){
            if(conditionList.get(i).toString().contains(".")){
                System.out.println("该语句有语法错误！");
                return false;
            }
        }
        return true;
    }
    //将一个list中的值转换为相连的字符串
    public String ListtoString(ArrayList list){
        String str = "";
        for(int i=0;i<list.size();i++){
            str = str+list.get(i)+" ";
        }
        return str;
    }

    //执行更新update语句
    public void ExecuteUpdSQL(){
        if(!GrammerMistake()){
            return;
        }
        //初始化表的内容
        Table t = new Table();
        t.initConfig(tabName);
        t.init_Data(tabName);
        ArrayList List[] = CommonFun.judgeCondition(conditionList,t);//获取满足条件的数据
        int flag = 0;
        int locations[] = new int[100];
        //获取要修改的属性在表中的位置
        for(int i=0;i<upList.size()/2;i++){
            locations[i] = CommonFun.getLocation(t,upList.get(i*2).toString());
            System.out.println("属性位置："+locations[i]);
        }
        //修改数据
        for(int i=0;i<t.number;i++){
            if(List[i]!=null){
                flag++;
                String matchStr = ListtoString(List[i]);//将List的内容转换为字符串
                System.out.println("匹配字符串："+matchStr);
                for(int j=0;j<upList.size()/2;j++){
                    int location = locations[j];
                    List[i].set(location,upList.get(j*2+1));//将数据修改
                }
                String updStr = ListtoString(List[i]);
                System.out.println("替换字符串："+updStr);
                CommonFun.updLine(matchStr,updStr,tabName+"data.txt");//将修改的数据写回
            }
        }
        if(flag==t.number){
            System.out.println("不存在要修改的数据！");
        }
    }

}
