package executeSQL;

import java.util.ArrayList;

public class SelSQL {
    public ArrayList selList= new ArrayList();;//存储需要查询的内容
    public ArrayList fromList= new ArrayList();;//存储查询的表名
    public ArrayList conditionList= new ArrayList();;//存储查询条件
    public String order_attribute = null;//存储order by的属性

    public String getOrder_attribute() { return order_attribute; }

    public void setOrder_attribute(String order_attribute) { this.order_attribute = order_attribute; }


    //执行查询select语句
    public void ExecuteSelSQL(){
        Table t = new Table();
        String tabname = null;
        //判断该查询是否是单表查询
        //如果是多表查询
        if(fromList.size()>1){
            System.out.println("本查询只实现单表查询，多表查询未实现！");
            return;
        }
        //如果是单表查询
        else {
            tabname = fromList.get(0).toString();
            t.initConfig(tabname);
            t.init_Data(tabname);
            ArrayList List[] = CommonFun.judgeCondition(conditionList,t);
            //显示查询结果的表头
            String showAtt = "";
            int locations[] = new int[100];
            for(int i=0;i<selList.size();i++){
                showAtt=showAtt+selList.get(i)+" ";
                locations[i]=CommonFun.getLocation(t,selList.get(i).toString());
            }
            System.out.println(showAtt);//输出查询结果的表头
            for(int i=0;i<t.number;i++){
                String result = "";
                for(int j=0;j<selList.size();j++){
                    if(List[i]!=null){
                        result = result+List[i].get(locations[j])+" ";
                    }

                }
                System.out.print(result+"\r\n");
            }
        }
    }
}
