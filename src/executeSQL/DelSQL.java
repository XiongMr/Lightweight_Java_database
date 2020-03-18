package executeSQL;

import java.util.ArrayList;

public class DelSQL {
    public ArrayList fromList = new ArrayList();
    public ArrayList conditionList = new ArrayList();

    //判断是否有语法错误
    public Boolean GrammerMistake(){
        if(fromList.size()>1){
            System.out.println("删除语句的表名不能存在多个！");
            return false;
        }
        return true;
    }

    //执行删除delete语句
    public void ExecuteDelSQL(){
        if(!GrammerMistake()){
            return;
        }
        Table t = new Table();
        String tabname = null;
        tabname = fromList.get(0).toString();
        t.initConfig(tabname);
        t.init_Data(tabname);
        String path = tabname+"data.txt";
        ArrayList List[] = CommonFun.judgeCondition(conditionList,t);
        int flag=0;
        for(int i=0;i<t.number;i++){
            String result="";//存储每一行的内容
            if(List[i]!=null){
                for(int j=0;j<t.attributeList.size();j++){
                    result=result+List[i].get(j)+" ";
                }
                CommonFun.delLine(path,result);
            }
            else
                flag++;
        }
        if(flag==t.number){
            System.out.println("不存在要删除的数据！");
        }
    }
}
