package com.example.administrator.mytest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {

    private List<Fruit> fruitList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        try{
            initFruit();
            FruitAdapter adapter = new FruitAdapter(this, R.layout.people_item, fruitList);

            ListView listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Fruit fruit = fruitList.get(i);
                    Toast.makeText(Main3Activity.this, fruit.getComment(), Toast.LENGTH_SHORT).show();
                }
            });
            Button button = (Button) findViewById(R.id.buttonStartAll);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent("startActivity");
                    startActivity(intent);

                }
            });

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void initFruit() {
        Fruit banana = new Fruit("王健林", R.drawable.wan, "王健林,1970年入伍，1986年从部队转业，毕业于辽宁大学，7月进入大连市西岗区人民政府任办公室主任，1989年起担任大连万达集团股份有限公司董事长至今。中共十七大代表、第十一届全国政协常委、第十一届全国工商联副主席，兼任中国民间商会副会长、中国企业联合会副会长、中国企业家协会副会长、中国商业联合会副会长、中国慈善联合会副会长。");
        fruitList.add(banana);
        Fruit apple = new Fruit("马云", R.drawable.mayun, "马云，男，1964年9月10日生于浙江省杭州市，祖籍浙江省嵊州市（原嵊县）谷来镇， 阿里巴巴集团主要创始人，现担任阿里巴巴集团董事局主席、日本软银董事、大自然保护协会中国理事会主席兼全球董事会成员、华谊兄弟董事、生命科学突破奖基金会董事。");
        fruitList.add(apple);
        Fruit cherry = new Fruit("马化腾", R.drawable.mahuaten, "马化腾，男，1971年10月29日生于广东省汕头市潮南区。腾讯公司主要创办人之一，现担任腾讯公司控股董事会主席兼首席执行官；全国青联副主席。");
        fruitList.add(cherry);
        Fruit grape = new Fruit("宗庆后", R.drawable.xong, "宗庆后，男，1945年11月16日出生，浙江杭州人。娃哈哈的创始人，现任杭州娃哈哈集团有限公司董事长兼总经理。担任浙江省饮料工业协会会长、第十一届全国人大代表。");
        fruitList.add(grape);
        Fruit mango = new Fruit("姚振华", R.drawable.you, "姚振华,1988年至1992年就读于华南理工大学工业管理工程和食品工程双专业。2016年10月13日，2016年胡润百富榜发布，姚振华以1150亿元财富，位列第四名。[1]  10月20日，2016胡润房地产富豪榜发布，姚振华以400亿位居第六。[2]  10月27日，2016福布斯中国富豪榜公布，姚振华以95亿美元财富，排名第十位。");
        fruitList.add(mango);



    }
}
